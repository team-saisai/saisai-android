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
                reduce { state.copy(route = event.route) }
            }

            is MapUiEvent.SetIsTracking -> intent {
                reduce { state.copy(isTracking = event.isTracking) }
            }

            is MapUiEvent.SetNowLatLng -> intent {
                reduce { state.copy(nowLatLng = event.latLng) }
            }

            is MapUiEvent.SetPermissionGranted -> intent {
                reduce { state.copy(permissionGranted = event.changed) }
            }

            is MapUiEvent.SetShowPermissionDialog -> intent {
                reduce { state.copy(isShowPermissionDialog = event.isShow) }
            }

            is MapUiEvent.SetCameraTracking -> intent {
                reduce { state.copy(isCameraTracking = event.isCameraTracking) }
            }

            MapUiEvent.StartRecording -> startRecording()
        }
    }

    private fun startRecording() = intent {
        postSideEffect(MapSideEffect.PermissionCheck)
        if (state.permissionGranted.not()) {
            postSideEffect(MapSideEffect.PermissionRequest)
            return@intent
        } else if (state.isCourseStarted) {
            return@intent
        }
        reduce {
            state.copy(
                isCourseStarted = true,
                isTracking = true,
                isCameraTracking = true,
            )
        }
    }
}