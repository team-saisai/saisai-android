package com.choius323.saisai.ui.model

import com.kakao.vectormap.LatLng

data class GpxPoint(
    val lat: Double, // 위도
    val lng: Double, // 경도
    val elevation: Double,
    val segmentDistance: Double,
) {
    fun toLatLng() = LatLng.from(lat, lng)
}