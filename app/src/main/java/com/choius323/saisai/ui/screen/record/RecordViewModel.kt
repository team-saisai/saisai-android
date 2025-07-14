package com.choius323.saisai.ui.screen.record

import androidx.lifecycle.ViewModel
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.GpxPoint
import com.choius323.saisai.ui.screen.map.updateUserLocation
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class RecordViewModel() : ViewModel(), ContainerHost<RecordUiState, RecordSideEffect> {
    override val container: Container<RecordUiState, RecordSideEffect> = container(RecordUiState())

    init {
        intent {
            val courseDetail = CourseDetail(
                courseId = 1,
                courseName = "남파랑길 1코스",
                summary = "- 해파랑길 시종점인 오륙도 해맞이 공원에서부터 부산 중구 부산대교까지 이어지는 구간<br>- 신선이 노닐던 신선대 및 부산항의 역동적인 파노라마를 만끽할 수 있는 구간으로 세계에서 하나뿐인 UN기념공원 및 부산박물관, 영화 “친구”로 유명한 부산 일대의 명소를 함께 체험할 수 있는 코스<br>- 아름다운 해안경관과 우리나라 제1의 항구도시 부산의 매력을 느낄 수 있는 구간<br>- 부산 갈맷길 3-1, 3-2코스가 중첩됨",
                level = 2,
                distance = 19.0,
                estimatedTime = 420.0,
                sigun = "부산 남구",
                imageUrl = null,
                inProgressUserCount = 0,
                completeUserCount = 0,
                gpxPointList = GpxPoint.gpxPointsSample
            )
            reduce { state.copy(courseDetail = courseDetail) }
        }
    }

    fun onEvent(event: RecordUiEvent) = when (event) {
        RecordUiEvent.ClickedStart -> startRecording()
        is RecordUiEvent.SetPermissionGranted -> intent {
            reduce { state.copy(permissionGranted = event.isGranted) }
        }

        RecordUiEvent.BackClicked -> intent {
            postSideEffect(RecordSideEffect.NavigateBack)
        }

        is RecordUiEvent.SetIsTracking -> setIsTracking(event)
        is RecordUiEvent.SetNowLatLng -> setNowLatLng(event)

        is RecordUiEvent.SetShowPermissionDialog -> intent {
            reduce { state.copy(isShowPermissionDialog = event.isShow) }
        }

        is RecordUiEvent.SetCameraTracking -> intent {
            reduce { state.copy(isCameraTracking = event.isCameraTracking) }
        }

        RecordUiEvent.StartRecording -> startRecording()
    }

    private fun startRecording() = intent {
        println("${state.isRecording} ${state.startTime}")
        postSideEffect(RecordSideEffect.PermissionCheck)
        if (state.permissionGranted.not()) {
            postSideEffect(RecordSideEffect.PermissionRequest)
            return@intent
        } else if (state.isRecording) {
            return@intent
        }
        reduce {
            state.copy(
                isRecording = true,
                isCameraTracking = true,
                startTime = System.currentTimeMillis(),
            )
        }
    }

    private fun setIsTracking(event: RecordUiEvent.SetIsTracking) = intent {
        // reduce {
        //     state.copy(
        //         isCourseStarted = true,
        //         isTracking = true,
        //         isCameraTracking = true,
        //         segmentIndex = 0,
        //     )
        // }
    }

    private fun setNowLatLng(event: RecordUiEvent.SetNowLatLng) = intent {
        if (state.isRecording.not()) return@intent
        reduce { state.copy(nowLatLng = event.latLng) }
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
                endRecording()
            }
        } else {
            reduce { state.copy(error = "코스 추적에 실패했습니다.") }
        }
    }

    private fun endRecording() = intent {
        delay(300)
        reduce {
            state.copy(
                isRecording = false,
                isCameraTracking = false,
            )
        }
    }
}