package com.choius323.saisai.ui.screen.map

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.choius323.saisai.R
import com.choius323.saisai.ui.model.GpxPoint
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraAnimation
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
    map.drawRoute(latLngList)
    map.moveCamera(latLngList)
}

fun KakaoMap?.drawRoute(route: List<LatLng>, color: Int = Color(0xFFC9FF66).toArgb()) {
    val layer = this?.routeLineManager?.layer ?: return
    if (route.size < 2) return
    layer.removeAll()
    val style = RouteLineStyle.from(LINE_WIDTH, color)
    val segment = RouteLineSegment.from(route)
        .setStyles(style)
    layer.addRouteLine(RouteLineOptions.from(segment))
}

fun KakaoMap?.drawRideRoute(route: List<LatLng>, startIndex: Int, endIndex: Int) {
    val layer = this?.routeLineManager?.layer ?: return
    if (route.size < 2) return
    if (layer.getRouteLine("rideLine/${startIndex}/${endIndex}") != null) return
    val segment =
        RouteLineSegment.from(route.subList(startIndex, endIndex)).setStyles(rideLineStyle)
    layer.addRouteLine(RouteLineOptions.from("rideLine/${startIndex}/${endIndex}", segment))
}

@JvmName("drawRideRouteWithGpxPoints")
fun KakaoMap?.drawRideRoute(route: List<GpxPoint>, startIndex: Int, endIndex: Int) {
    drawRideRoute(route.map(GpxPoint::toLatLng), startIndex, endIndex)
}

@JvmName("drawRouteWithGpxPoints")
fun KakaoMap?.drawRoute(route: List<GpxPoint>, color: Int = Color(0xFFC9FF66).toArgb()) {
    drawRoute(route.map(GpxPoint::toLatLng), color)
}

fun KakaoMap?.moveCamera(route: List<LatLng>) {
    if (this == null || route.isEmpty()) return
    moveCamera(
        CameraUpdateFactory.fitMapPoints(
            route.toTypedArray(), 200
        )
    )
}

fun KakaoMap?.moveCamera(latLng: LatLng, zoomLevel: Int = 17) {
    if (this == null) return
    moveCamera(CameraUpdateFactory.newCenterPosition(latLng, zoomLevel), CameraAnimation.from(500))
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

fun KakaoMap?.createDirectionLabel(latLng: LatLng) {
    val layer = this?.labelManager?.layer ?: return
    val label = layer.getLabel(DIRECTION_LABEL) ?: layer.addLabel(
        LabelOptions.from(DIRECTION_LABEL, latLng).setStyles(directionStyle)
    )
    label.moveTo(latLng)
}

private const val START_LABEL = "Start"
private const val END_LABEL = "End"
private const val DIRECTION_LABEL = "Direction"

private val startStyle: LabelStyles =
    LabelStyles.from(START_LABEL, LabelStyle.from(KakaoMapR.style.LabelStyle))
private val endStyle: LabelStyles =
    LabelStyles.from(END_LABEL, LabelStyle.from(KakaoMapR.style.LabelStyle))
private val directionStyle: LabelStyles =
    LabelStyles.from(DIRECTION_LABEL, LabelStyle.from(R.drawable.ic_direction_label))
private val rideLineStyle = RouteLineStyle.from(LINE_WIDTH, Color(0xFFC9FF66).toArgb())

private const val LINE_WIDTH = 20f