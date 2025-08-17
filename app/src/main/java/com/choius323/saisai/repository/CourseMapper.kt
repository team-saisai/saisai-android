package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDto
import com.choius323.saisai.data.course.remote.model.CourseItemDto
import com.choius323.saisai.data.course.remote.model.GpxPointDto
import com.choius323.saisai.data.course.remote.model.PointDto
import com.choius323.saisai.data.course.remote.model.PopularChallengeItemDto
import com.choius323.saisai.data.course.remote.model.RecentCourseDto
import com.choius323.saisai.data.course.remote.model.ResumeRideDto
import com.choius323.saisai.data.course.remote.model.RideHistoryDataDto
import com.choius323.saisai.data.course.remote.model.RideHistoryItemDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.ui.model.CheckPoint
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.CoursePage
import com.choius323.saisai.ui.model.GpxPoint
import com.choius323.saisai.ui.model.Level
import com.choius323.saisai.ui.model.PopularChallengeListItem
import com.choius323.saisai.ui.model.RecentCourse
import com.choius323.saisai.ui.model.ResumeRideInfo
import com.choius323.saisai.ui.model.RideHistoryItem
import com.choius323.saisai.util.DateTimeFormat
import org.threeten.bp.LocalDate


@JvmName("CourseDataDtoToCoursePage")
fun SaiResponseDto<CourseDataDto>.toCoursePage() = CoursePage(
    content = data.content.map(CourseItemDto::toCourseListItem),
    totalElements = data.totalElements,
    totalPages = data.totalPages,
    currentPageNumber = data.number,
    pageSize = data.size,
    isFirstPage = data.first,
    isLastPage = data.last,
    isEmpty = data.empty
)

fun CourseItemDto.toCourseListItem() = CourseListItem(
    courseId = courseId,
    courseName = courseName,
    level = Level.from(level),
    distance = distance,
    estimatedTime = estimatedTime,
    sigun = sigun,
    imageUrl = imageUrl,
    participantsCount = participantsCount,
    challengeStatus = challengeStatus,
    challengeEndedAt = LocalDate.parse(challengeEndedAt, DateTimeFormat.dateFormat),
    isEventActive = isEventActive,
    reward = reward,
    isBookmarked = isBookmarked,
)

@JvmName("RideHistoryDataDtoToCoursePage")
fun SaiResponseDto<RideHistoryDataDto>.toCoursePage() = CoursePage(
    content = data.content.map(RideHistoryItemDto::toRideHistoryItem),
    totalElements = data.totalElements,
    totalPages = data.totalPages,
    currentPageNumber = data.number,
    pageSize = data.size,
    isFirstPage = data.first,
    isLastPage = data.last,
    isEmpty = data.empty
)

fun RideHistoryItemDto.toRideHistoryItem() = RideHistoryItem(
    courseId = courseId,
    courseName = courseName,
    level = Level.from(level),
    distance = distance,
    estimatedTime = estimatedTime,
    sigun = sigun,
    imageUrl = imageUrl,
    challengeStatus = challengeStatus,
    challengeEndedAt = LocalDate.parse(challengeEndedAt, DateTimeFormat.dateTimeFormat),
    isEventActive = isEventActive,
    rideId = rideId,
    lastRideDate = LocalDate.parse(lastRideDate, DateTimeFormat.dateTimeFormat),
    progressRate = 0,
    isCompleted = false,
)

fun CourseDetailDto.toCourseDetail(): CourseDetail {
    return CourseDetail(
        courseId = courseId,
        courseName = courseName,
        summary = summary,
        level = Level.from(level),
        distance = distance,
        estimatedTime = estimatedTime,
        sigun = sigun,
        imageUrl = imageUrl,
        inProgressUserCount = challengerCount,
        completeUserCount = finisherCount,
        gpxPointList = gpxPointDtoList.map(GpxPointDto::toGpxPoint),
        rideId = rideId,
        challengeStatus = challengeStatus,
        challengeEndedAt = LocalDate.parse(challengeEndedAt, DateTimeFormat.dateFormat),
        isEventActive = isEventActive,
        checkPointList = checkpointList.toCheckPointList(gpxPointDtoList)
    )
}

fun GpxPointDto.toGpxPoint(): GpxPoint = GpxPoint(
    lat = latitude,
    lng = longitude,
    elevation = elevation,
    segmentDistance = segmentDistance,
    totalDistance = totalDistance,
)

fun List<PointDto>.toCheckPointList(gpxPointDtoList: List<GpxPointDto>): List<CheckPoint> {
    val gpxPointMap = gpxPointDtoList.withIndex().associate { (index, gpxPoint) ->
        PointDto(gpxPoint.latitude, gpxPoint.longitude) to index
    }

    val newCheckPointList = this.mapNotNull { checkpoint ->
        val gpxIndex = gpxPointMap[checkpoint]

        if (gpxIndex != null && gpxPointDtoList[gpxIndex].elevation == null) {
            CheckPoint(
                lat = checkpoint.latitude,
                lng = checkpoint.longitude,
                gpxPointIdx = gpxIndex
            )
        } else {
            null
        }
    }
    println(newCheckPointList)
    return newCheckPointList
}


fun PopularChallengeItemDto.toPopularChallengeListItem(): PopularChallengeListItem {
    return PopularChallengeListItem(
        courseName = courseName,
        challengeStatus = challengeStatus,
        distance = distance,
        endedAt = LocalDate.parse(endedAt, DateTimeFormat.dateFormat),
        estimatedTime = estimatedTime,
        level = Level.from(level),
        participantCount = challengerCount,
        sigun = sigun,
        imageUrl = imageUrl,
        courseId = courseId,
        isEventActive = isEventActive,
        reward = reward,
    )
}

fun RecentCourseDto?.toRecentCourse(): RecentCourse? {
    return if (this == null || courseName == null || distance == null || sigun == null || progressRate == null || recentRideAt == null || courseId == null) {
        null
    } else {
        RecentCourse(
            courseName = courseName,
            distance = distance,
            sigun = sigun,
            progressRate = progressRate,
            recentDateAt = LocalDate.parse(
                recentRideAt, DateTimeFormat.dateFormat
            ),
            imageUrl = courseImageUrl,
            courseId = courseId
        )
    }
}

fun ResumeRideDto.toResumeRideInfo() = ResumeRideInfo(
    rideId = rideId, duration = durationSecond, checkpointIdx = checkpointIdx
)