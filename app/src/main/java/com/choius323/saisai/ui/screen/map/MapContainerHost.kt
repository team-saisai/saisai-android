package com.choius323.saisai.ui.screen.map

import com.choius323.saisai.ui.model.GpxPoint

data class MapUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val route: List<GpxPoint> = emptyList(),
)

sealed interface MapUiEvent {
    data object BackClicked : MapUiEvent
    data class SetRoute(val route: List<GpxPoint>) : MapUiEvent
}

sealed interface MapSideEffect {
    data object NavigateBack : MapSideEffect
}