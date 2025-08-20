package com.choius323.saisai.ui.screen.map

import androidx.compose.ui.graphics.toArgb
import com.choius323.saisai.R
import com.choius323.saisai.ui.model.GpxPoint
import com.choius323.saisai.ui.theme.SaiColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayerOptions
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

fun KakaoMap?.drawRoute(route: List<LatLng>, color: Int = SaiColor.Lime.toArgb()) {
    val layer = this?.routeLineManager?.layer ?: return
    if (route.size < 2) return
    layer.removeAll()
    val style = RouteLineStyle.from(LINE_WIDTH, color)
    val segment = RouteLineSegment.from(route)
        .setStyles(style)
    val routeLine = layer.addRouteLine(RouteLineOptions.from(segment))
    routeLine.zOrder = 3
}

fun KakaoMap?.drawRideRoute(route: List<LatLng>, startIndex: Int, endIndex: Int) {
    val layer = this?.routeLineManager?.layer ?: return
    if (route.size < 2) return
    if (layer.getRouteLine("rideLine/${startIndex}/${endIndex}") != null) return
    val segment =
        RouteLineSegment.from(route.subList(startIndex, endIndex)).setStyles(rideLineStyle)
    val routeLine =
        layer.addRouteLine(RouteLineOptions.from("rideLine/${startIndex}/${endIndex}", segment))
    routeLine.zOrder = 3
}

@JvmName("drawRideRouteWithGpxPoints")
fun KakaoMap?.drawRideRoute(route: List<GpxPoint>, startIndex: Int, endIndex: Int) {
    drawRideRoute(route.map(GpxPoint::toLatLng), startIndex, endIndex)
}

@JvmName("drawRouteWithGpxPoints")
fun KakaoMap?.drawRoute(route: List<GpxPoint>, color: Int = SaiColor.Lime.toArgb()) {
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

fun KakaoMap?.initCircles(list: List<LatLng>, isColor: Boolean = true) {
    println(list.joinToString("\n") { "${it.latitude}, ${it.longitude}"})
    val layer = this?.labelManager?.addLayer(LabelLayerOptions.from(CIRCLE_LAYER_ID)) ?: return
    val resource = if (isColor) R.drawable.ic_circle_lime else R.drawable.ic_circle_gray
    list.forEachIndexed { index, latLng ->
        val label = layer.addLabel(
            LabelOptions.from(latLng)
                .setStyles(LabelStyle.from(resource).setAnchorPoint(0.5f, 0.5f))
        )
        label.scaleTo(3f, 3f)
    }
}

fun KakaoMap?.setCirclesStyle(list: List<LatLng>, passedIndex: Int) {
    val layer = this?.labelManager?.getLayer(CIRCLE_LAYER_ID) ?: return
    layer.removeAll()
    for ((index, latLng) in list.withIndex()) {
        val label = layer.addLabel(
            LabelOptions.from(latLng)
                .setStyles(
                    LabelStyle.from(if (index > passedIndex) R.drawable.ic_circle_gray else R.drawable.ic_circle_lime)
                        .setAnchorPoint(0.5f, 0.5f)
                )
        )
        label.scaleTo(3f, 3f)
    }
}

private const val START_LABEL = "Start"
private const val END_LABEL = "End"
private const val DIRECTION_LABEL = "Direction"
private const val CIRCLE_ID = "Circle"
private const val CIRCLE_LAYER_ID = "CircleLayer"

private val startStyle: LabelStyles =
    LabelStyles.from(START_LABEL, LabelStyle.from(KakaoMapR.style.LabelStyle))
private val endStyle: LabelStyles =
    LabelStyles.from(END_LABEL, LabelStyle.from(KakaoMapR.style.LabelStyle))
private val directionStyle: LabelStyles =
    LabelStyles.from(
        DIRECTION_LABEL, LabelStyle.from(R.drawable.ic_ride_bicycle).setAnchorPoint(0.5f, 0.5f)
    )
private val rideLineStyle = RouteLineStyle.from(LINE_WIDTH, SaiColor.Lime.toArgb())
private val grayCircleStyle = LabelStyle.from(R.drawable.ic_circle_gray).setAnchorPoint(0.5f, 0.5f)
private val limeCircleStyle = LabelStyle.from(R.drawable.ic_circle_lime).setAnchorPoint(0.5f, 0.5f)

private const val LINE_WIDTH = 20f