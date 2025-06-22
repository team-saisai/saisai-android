package com.choius323.saisai.ui.screen.map

import com.choius323.saisai.ui.model.Position
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.R
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineSegment

fun updateMapData(map: KakaoMap?, route: List<Position>) {
    val latLngList = route.map(Position::toLatLng)
    if (map == null) return
    map.drawLine(latLngList)
    map.moveCamera(latLngList)
    map.createLabel(latLngList.first(), latLngList.last())
}

fun KakaoMap.drawLine(route: List<LatLng>) {
    val layer = this.routeLineManager?.layer ?: return
    layer.removeAll()
    // val style = RouteLineStyle.from(LINE_WIDTH, route.traffic.argb)
    val segment = RouteLineSegment.from(route)
    // .setStyles(style)
    // layer.addRouteLine(RouteLineOptions.from(segment))
}

fun KakaoMap.moveCamera(route: List<LatLng>) {
    moveCamera(
        CameraUpdateFactory.fitMapPoints(
            route.toTypedArray(), 200
        )
    )
}

fun KakaoMap.createLabel(start: LatLng, end: LatLng) {
    val layer = this.labelManager?.layer ?: return
    layer.removeAll()
    // layer.addLabels(
    //     listOf(
    //         LabelOptions.from(START_LABEL, start).setStyles(startStyle),
    //         LabelOptions.from(END_LABEL, end).setStyles(endStyle)
    //     ),
    // )
}

private const val START_LABEL = "Start"
private const val END_LABEL = "End"

private val startStyle: LabelStyles =
    LabelStyles.from(START_LABEL, LabelStyle.from(R.style.LabelStyle))
private val endStyle: LabelStyles =
    LabelStyles.from(END_LABEL, LabelStyle.from(R.style.LabelStyle))

private const val LINE_WIDTH = 20f