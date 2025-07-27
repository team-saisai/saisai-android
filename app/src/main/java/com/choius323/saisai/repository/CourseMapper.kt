package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.model.ChallengeInfoDto
import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDto
import com.choius323.saisai.data.course.remote.model.GpxPointDto
import com.choius323.saisai.data.course.remote.model.PopularChallengeItemDto
import com.choius323.saisai.data.course.remote.model.RecentCourseDto
import com.choius323.saisai.data.course.remote.model.RewardInfoDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.ui.model.ChallengeInfo
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.CoursePage
import com.choius323.saisai.ui.model.GpxPoint
import com.choius323.saisai.ui.model.Level
import com.choius323.saisai.ui.model.PopularChallengeListItem
import com.choius323.saisai.ui.model.RecentCourse
import com.choius323.saisai.ui.model.RewardInfo
import com.choius323.saisai.util.DateTimeFormat
import org.threeten.bp.LocalDate


fun SaiResponseDto<CourseDataDto>.toCoursePage(): CoursePage {
    val content = data.content.map { courseItemDto ->
        courseItemDto.run {
            CourseListItem(
                courseId = courseId,
                courseName = courseName,
                level = Level.from(level),
                distance = distance,
                estimatedTime = estimatedTime,
                sigun = sigun,
                imageUrl = imageUrl,
                courseChallengerCount = courseChallengerCount,
                courseFinisherCount = courseFinisherCount,
                challengeStatus = challengeStatus,
                challengeEndedAt = LocalDate.parse(challengeEndedAt, DateTimeFormat.dateFormat),
                isEventActive = isEventActive,
                reward = reward,
            )
        }
    }

    return CoursePage(
        content = content,
        totalElements = data.totalElements,
        totalPages = data.totalPages,
        currentPageNumber = data.number,
        pageSize = data.size,
        isFirstPage = data.first,
        isLastPage = data.last,
        isEmpty = data.empty
    )
}

fun ChallengeInfoDto?.toChallengeInfo() = this?.run {
    ChallengeInfo(
        challengeStatus = challengeStatus,
        challengeEndedTime = LocalDate.parse(challengeEndedTime, DateTimeFormat.dateFormat)
    )
}

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
    )
}

fun GpxPointDto.toGpxPoint(): GpxPoint = GpxPoint(
    lat = latitude,
    lng = longitude,
    elevation = elevation,
    segmentDistance = segmentDistance,
    totalDistance = totalDistance,
)

fun RewardInfoDto.toRewardUiInfo(): RewardInfo {
    return RewardInfo(
        rewardName = rewardName,
        rewardImageUrl = rewardImageUrl,
        startedAt = startedAt,
        endedAt = endedAt
    )
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