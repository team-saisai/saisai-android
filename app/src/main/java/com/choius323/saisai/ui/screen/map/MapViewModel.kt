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
                        checkPointList = if (event.route.isNotEmpty()) {
                            List(3) { event.route[((it + 1) * (event.route.lastIndex.toFloat() / 4)).toInt()] }
                        } else {
                            emptyList()
                        }
                    )
                }
            }

            is MapUiEvent.SetIsTracking -> setIsTracking(event)
            is MapUiEvent.SetNowLatLng -> setNowLatLng(event)
            is MapUiEvent.SetPermissionGranted -> intent {
                reduce { state.copy(permissionGranted = event.changed) }
            }

            is MapUiEvent.SetShowPermissionDialog -> intent {
                reduce { state.copy(isShowPermissionDialog = event.isShow) }
            }
        }
    }

    private fun setIsTracking(event: MapUiEvent.SetIsTracking) = intent {
        reduce {
            state.copy(isTracking = true)
        }
    }

    private fun setNowLatLng(event: MapUiEvent.SetNowLatLng) = intent {
        reduce { state.copy(nowLatLng = event.latLng) }
    }
}