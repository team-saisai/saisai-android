package com.choius323.saisai.ui.screen.map

import com.choius323.saisai.ui.model.CheckPoint
import com.choius323.saisai.ui.model.GpxPoint
import com.kakao.vectormap.LatLng

data class MapUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val route: List<GpxPoint> = emptyList(),
    val checkPointList: List<CheckPoint> = emptyList(),
)

sealed interface MapUiEvent {
    data object BackClicked : MapUiEvent
    data class SetRoute(val route: List<GpxPoint>, val checkPointList: List<CheckPoint>) :
        MapUiEvent
}

sealed interface MapSideEffect {
    data object NavigateBack : MapSideEffect
    data class ShowToast(val message: String) : MapSideEffect
}