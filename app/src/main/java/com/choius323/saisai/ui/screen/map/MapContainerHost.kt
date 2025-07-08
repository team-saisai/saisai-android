package com.choius323.saisai.ui.screen.map

import com.choius323.saisai.ui.model.GpxPoint
import com.kakao.vectormap.LatLng

data class MapUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val route: List<GpxPoint> = emptyList(),
    val nowLatLng: LatLng? = null,
    val isTracking: Boolean = false,
    val permissionGranted: Boolean = false,
    val isShowPermissionDialog: Boolean = false,
    val isCameraTracking: Boolean = false,
    val isCourseStarted: Boolean = false,
)

sealed interface MapUiEvent {
    data object BackClicked : MapUiEvent
    data object StartRecording : MapUiEvent
    data class SetRoute(val route: List<GpxPoint>) : MapUiEvent
    data class SetNowLatLng(val latLng: LatLng) : MapUiEvent
    data class SetIsTracking(val isTracking: Boolean) : MapUiEvent
    data class SetPermissionGranted(val changed: Boolean) : MapUiEvent
    data class SetShowPermissionDialog(val isShow: Boolean) : MapUiEvent
    data class SetCameraTracking(val isCameraTracking: Boolean) : MapUiEvent
}

sealed interface MapSideEffect {
    data object PermissionCheck : MapSideEffect
    data object NavigateBack : MapSideEffect
    data object PermissionRequest : MapSideEffect
}