package com.choius323.saisai.ui.screen.record

import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.GpxPoint
import com.kakao.vectormap.LatLng

data class RecordUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courseDetail: CourseDetail? = null,
    val permissionGranted: Boolean = false,
    val rideState: RideState = RideState.READY,
    val totalSeconds: Long = 0,
    val lastTimestamp:Long = 0,
    val isShowPermissionDialog: Boolean = false,
    val isCameraTracking: Boolean = false,
    val nowCheckPointIndex: Int = -1,
    val nowLatLng: LatLng? = null,
    val rideId: Long = 0,
    val isShowCompleteDialog: Boolean = false,
    val isExpandedSummary: Boolean = false,
) {
    val route: List<GpxPoint>
        get() = courseDetail?.gpxPointList ?: emptyList()
    val totalRideDistance: Double
        get() = courseDetail?.checkPointList?.getOrNull(nowCheckPointIndex)?.totalDistance ?: 0.0
}

sealed interface RecordUiEvent {
    data class ClickedStart(val isPermissionGranted: Boolean) : RecordUiEvent
    data class SetPermissionGranted(val isGranted: Boolean) : RecordUiEvent
    data object BackClicked : RecordUiEvent
    data class SetNowLatLng(val latLng: LatLng) : RecordUiEvent
    data class SetShowPermissionDialog(val isShow: Boolean) : RecordUiEvent
    data class SetCameraTracking(val isCameraTracking: Boolean) : RecordUiEvent
    data class StartRecording(val isPermissionGranted: Boolean, val nowLatLng: LatLng) :
        RecordUiEvent

    data object StopRecording : RecordUiEvent
    data object ResumeRecording : RecordUiEvent
    data object OnToggleExpandedSummary : RecordUiEvent
    data object OnClickToggleRecording : RecordUiEvent
}

sealed interface RecordSideEffect {
    data object PermissionCheck : RecordSideEffect
    data object NavigateBack : RecordSideEffect
    data object StartRecording : RecordSideEffect
    data class PermissionRequest(val isStartRecord: Boolean = false) : RecordSideEffect
    data class ShowToast(val msg: String) : RecordSideEffect
}

enum class RideState {
    READY,
    RECORDING,
    PAUSED,
    COMPLETE
    ;

    fun isRecording() = this == RECORDING
}