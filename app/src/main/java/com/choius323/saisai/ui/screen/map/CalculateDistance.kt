package com.choius323.saisai.ui.screen.map

import android.location.Location
import android.util.Log
import com.choius323.saisai.ui.model.GpxPoint
import com.kakao.vectormap.LatLng

data class RideSnapshot(
    val totalDistance: Double,
    val projectedPoint: LatLng,
    val segmentIndex: Int,
)

/**
 * GMS로부터 받은 사용자 위치를 처리하는 메인 함수
 * @param [userLatLng] 사용자의 현재 [LatLng]
 */
fun updateUserLocation(
    userLatLng: LatLng,
    currentSegmentIndex: Int,
    route: List<GpxPoint>,
): RideSnapshot? {
    // 1. 최적화: 현재 구간 주변의 일부 구간만 탐색
    var minDistanceToCourse = Float.MAX_VALUE
    var bestSegmentIndex = -1
    var projectedPointOnCourse: LatLng? = null

    for (i in currentSegmentIndex..<route.lastIndex) {
        val segmentStart = route[i].toLatLng()
        val segmentEnd = route[i + 1].toLatLng()
        val (distance, projectedPoint) = findProjectionAndDistance(
            userLatLng, segmentStart, segmentEnd
        )

        if (distance < minDistanceToCourse) {
            minDistanceToCourse = distance
            bestSegmentIndex = i
            projectedPointOnCourse = projectedPoint
        }
        if (route[i + 1].totalDistance - route[currentSegmentIndex].totalDistance >= 25.0) {
            break
        }
    }
    if (bestSegmentIndex == route.lastIndex - 1 &&
        projectedPointOnCourse?.latitude == route.last().lat &&
        projectedPointOnCourse.longitude == route.last().lng
    ) {
        bestSegmentIndex = route.lastIndex
    }

    // 코스 이탈 여부 판단 (예: 25미터 이상 떨어지면 이탈로 간주)
    if (minDistanceToCourse > 25.0) {
        // 코스 이탈 처리 로직 (주행 중단, 알림 등)
        Log.d(
            TAG,
            "코스를 이탈했습니다! (user point: $userLatLng, distance: $minDistanceToCourse, index: $bestSegmentIndex, point: $projectedPointOnCourse)"
        )
        return null
    } else if (bestSegmentIndex != -1 && projectedPointOnCourse != null) {
        // 2. 가장 가까운 구간을 찾았으면, 상태 업데이트
        // 3. 총 이동 거리 계산 (totalDistance 필드 활용)
        // 현재 구간의 시작점까지의 누적 거리를 바로 가져온다.
        val distanceToSegmentStart = route[currentSegmentIndex].totalDistance

        // 현재 구간 내에서의 이동 거리를 계산
        val distanceOnCurrentSegment = calculateDistance(
            route[currentSegmentIndex].toLatLng(), projectedPointOnCourse
        )

        val totalDistanceTraveled = distanceToSegmentStart + distanceOnCurrentSegment

        Log.d(
            TAG,
            "총 이동 거리: ${totalDistanceTraveled}m, 현재 코스 위 위치: $projectedPointOnCourse, 현재 구간: $bestSegmentIndex"
        )
        return RideSnapshot(
            totalDistance = totalDistanceTraveled,
            projectedPoint = projectedPointOnCourse,
            segmentIndex = bestSegmentIndex
        )
    } else {
        return null
    }
}

/**
 * 두 지점([LatLng]) 사이의 거리를 미터(meter) 단위로 계산합니다.
 * Android의 [Location.distanceBetween] 유틸리티를 활용합니다.
 */
private fun calculateDistance(point1: LatLng, point2: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(
        point1.latitude, point1.longitude, point2.latitude, point2.longitude, results
    )
    return results[0]
}

/**
 * 한 점([point])에서 선분([segmentStart], [segmentEnd])까지의 최단 거리와,
 * 그 선분 위의 가장 가까운 지점(수선의 발, projected point)을 찾습니다.
 *
 * @return [Pair]<거리, 가장 가까운 지점의 [LatLng]>
 */
private fun findProjectionAndDistance(
    point: LatLng,
    segmentStart: LatLng,
    segmentEnd: LatLng,
): Pair<Float, LatLng> {

    // 선분의 벡터 (V)
    val vx = segmentEnd.longitude - segmentStart.longitude
    val vy = segmentEnd.latitude - segmentStart.latitude

    // 선분 시작점에서 사용자의 위치를 가리키는 벡터 (W)
    val wx = point.longitude - segmentStart.longitude
    val wy = point.latitude - segmentStart.latitude

    // 벡터 W를 V에 투영(projection)
    val dotProduct = wx * vx + wy * vy
    val segmentLengthSquared = vx * vx + vy * vy

    // 투영된 지점이 선분 위에 어느 지점에 위치하는지를 나타내는 비율 (0.0 ~ 1.0)
    val t = if (segmentLengthSquared == 0.0) {
        -1.0 // 선분의 길이가 0인 경우 (시작점=끝점)
    } else {
        dotProduct / segmentLengthSquared
    }
    val projectedPoint: LatLng = if (t < 0.0) {
        // 투영된 지점이 선분 시작점보다 '이전'이므로, 가장 가까운 점은 시작점
        segmentStart
    } else if (t > 1.0) {
        // 투영된 지점이 선분 끝점보다 '이후'이므로, 가장 가까운 점은 끝점
        segmentEnd
    } else {
        // 투영된 지점이 선분 '안'에 있으므로, 수선의 발을 계산
        LatLng.from(
            segmentStart.latitude + t * vy, segmentStart.longitude + t * vx
        )
    }

    // 사용자 위치와 코스 위의 가장 가까운 지점 사이의 거리 계산
    val distance = calculateDistance(point, projectedPoint)
    Log.d(
        TAG,
        "distance: $distance, point: $point, segmentStart: $segmentStart, segmentEnd: $segmentEnd, projectedPoint: $projectedPoint"
    )
    return Pair(distance, projectedPoint)
}

private const val TAG = "CalculateDistance"