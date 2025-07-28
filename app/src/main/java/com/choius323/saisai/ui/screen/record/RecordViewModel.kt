package com.choius323.saisai.ui.screen.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.navigation.MainNavItem
import com.choius323.saisai.ui.screen.map.calculateDistance
import com.choius323.saisai.ui.screen.map.updateUserLocation
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
                            courseDetail = courseDetail.copy(
                                gpxPointList = courseDetail.gpxPointList.take(
                                    5
                                )
                            ), isLoading = false
                        )
                    }
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
        RecordUiEvent.StopRecording -> stopRecording()
        RecordUiEvent.ResumeRecording -> resumeRecording()
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
            state.route.isNotEmpty() &&
                    calculateDistance(event.nowLatLng, state.route.first().toLatLng()) < 25
                -> {
                courseRepository.startCourse(courseId).collectLatest { result ->
                    result.onSuccess { rideId ->
                        reduce {
                            state.copy(
                                isRecording = true,
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
                postSideEffect(RecordSideEffect.ShowToast("코스 시작점에서 멀리 떨어져있습니다."))
            }
        }
        reduce { state.copy(isLoading = false) }
    }

    private fun setNowLatLng(event: RecordUiEvent.SetNowLatLng) = intent {
        if (state.permissionGranted.not()) return@intent
        reduce { state.copy(nowLatLng = event.latLng) }
        if (state.isRecording.not()) return@intent
        val rideSnapshot = updateUserLocation(event.latLng, state.segmentIndex, state.route)
        if (rideSnapshot != null) {
            reduce {
                state.copy(
                    segmentIndex = rideSnapshot.segmentIndex,
                    totalRideDistance = rideSnapshot.totalDistance,
                    projectedPoint = rideSnapshot.projectedPoint,
                )
            }
            if (rideSnapshot.segmentIndex == state.courseDetail?.gpxPointList?.lastIndex) {
                completeRecording()
            }
        } else {
            postSideEffect(RecordSideEffect.ShowToast("코스 추적에 실패했습니다."))
        }
    }

    private fun completeRecording() = intent {
        delay(300)
        val courseDetail = state.courseDetail ?: return@intent
        reduce { state.copy(isLoading = true) }
        courseRepository.completeCourse(
            courseDetail.courseId,
            System.currentTimeMillis() - state.startTime,
            state.totalRideDistance
        ).collectLatest { result ->
            result.onSuccess {
                reduce {
                    state.copy(
                        isRecording = false,
                        isCameraTracking = false,
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

    private fun stopRecording() = intent {
        //TODO: 로직 추가
    }

    private fun resumeRecording() = intent {
        //TODO: 로직 추가
    }
}