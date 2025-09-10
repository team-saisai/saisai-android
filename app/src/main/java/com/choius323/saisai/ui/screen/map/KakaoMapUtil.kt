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
import com.kakao.vectormap.label.PathOptions
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle


fun updateMapData(map: KakaoMap?, route: List<GpxPoint>) {
    val latLngList = route.map(GpxPoint::toLatLng)
    if (map == null) return
    map.drawRoute(latLngList)
    map.moveCamera(latLngList)
}

fun KakaoMap?.drawRoute(route: List<LatLng>, color: Int = SaiColor.Lime.toArgb()) {
    val layer = this?.routeLineManager?.getLayer(DEFAULT_LINE_LAYER_ID)
        ?: this?.routeLineManager?.addLayer(DEFAULT_LINE_LAYER_ID, Z_ORDER_DEFAULT_LINE)
        ?: return
    if (route.size < 2) return
    layer.removeAll()
    val style = RouteLineStyle.from(LINE_WIDTH, color)
    val segment = RouteLineSegment.from(route)
        .setStyles(style)
    layer.addRouteLine(RouteLineOptions.from(segment))
}

fun KakaoMap?.createEndPointsLabel(startPoint: LatLng, endPoint: LatLng) {
    val layer = this?.labelManager?.getLayer(END_POINTS_LAYER_ID)
        ?: this?.labelManager?.addLayer(LabelLayerOptions.from(END_POINTS_LAYER_ID))
        ?: return
    layer.zOrder = Z_ORDER_END_POINTS
    layer.addLabels(
        listOf(
            LabelOptions.from(START_LABEL, startPoint).setStyles(startStyle),
            LabelOptions.from(END_LABEL, endPoint).setStyles(endStyle)
        )
    )
    layer.allLabels.forEach {
        it.scaleTo(4f, 4f)
    }
}

fun KakaoMap?.drawRideRoute(route: List<LatLng>, startIndex: Int, endIndex: Int) {
    val layer = this?.routeLineManager?.getLayer(COLOR_LINE_LAYER_ID)
        ?: this?.routeLineManager?.addLayer(COLOR_LINE_LAYER_ID, Z_ORDER_COLOR_LINE)
        ?: return
    if (route.size < 2) return
    if (layer.getRouteLine("rideLine/${startIndex}/${endIndex}") != null) return
    val segment =
        RouteLineSegment.from(route.subList(startIndex, endIndex)).setStyles(rideLineStyle)
    val routeLine =
        layer.addRouteLine(RouteLineOptions.from("rideLine/${startIndex}/${endIndex}", segment))
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
    val label = layer.getLabel(DIRECTION_LABEL)
    if (label == null) {
        val newLabel = layer.addLabel(
            LabelOptions.from(DIRECTION_LABEL, latLng).setStyles(directionStyle)
        )
        newLabel.scaleTo(DIRECTION_LABEL_SCALE, DIRECTION_LABEL_SCALE)
    } else {
        label.moveOnPath(PathOptions.fromPath(label.position, latLng))
    }
}

fun KakaoMap?.initCircles(list: List<LatLng>, isColor: Boolean = true) {
    val layer = this?.labelManager?.addLayer(LabelLayerOptions.from(CIRCLE_LAYER_ID)) ?: return
    layer.zOrder = Z_ORDER_DEFAULT_CIRCLE
    val circleStyle = if (isColor) limeCircleStyle else grayCircleStyle
    list.forEachIndexed { index, latLng ->
        val label = layer.addLabel(LabelOptions.from(latLng).setStyles(circleStyle))
        label.scaleTo(CIRCLE_SCALE, CIRCLE_SCALE)
    }
}

fun KakaoMap?.setCirclesStyle(list: List<LatLng>, passedIndex: Int) {
    val layer = this?.labelManager?.getLayer(CIRCLE_LAYER_ID) ?: return
    layer.removeAll()
    layer.zOrder = Z_ORDER_COLOR_CIRCLE
    for ((index, latLng) in list.withIndex()) {
        val circleStyle = if (index <= passedIndex) limeCircleStyle else grayCircleStyle
        val label = layer.addLabel(LabelOptions.from(latLng).setStyles(circleStyle))
        label.scaleTo(CIRCLE_SCALE, CIRCLE_SCALE)
    }
}

private const val START_LABEL = "Start"
private const val END_LABEL = "End"
private const val DIRECTION_LABEL = "Direction"
private const val CIRCLE_ID = "Circle"
private const val CIRCLE_LAYER_ID = "CircleLayer"
private const val DEFAULT_LINE_LAYER_ID = "DefaultLineLayer"
private const val COLOR_LINE_LAYER_ID = "ColorLineLayer"
private const val END_POINTS_LAYER_ID = "EndPointsLayer"

private val startStyle: LabelStyle =
    LabelStyle.from(R.drawable.img_play_arrow).setAnchorPoint(0.45f, 0.45f)
private val endStyle: LabelStyle =
    LabelStyle.from(R.drawable.img_flag).setAnchorPoint(0.2f, 0.9f)
private val directionStyle: LabelStyles =
    LabelStyles.from(
        DIRECTION_LABEL, LabelStyle.from(R.drawable.ic_ride_bicycle).setAnchorPoint(0.5f, 0.5f)
    )
private val rideLineStyle = RouteLineStyle.from(LINE_WIDTH, SaiColor.Lime.toArgb())
private val grayCircleStyle = LabelStyle.from(R.drawable.ic_circle_gray).setAnchorPoint(0.5f, 0.5f)
private val limeCircleStyle = LabelStyle.from(R.drawable.ic_circle_lime).setAnchorPoint(0.5f, 0.5f)

private const val LINE_WIDTH = 10f
private const val CIRCLE_SCALE = 2.3f
private const val DIRECTION_LABEL_SCALE = 1.5f

private const val Z_ORDER_DEFAULT_LINE = 3300
private const val Z_ORDER_COLOR_LINE = 3400
private const val Z_ORDER_DEFAULT_CIRCLE = 3500
private const val Z_ORDER_COLOR_CIRCLE = 3600
private const val Z_ORDER_END_POINTS = 3700
