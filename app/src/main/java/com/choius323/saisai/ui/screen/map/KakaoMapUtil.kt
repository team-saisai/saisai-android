package com.choius323.saisai.ui.screen.map

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.choius323.saisai.ui.model.GpxPoint
import com.choius323.saisai.ui.model.Position
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.R as KakaoMapR

fun updateMapData(map: KakaoMap?, route: List<GpxPoint>) {
    val latLngList = route.map(GpxPoint::toLatLng)
    if (map == null) return
    map.drawLine(latLngList)
    map.moveCamera(latLngList)
}

fun KakaoMap?.drawLine(route: List<LatLng>) {
    val layer = this?.routeLineManager?.layer ?: return
    if (route.size < 2) return
    layer.removeAll()
    val style = RouteLineStyle.from(LINE_WIDTH, Color(201, 255, 102).toArgb())
    val segment = RouteLineSegment.from(route)
        .setStyles(style)
    layer.addRouteLine(RouteLineOptions.from(segment))
}

@JvmName("drawLineWithPositions")
fun KakaoMap?.drawLine(route: List<Position>) {
    drawLine(route.map(Position::toLatLng))
}

fun KakaoMap.moveCamera(route: List<LatLng>) {
    moveCamera(
        CameraUpdateFactory.fitMapPoints(
            route.toTypedArray(), 200
        )
    )
}

fun KakaoMap?.createLabel(start: LatLng, end: LatLng) {
    val layer = this?.labelManager?.layer ?: return
    layer.removeAll()
    layer.addLabels(
        listOf(
            LabelOptions.from(START_LABEL, start).setStyles(startStyle),
            LabelOptions.from(END_LABEL, end).setStyles(endStyle)
        ),
    )
}

private const val START_LABEL = "Start"
private const val END_LABEL = "End"

private val startStyle: LabelStyles =
    LabelStyles.from(START_LABEL, LabelStyle.from(KakaoMapR.style.LabelStyle))
private val endStyle: LabelStyles =
    LabelStyles.from(END_LABEL, LabelStyle.from(KakaoMapR.style.LabelStyle))

private const val LINE_WIDTH = 20f