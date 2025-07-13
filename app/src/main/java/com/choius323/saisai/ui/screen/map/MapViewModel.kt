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

            is MapUiEvent.SetIsTracking -> setIsTracking(event)
            is MapUiEvent.SetNowLatLng -> setNowLatLng(event)
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

    private fun setIsTracking(event: MapUiEvent.SetIsTracking) = intent {
        reduce {
            state.copy(
                isCourseStarted = true,
                isTracking = true,
                isCameraTracking = true,
                segmentIndex = 0,
            )
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
        onEvent(MapUiEvent.SetIsTracking(true))
    }

    private fun setNowLatLng(event: MapUiEvent.SetNowLatLng) = intent {
        reduce { state.copy(nowLatLng = event.latLng) }
        val rideSnapshot = updateUserLocation(event.latLng, state.segmentIndex, state.route)
        if (rideSnapshot != null) {
            if (rideSnapshot.segmentIndex != state.route.lastIndex) {
                reduce {
                    state.copy(
                        segmentIndex = rideSnapshot.segmentIndex,
                        totalRideDistance = rideSnapshot.totalDistance,
                        projectedPoint = rideSnapshot.projectedPoint,
                    )
                }
            } else {
                // TODO: Record 종료
            }
        } else {
            reduce { state.copy(error = "코스 추적에 실패했습니다.") }
        }
    }
}