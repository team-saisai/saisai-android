package com.choius323.saisai.ui.screen.map

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class MapViewModel(
    // private val routeDataSource: RouteDataSource,
) : ViewModel(), ContainerHost<MapUiState, MapSideEffect> {

    override val container: Container<MapUiState, MapSideEffect> = container(MapUiState())

    fun onEvent(event: MapUiEvent) {
        when (event) {
            MapUiEvent.BackClicked -> intent {
                postSideEffect(MapSideEffect.NavigateBack)
            }

            is MapUiEvent.SetRoute -> intent {
                reduce {
                    state.copy(
                        route = event.route,
                        checkPointList = event.checkPointList
                    )
                }
            }
        }
    }
}