package com.choius323.saisai.ui.screen.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.choius323.saisai.ui.component.ComposableLifecycle
import com.choius323.saisai.ui.model.GpxPoint
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MapScreen(
    route: List<GpxPoint>,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel,
    goBack: () -> Unit = {},
) {
    val uiState by viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MapSideEffect.NavigateBack -> goBack()
        }
    }
    MapScreen(
        uiState = uiState, modifier = modifier
    )
}

@Composable
private fun MapScreen(
    uiState: MapUiState,
    modifier: Modifier = Modifier,
) {
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }

    val mapView: MapView = rememberMapView {
        if (kakaoMap == null) {
            kakaoMap = it
        }
    }
    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView }, update = {
                Log.d(TAG, "MapView update")
                updateMapData(
                    kakaoMap, uiState.route
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun rememberMapView(setKakaoMap: (KakaoMap) -> Unit): MapView {
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

private const val TAG = "MapScreen"