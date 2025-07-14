package com.choius323.saisai.ui.screen.record

import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.GpxPoint
import com.kakao.vectormap.LatLng

data class RecordUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val courseDetail: CourseDetail? = null,
    val permissionGranted: Boolean = false,
    val isRecording: Boolean = false,
    val startTime: Long = 0,
    val isShowPermissionDialog: Boolean = false,
    val isCameraTracking: Boolean = false,
    val segmentIndex: Int = 0,
    val totalRideDistance: Double = 0.0,
    val projectedPoint: LatLng? = null,
    val nowLatLng: LatLng? = null,
) {
    val route: List<GpxPoint>
        get() = courseDetail?.gpxPointList ?: emptyList()
}

sealed interface RecordUiEvent {
    data object ClickedStart : RecordUiEvent
    data class SetPermissionGranted(val isGranted: Boolean) : RecordUiEvent
    data object BackClicked : RecordUiEvent
    data object StartRecording : RecordUiEvent
    data class SetNowLatLng(val latLng: LatLng) : RecordUiEvent
    data class SetIsTracking(val isTracking: Boolean) : RecordUiEvent
    data class SetShowPermissionDialog(val isShow: Boolean) : RecordUiEvent
    data class SetCameraTracking(val isCameraTracking: Boolean) : RecordUiEvent
}

sealed interface RecordSideEffect {
    data object PermissionCheck : RecordSideEffect
    data object NavigateBack : RecordSideEffect
    data object PermissionRequest : RecordSideEffect
}