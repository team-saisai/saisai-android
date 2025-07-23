package com.choius323.saisai.ui.screen.record

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.choius323.saisai.ui.screen.map.ObserveLocation
import com.choius323.saisai.ui.screen.map.createDirectionLabel
import com.choius323.saisai.ui.screen.map.drawRideRoute
import com.choius323.saisai.ui.screen.map.drawRoute
import com.choius323.saisai.ui.screen.map.moveCamera
import com.choius323.saisai.ui.screen.map.rememberMapView
import com.choius323.saisai.ui.screen.map.updateMapData
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapView

@Composable
fun RecordMapSection(
    uiState: RecordUiState,
    modifier: Modifier = Modifier,
    onEvent: (RecordUiEvent) -> Unit,
) {
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }

    val mapView: MapView = rememberMapView {
        if (kakaoMap == null) {
            kakaoMap = it
        }
    }
    RecordMapSetting(uiState, kakaoMap, onEvent)
    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView }, update = {
                Log.d(TAG, "MapView update")
            }, modifier = Modifier.fillMaxSize()
        )
        Text("${uiState.totalRideDistance}")
        if (uiState.isRecording) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 24.dp)
                    .size(24.dp)
                    .background(Color.Black)
                    .clip(CircleShape)
                    .clickable(
                        onClick = {
                            onEvent(
                                RecordUiEvent.SetCameraTracking(uiState.isCameraTracking.not())
                            )
                        }),
                contentDescription = "내 위치로 이동"
            )
        }
    }
}

@Composable
private fun RecordMapSetting(
    uiState: RecordUiState,
    kakaoMap: KakaoMap?,
    onEvent: (RecordUiEvent) -> Unit,
) {
    LaunchedEffect(uiState.isCameraTracking, uiState.nowLatLng) {
        Log.d(
            TAG,
            "isRecording: ${uiState.isRecording}, isCameraTracking: ${uiState.isCameraTracking}, nowLatLng: ${uiState.nowLatLng}"
        )
        if (uiState.isRecording && uiState.isCameraTracking && uiState.nowLatLng != null) {
            kakaoMap.moveCamera(uiState.nowLatLng)
        }
    }
    LaunchedEffect(kakaoMap, uiState.route) {
        updateMapData(kakaoMap, uiState.route)
        val latLngList = uiState.route.map { it.toLatLng() }
        if (uiState.isRecording) {
            kakaoMap.drawRoute(latLngList, Color(0xFFC9FF66).toArgb())
        } else {
            kakaoMap.drawRoute(latLngList, Color(0xFFBABEC3).toArgb())
        }
        kakaoMap.moveCamera(latLngList)
    }
    LaunchedEffect(uiState.nowLatLng) {
        if (uiState.nowLatLng != null) {
            kakaoMap.createDirectionLabel(uiState.nowLatLng)
        }
    }
    LaunchedEffect(uiState.segmentIndex) {
        if (uiState.projectedPoint != null && uiState.segmentIndex > 0 && uiState.isRecording) {
            kakaoMap.drawRideRoute(
                uiState.route,
                uiState.segmentIndex - 1, uiState.segmentIndex + 1
            )
        }
    }
    ObserveLocation(
        permissionGranted = uiState.permissionGranted,
        callbackLocation = { location ->
            onEvent(
                RecordUiEvent.SetNowLatLng(
                    LatLng.from(
                        location.latitude, location.longitude
                    )
                )
            )
        },
    )
}

private const val TAG = "RecordMapSection"