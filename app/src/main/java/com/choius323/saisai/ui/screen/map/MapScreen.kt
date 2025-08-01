package com.choius323.saisai.ui.screen.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.choius323.saisai.ui.component.ComposableLifecycle
import com.choius323.saisai.ui.component.HandlePermissionActions
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.util.locationPermissions
import com.choius323.saisai.util.postNotificationPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel,
    goBack: () -> Unit = {},
) {
    val uiState by viewModel.collectAsState()

    val permissionState = rememberMultiplePermissionsState(
        postNotificationPermissions + locationPermissions
    ) { resultMap ->
        viewModel.onEvent(MapUiEvent.SetPermissionGranted(resultMap.all { it.value }))
    }
    HandlePermissionActions(
        permissionState,
        isShowPermissionDialog = uiState.isShowPermissionDialog,
        setShowPermissionDialog = { viewModel.onEvent(MapUiEvent.SetShowPermissionDialog(it)) },
        onPermissionAllGranted = {
            if (permissionState.allPermissionsGranted) {
                viewModel.onEvent(MapUiEvent.SetPermissionGranted(true))
            } else {
                viewModel.onEvent(MapUiEvent.SetShowPermissionDialog(true))
            }
        })
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MapSideEffect.NavigateBack -> goBack()
            is MapSideEffect.PermissionCheck -> viewModel.onEvent(
                MapUiEvent.SetPermissionGranted(
                    permissionState.allPermissionsGranted
                )
            )

            is MapSideEffect.PermissionRequest -> permissionState.launchMultiplePermissionRequest()
        }
    }
    ObserveLocation(
        permissionGranted = uiState.permissionGranted,
        callbackLocation = { location ->
            println("callback $location")
            viewModel.onEvent(
                MapUiEvent.SetNowLatLng(
                    LatLng.from(
                        location.latitude, location.longitude
                    )
                )
            )
        },
    )

    LaunchedEffect(Unit) {
        viewModel.onEvent(MapUiEvent.SetPermissionGranted(permissionState.allPermissionsGranted))
    }
    MapScreen(
        uiState = uiState, modifier = modifier, viewModel::onEvent
    )
}

@Composable
private fun MapScreen(
    uiState: MapUiState,
    modifier: Modifier = Modifier,
    onEvent: (MapUiEvent) -> Unit,
) {
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }

    val mapView: MapView = rememberMapView {
        if (kakaoMap == null) {
            kakaoMap = it
        }
    }
    LaunchedEffect(uiState.isCameraTracking, uiState.nowLatLng) {
        Log.d(
            TAG,
            "isTracking: ${uiState.isTracking}, isCameraTracking: ${uiState.isCameraTracking}, nowLatLng: ${uiState.nowLatLng}"
        )
        if (uiState.isTracking && uiState.isCameraTracking && uiState.nowLatLng != null) {
            kakaoMap.moveCamera(uiState.nowLatLng)
        }
    }
    LaunchedEffect(kakaoMap, uiState.route) {
        updateMapData(kakaoMap, uiState.route)
        val latLngList = uiState.route.map { it.toLatLng() }
        kakaoMap.drawRoute(latLngList, SaiColor.Lime.toArgb())
        kakaoMap.moveCamera(latLngList)
    }
    LaunchedEffect(uiState.nowLatLng) {
        if (uiState.nowLatLng != null) {
            kakaoMap.createDirectionLabel(uiState.nowLatLng)
        }
    }
    LaunchedEffect(uiState.segmentIndex) {
        if (uiState.projectedPoint != null && uiState.segmentIndex > 0 && uiState.isTracking) {
            kakaoMap.drawRideRoute(
                uiState.route,
                uiState.segmentIndex - 1, uiState.segmentIndex + 1
            )
        }
    }
    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView }, update = {
                Log.d(TAG, "MapView update")
            }, modifier = Modifier.fillMaxSize()
        )
        Text("${uiState.totalRideDistance}")
        if (uiState.isTracking) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 24.dp)
                    .size(24.dp)
                    .background(SaiColor.Black)
                    .clip(CircleShape)
                    .clickable(onClick = {
                        onEvent(
                            MapUiEvent.SetCameraTracking(
                                uiState.isCameraTracking.not()
                            )
                        )
                    }),
                contentDescription = "내 위치로 이동"
            )
        }
    }
}

@Composable
fun rememberMapView(setKakaoMap: (KakaoMap) -> Unit): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d(TAG, "onMapDestroy")
                }

                override fun onMapError(exception: Exception?) {
                    Log.d(TAG, "onMapError: $exception")
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    Log.d(TAG, "onMapReady $map")
                    setKakaoMap(map)
                    map.compass?.show()
                    map.compass?.isBackToNorthOnClick = true
                }
            })
        }
    }
    ComposableLifecycle { event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> mapView.resume()
            Lifecycle.Event.ON_PAUSE -> mapView.pause()
            else -> {}
        }
    }
    return mapView
}

@SuppressLint("MissingPermission")
@Composable
fun ObserveLocation(
    permissionGranted: Boolean,
    callbackLocation: (location: Location) -> Unit,
) {
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    callbackLocation(location)
                }
            }
        }
    }
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    LifecycleResumeEffect(permissionGranted) {
        if (permissionGranted) {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000
            ).build()

            // 위치 업데이트 시작
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } else {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

        onPauseOrDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    callbackLocation: (Location) -> Unit,
    callbackCanceled: () -> Unit = {},
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener(callbackLocation)
        .addOnCanceledListener(callbackCanceled)
}

private const val TAG = "MapScreen"