package com.choius323.saisai.ui.screen.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.navigation.MainNavItem
import com.choius323.saisai.ui.screen.map.calculateDistance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class RecordViewModel(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
) : ViewModel(), ContainerHost<RecordUiState, RecordSideEffect> {
    override val container: Container<RecordUiState, RecordSideEffect> =
        container(RecordUiState())
    val courseId = savedStateHandle.toRoute<MainNavItem.Record>().courseId

    init {
        intent {
            reduce { state.copy(isLoading = true) }
            courseRepository.getCourseDetail(courseId).collectLatest { result ->
                result.onSuccess { courseDetail ->
                    reduce {
                        state.copy(
                            // TODO: 실제 데이터로 수정 필요
                            courseDetail = courseDetail.copy(
                                gpxPointList = courseDetail.gpxPointList.take(5),
                                checkPointList = listOf(
                                    courseDetail.gpxPointList[0],
                                    courseDetail.gpxPointList[2],
                                    courseDetail.gpxPointList[4],
                                )
                            ), isLoading = false
                        )
                    }
                    postSideEffect(RecordSideEffect.StartRecording)
                }.onFailure {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(RecordSideEffect.ShowToast("코스 정보를 가져오는데 실패했습니다."))
                }
            }
        }
    }

    fun onEvent(event: RecordUiEvent) = when (event) {
        is RecordUiEvent.ClickedStart -> clickStart(event)
        is RecordUiEvent.SetNowLatLng -> setNowLatLng(event)
        is RecordUiEvent.StartRecording -> startRecording(event)
        RecordUiEvent.StopRecording -> pauseRecording()
        RecordUiEvent.ResumeRecording -> resumeRecording()
        RecordUiEvent.OnClickToggleRecording -> intent {
            if (state.isPaused) {
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

        RecordUiEvent.BackClicked -> intent {
            postSideEffect(RecordSideEffect.NavigateBack)
        }

        is RecordUiEvent.SetShowPermissionDialog -> intent {
            reduce { state.copy(isShowPermissionDialog = event.isShow) }
        }

        is RecordUiEvent.SetCameraTracking -> intent {
            reduce { state.copy(isCameraTracking = event.isCameraTracking) }
        }

    }

    private fun clickStart(event: RecordUiEvent.ClickedStart) = intent {
        if (state.isRecording) return@intent
        if (event.isPermissionGranted) {
            postSideEffect(RecordSideEffect.StartRecording)
        } else {
            postSideEffect(RecordSideEffect.PermissionRequest(isStartRecord = true))
        }
    }

    private fun startRecording(event: RecordUiEvent.StartRecording) = intent {
        reduce { state.copy(isLoading = true) }
        when {
            event.isPermissionGranted.not() -> postSideEffect(RecordSideEffect.ShowToast("위치 및 알림 권한이 필요합니다."))
            state.isRecording -> postSideEffect(RecordSideEffect.ShowToast("이미 실행 중 입니다."))
            state.route.isNotEmpty() -> {
                courseRepository.startCourse(courseId).collectLatest { result ->
                    result.onSuccess { rideId ->
                        reduce {
                            state.copy(
                                isRecording = true,
                                isPaused = false,
                                isCameraTracking = true,
                                startTime = System.currentTimeMillis(),
                                rideId = rideId
                            )
                        }
                    }.onFailure {
                        postSideEffect(RecordSideEffect.ShowToast("코스를 시작하는데 실패했습니다."))
                    }
                }
            }

            else -> {
                postSideEffect(RecordSideEffect.ShowToast("코스를 시작할 수 없습니다."))
            }
        }
        reduce { state.copy(isLoading = false) }
    }

    private fun setNowLatLng(event: RecordUiEvent.SetNowLatLng) = intent {
        if (state.permissionGranted.not()) return@intent
        reduce { state.copy(nowLatLng = event.latLng) }
        if (state.isRecording.not() &&
            state.nowCheckPointIndex == state.courseDetail?.checkPointList?.lastIndex
        ) return@intent
        val nextLatLng =
            state.courseDetail?.checkPointList?.get(state.nowCheckPointIndex + 1)?.toLatLng()
                ?: return@intent
        val distance = calculateDistance(event.latLng, nextLatLng)
        if (distance < 10f) {
            reduce {
                state.copy(
                    nowCheckPointIndex = state.nowCheckPointIndex + 1,
                )
            }
            if (state.nowCheckPointIndex == state.courseDetail?.checkPointList?.lastIndex) {
                completeRecording()
            }
        }
    }

    private fun completeRecording() = intent {
        delay(300)
        val courseDetail = state.courseDetail ?: return@intent
        reduce {
            state.copy(
                isLoading = true,
                isCameraTracking = false,
                isRecording = false,
                isPaused = true,
            )
        }
        courseRepository.completeCourse(
            state.rideId,
            System.currentTimeMillis() - state.startTime,
            courseDetail.distance
        ).collectLatest { result ->
            result.onSuccess {
                reduce {
                    state.copy(
                        isShowCompleteDialog = true,
                        isLoading = false,
                    )
                }
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

    private fun pauseRecording() = intent {
        reduce { state.copy(isLoading = true) }
        val courseDetail = state.courseDetail ?: return@intent
        courseRepository.pauseRide(
            state.rideId,
            System.currentTimeMillis() - state.startTime,
            courseDetail.checkPointList[state.nowCheckPointIndex].totalDistance
        ).collectLatest { result ->
            result.onSuccess {
                reduce { state.copy(isPaused = true, isLoading = false) }
            }.onFailure {
                postSideEffect(RecordSideEffect.ShowToast(it.message ?: "주행 정보를 서버에 전송하지 못했습니다."))
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private fun resumeRecording() = intent {
        reduce { state.copy(isLoading = true) }
        courseRepository.resumeRide(state.rideId)
            .collectLatest { result ->
                result.onSuccess {
                    reduce { state.copy(isPaused = false, isLoading = false) }
                }.onFailure {
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