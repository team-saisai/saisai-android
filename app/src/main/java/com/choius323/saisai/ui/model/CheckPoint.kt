package com.choius323.saisai.ui.model

import com.kakao.vectormap.LatLng

data class CheckPoint(val lat: Double, val lng: Double, val gpxPointIdx: Int) {
    fun toLatLng() = LatLng.from(lat, lng)

    companion object {
        val sample1 = listOf(
            CheckPoint(
                lat = 36.71069498173892,
                lng = 126.54820843599737,
                0
            ),
            CheckPoint(
                lat = 36.710855243727565,
                lng = 126.54816350899637,
                1
            ),
            CheckPoint(
                lat = 36.71119473218633,
                lng = 126.54910530335293,
                2
            ),
            CheckPoint(
                lat = 36.71169091947375,
                lng = 126.550282118842,
                3
            ),
        )
    }
}