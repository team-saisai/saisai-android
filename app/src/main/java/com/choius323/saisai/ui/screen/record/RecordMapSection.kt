package com.choius323.saisai.ui.screen.record

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.choius323.saisai.ui.screen.map.ObserveLocation
import com.choius323.saisai.ui.screen.map.createDirectionLabel
import com.choius323.saisai.ui.screen.map.drawRideRoute
import com.choius323.saisai.ui.screen.map.drawRoute
import com.choius323.saisai.ui.screen.map.initCircles
import com.choius323.saisai.ui.screen.map.moveCamera
import com.choius323.saisai.ui.screen.map.rememberMapView
import com.choius323.saisai.ui.screen.map.setCirclesStyle
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
    }
}

@Composable
private fun RecordMapSetting(
    uiState: RecordUiState,
    kakaoMap: KakaoMap?,
    onEvent: (RecordUiEvent) -> Unit,
) {
    LaunchedEffect(uiState.isCameraTracking, uiState.nowLatLng) {
        if (uiState.rideState == RideState.RECORDING && uiState.isCameraTracking && uiState.nowLatLng != null) {
            Log.d(
                TAG,
                "isRecording: ${uiState.rideState}, nowLatLng: ${uiState.nowLatLng}"
            )
//            kakaoMap.moveCamera(uiState.nowLatLng)
        }
    }
    LaunchedEffect(kakaoMap, uiState.route) {
        updateMapData(kakaoMap, uiState.route)
        val latLngList = uiState.route.map { it.toLatLng() }
        kakaoMap.drawRoute(latLngList, Color(0xFFBABEC3).toArgb())
        kakaoMap.moveCamera(latLngList)
    }
    LaunchedEffect(kakaoMap, uiState.courseDetail?.checkPointList) {
        val checkPointList =
            uiState.courseDetail?.checkPointList?.map { LatLng.from(it.lat, it.lng) } ?: emptyList()
        kakaoMap.initCircles(checkPointList, false)
    }
    LaunchedEffect(uiState.nowLatLng) {
        if (uiState.nowLatLng != null) {
            kakaoMap.createDirectionLabel(uiState.nowLatLng)
        }
    }
    LaunchedEffect(kakaoMap, uiState.nowCheckPointIndex, uiState.rideState) {
        val courseDetail = uiState.courseDetail
        val nowCheckPointIdx = uiState.nowCheckPointIndex
        if (courseDetail != null && uiState.rideState == RideState.RECORDING && nowCheckPointIdx in courseDetail.checkPointList.indices) {
            val checkPointList = courseDetail.checkPointList.map { LatLng.from(it.lat, it.lng) }
            kakaoMap.setCirclesStyle(checkPointList, nowCheckPointIdx)
            kakaoMap.drawRideRoute(
                uiState.route, 0, courseDetail.checkPointList[nowCheckPointIdx].gpxPointIdx
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