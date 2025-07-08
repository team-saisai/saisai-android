package com.choius323.saisai.ui.model

import com.kakao.vectormap.LatLng

data class Point(
    val lat: Double, // 위도
    val lng: Double, // 경도
) {
    fun toLatLng() = LatLng.from(lat, lng)
}