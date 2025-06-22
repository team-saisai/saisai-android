package com.choius323.saisai.ui.screen.map

import com.choius323.saisai.ui.model.Position

data class MapUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val route: List<Position> = emptyList(),
)

sealed interface MapUiEvent {
    data object BackClicked : MapUiEvent
}

sealed interface MapSideEffect {
    data object NavigateBack : MapSideEffect
}