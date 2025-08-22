package com.choius323.saisai.ui.screen.record

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.navigation.MainNavItem
import com.choius323.saisai.ui.screen.map.calculateDistance
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class RecordViewModel(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
) : ViewModel(), ContainerHost<RecordUiState, RecordSideEffect> {
    override val container: Container<RecordUiState, RecordSideEffect> = container(RecordUiState())
    val courseId = savedStateHandle.toRoute<MainNavItem.Record>().courseId
    private var timerJob: Job? = null

    init {
        intent {
            reduce { state.copy(isLoading = true) }
            courseRepository.getCourseDetail(courseId).collectLatest { result ->
                result.onSuccess { courseDetail ->
                    reduce {
                        state.copy(
                            courseDetail = courseDetail,
                            isLoading = false,
                            rideId = courseDetail.rideId ?: 0,
                        )
                    }
                    postSideEffect(RecordSideEffect.StartRecording)
                }.onFailure {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(RecordSideEffect.ShowToast(it.message ?: "코스 정보를 가져오는데 실패했습니다."))
                }
            }
        }
    }

    fun onEvent(event: RecordUiEvent) = when (event) {
        is RecordUiEvent.ClickedStart -> clickStart(event)
        is RecordUiEvent.SetNowLatLng -> setNowLatLng(event)
        is RecordUiEvent.StartRecording -> startRecording(event.isPermissionGranted, event.latLng)
        RecordUiEvent.StopRecording -> pauseRecording()
        RecordUiEvent.ResumeRecording -> resumeRecording()
        RecordUiEvent.OnClickToggleRecording -> intent {
            if (state.rideState == RideState.PAUSED) {
                resumeRecording()
            } else {
                pauseRecording()
            }
        }

        RecordUiEvent.OnToggleExpandedSummary -> intent {
            reduce { state.copy(isExpandedSummary = state.isExpandedSummary.not()) }
        }

        is RecordUiEvent.SetPermissionGranted -> intent {
            reduce { state.copy(permissionGranted = event.isGranted) }
        }

        RecordUiEvent.OnClickBack -> onClickBack()
        is RecordUiEvent.OnClickBackDialog -> onClickBackDialog(event.isConfirm)
        is RecordUiEvent.SetShowPermissionDialog -> intent {
            reduce { state.copy(isShowPermissionDialog = event.isShow) }
        }

        is RecordUiEvent.SetCameraTracking -> intent {
            reduce { state.copy(isCameraTracking = event.isCameraTracking) }
        }
    }

    private fun clickStart(event: RecordUiEvent.ClickedStart) = intent {
        if (state.rideState == RideState.RECORDING) return@intent
        if (event.isPermissionGranted) {
            postSideEffect(RecordSideEffect.StartRecording)
        } else {
            postSideEffect(RecordSideEffect.PermissionRequest(isStartRecord = true))
        }
    }

    private fun startRecording(isPermissionGranted: Boolean, nowLatLng: LatLng) = intent {
        reduce { state.copy(isLoading = true) }
        when {
            isPermissionGranted.not() -> postSideEffect(RecordSideEffect.ShowToast("위치 및 알림 권한이 필요합니다."))
            state.rideState == RideState.RECORDING -> postSideEffect(RecordSideEffect.ShowToast("이미 실행 중 입니다."))
            state.rideId != 0L -> resumeRecording()
            calculateDistance(nowLatLng, state.route.first().toLatLng()) > DISTANCE_STANDARD ->
                postSideEffect(RecordSideEffect.ShowToast("코스를 시작할 수 없습니다. 시작지점 근처로 이동해주세요."))

            state.route.isNotEmpty() -> startRecording()
            else -> postSideEffect(RecordSideEffect.ShowToast("코스를 시작할 수 없습니다."))
        }
        reduce { state.copy(isLoading = false) }
    }

    private fun startRecording() = intent {
        courseRepository.startCourse(courseId).collectLatest { result ->
            result.onSuccess { rideId ->
                reduce {
                    state.copy(
                        rideState = RideState.RECORDING,
                        isCameraTracking = true,
                        totalTime = 0,
                        rideId = rideId,
                        isLoading = false,
                    )
                }
                startTimer()
            }.onFailure {
                postSideEffect(RecordSideEffect.ShowToast(it.message ?: "코스를 시작하는데 실패했습니다."))
            }
        }
    }

    private fun setNowLatLng(event: RecordUiEvent.SetNowLatLng) = intent {
        val courseDetail = state.courseDetail
        if (state.permissionGranted.not() || courseDetail == null) return@intent
        reduce { state.copy(nowLatLng = event.latLng) }
        if (state.rideState != RideState.RECORDING && state.nowCheckPointIndex == courseDetail.checkPointList.lastIndex) return@intent
        val nextCheckPointIndex = state.nowCheckPointIndex + 1
        val nextLatLng =
            if (state.nowCheckPointIndex == courseDetail.checkPointList.lastIndex) {
                courseDetail.gpxPointList.last()
            } else {
                courseDetail.gpxPointList[courseDetail.checkPointList[nextCheckPointIndex].gpxPointIdx]
            }.toLatLng()
        val distance = calculateDistance(event.latLng, nextLatLng)
        if (distance < DISTANCE_STANDARD) {
            if (nextCheckPointIndex == courseDetail.checkPointList.size) {
                completeRecording()
            } else {
                reduce { state.copy(nowCheckPointIndex = nextCheckPointIndex) }
                saveRideState(checkPointIndex = nextCheckPointIndex)
            }
        }
    }

    private fun completeRecording() = intent {
        delay(300)
        reduce {
            state.copy(
                isLoading = true,
                isCameraTracking = false,
                rideState = RideState.COMPLETE,
            )
        }
        courseRepository.completeCourse(
            state.rideId, state.totalSeconds
        ).collectLatest { result ->
            result.onSuccess {
                reduce {
                    state.copy(
                        isShowCompleteDialog = true,
                        isLoading = false,
                    )
                }
                stopTimer()
            }.onFailure {
                reduce {
                    state.copy(
                        isLoading = false,
                    )
                }
                postSideEffect(RecordSideEffect.ShowToast(it.message ?: "완주 정보를 서버에 보내지 못했습니다."))
            }
        }
    }

    private fun pauseRecording(isFinish: Boolean = false) = intent {
        if (state.rideState.isRecording().not()) return@intent
        reduce { state.copy(isLoading = true) }
        courseRepository.pauseRide(
            state.rideId,
            state.totalSeconds,
            state.nowCheckPointIndex,
        ).collectLatest { result ->
            result.onSuccess {
                reduce { state.copy(rideState = RideState.PAUSED, isLoading = false) }
                stopTimer()
                if (isFinish) {
                    postSideEffect(RecordSideEffect.NavigateBack)
                }
            }.onFailure {
                postSideEffect(RecordSideEffect.ShowToast(it.message ?: "주행 정보를 서버에 전송하지 못했습니다."))
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private fun resumeRecording() = intent {
        reduce { state.copy(isLoading = true) }
        courseRepository.resumeRide(state.rideId).collectLatest { result ->
            result.onSuccess { resumeRideInfo ->
                reduce {
                    state.copy(
                        rideState = RideState.RECORDING,
                        isLoading = false,
                        nowCheckPointIndex = maxOf(
                            state.nowCheckPointIndex, resumeRideInfo.checkpointIdx
                        ),
                        totalTime = maxOf(state.totalSeconds, resumeRideInfo.duration) * 1000,
                    )
                }
                startTimer()
            }.onFailure {
                if (it.message?.contains("RD_ER_06") == true) {
                    startRecording()
                } else {
                    postSideEffect(
                        RecordSideEffect.ShowToast(
                            it.message ?: "주행 정보를 서버에 전송하지 못했습니다."
                        )
                    )
                    reduce { state.copy(isLoading = false) }
                }
            }
        }
    }

    private fun saveRideState(checkPointIndex: Int) = intent {
        if (checkPointIndex == state.courseDetail?.checkPointList?.lastIndex) return@intent
        courseRepository.syncRide(state.rideId, state.totalSeconds, checkPointIndex)
            .collectLatest { result ->
                result.onSuccess {
                    postSideEffect(RecordSideEffect.ShowToast("${checkPointIndex + 1}번째 체크 포인트를 저장했습니다."))
                }.onFailure {
                    postSideEffect(
                        RecordSideEffect.ShowToast(
                            it.message ?: "주행 정보를 서버에 전송하지 못했습니다."
                        )
                    )
                }
            }
    }

    private fun onClickBack() = intent {
        Log.d(TAG, "onClickBack")
        if (state.rideState == RideState.RECORDING) {
            reduce { state.copy(isShowBackDialog = true) }
        } else {
            postSideEffect(RecordSideEffect.NavigateBack)
        }
    }

    private fun onClickBackDialog(isConfirm: Boolean) = intent {
        if (isConfirm) {
            onClickBack()
        } else {
            reduce { state.copy(isShowBackDialog = false) }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        intent { reduce { state.copy(lastTimestamp = System.currentTimeMillis()) } }
        timerJob = viewModelScope.launch {
            intent {
                while (true) {
                    delay(500L)
                    val now = System.currentTimeMillis()
                    val elapsedTime = now - state.lastTimestamp
                    reduce {
                        state.copy(
                            totalTime = state.totalTime + elapsedTime, lastTimestamp = now
                        )
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }
}

private const val TAG = "RecordViewModel"
private const val DISTANCE_STANDARD = 25f