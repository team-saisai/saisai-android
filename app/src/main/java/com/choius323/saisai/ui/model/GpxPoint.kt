package com.choius323.saisai.ui.model

import com.kakao.vectormap.LatLng

data class GpxPoint(
    val lat: Double, // 위도
    val lng: Double, // 경도
    val elevation: Double?,
    val segmentDistance: Double,
    val totalDistance: Double,
) {
    fun toLatLng() = LatLng.from(lat, lng)
    fun toPoint() = Point(lat = lat, lng = lng)

    companion object {
        val gpxPointsSample = listOf<GpxPoint>(
            GpxPoint(
                lat = 36.71069498173892,
                lng = 126.54820843599737,
                elevation = 35.498,
                segmentDistance = 0.0,
                totalDistance = 0.0
            ),
            GpxPoint(
                lat = 36.71066598035394,
                lng = 126.54808161780238,
                elevation = 35.603,
                segmentDistance = 11.755663453902102,
                totalDistance = 0.011755663453902101
            ),
            GpxPoint(
                lat = 36.71078776940703,
                lng = 126.54793107882142,
                elevation = 35.539,
                segmentDistance = 19.064866281270156,
                totalDistance = 0.030820529735172256
            ),
            GpxPoint(
                lat = 36.710855243727565,
                lng = 126.54816350899637,
                elevation = 35.0,
                segmentDistance = 22.035648140529318,
                totalDistance = 0.05285617787570157
            ),
            GpxPoint(
                lat = 36.71096546575428,
                lng = 126.54847850091755,
                elevation = 35.771,
                segmentDistance = 30.63692256747517,
                totalDistance = 0.08349310044317675
            ),
            GpxPoint(
                lat = 36.711097732186325,
                lng = 126.54884420335293,
                elevation = 36.992,
                segmentDistance = 35.763061424002835,
                totalDistance = 0.11925616186717958
            ),
            GpxPoint(
                lat = 36.71119473218633,
                lng = 126.54910530335293,
                elevation = 38.1465,
                segmentDistance = 25.65232042776978,
                totalDistance = 0.14490848229494935
            ),
            GpxPoint(
                lat = 36.71129185706376,
                lng = 126.54936656355858,
                elevation = 39.301,
                segmentDistance = 25.67109015188891,
                totalDistance = 0.17057957244683825
            ),
            GpxPoint(
                lat = 36.711499141529195,
                lng = 126.54985598288476,
                elevation = 39.397,
                segmentDistance = 49.34129755813654,
                totalDistance = 0.2199208700049748
            ),
            GpxPoint(
                lat = 36.71169091947375,
                lng = 126.550282118842,
                elevation = 39.914,
                segmentDistance = 43.56214028838595,
                totalDistance = 0.26348301029336074
            ),
        )
    }
}