package com.choius323.saisai.ui.screen.map

import android.location.Location
import com.kakao.vectormap.LatLng

/**
 * 두 지점([LatLng]) 사이의 거리를 미터(meter) 단위로 계산합니다.
 * Android의 [Location.distanceBetween] 유틸리티를 활용합니다.
 */
fun calculateDistance(point1: LatLng, point2: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(
        point1.latitude, point1.longitude, point2.latitude, point2.longitude, results
    )
    return results[0]
}

private const val TAG = "CalculateDistance"