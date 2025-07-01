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
import com.choius323.saisai.ui.model.PopularChallengeListItem
import com.choius323.saisai.ui.model.RecentCourse
import com.choius323.saisai.ui.model.RewardInfo
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


fun SaiResponseDto<CourseDataDto>.toCoursePage(): CoursePage {
    val content = data.content.map { courseItemDto ->
        CourseListItem(
            courseId = courseItemDto.courseId,
            courseName = courseItemDto.courseName,
            summary = courseItemDto.summary,
            level = courseItemDto.level,
            distance = courseItemDto.distance,
            estimatedTime = courseItemDto.estimatedTime,
            sigun = courseItemDto.sigun,
            imageUrl = courseItemDto.imageUrl,
            challengeInfo = courseItemDto.challengeInfo.toChallengeInfo(),
        )
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
        challengeEndedTime = LocalDateTime.parse(challengeEndedTime)
    )
}

fun CourseDetailDto.toCourseDetail(): CourseDetail {
    return CourseDetail(
        courseId = courseId,
        courseName = courseName,
        summary = summary,
        level = level,
        distance = distance,
        estimatedTime = estimatedTime,
        sigun = sigun,
        imageUrl = imageUrl,
        inProgressUserCount = inProgressUserCount,
        completeUserCount = completeUserCount,
        gpxPointList = gpxPointDtoList.map(GpxPointDto::toGpxPoint),
    )
}

fun GpxPointDto.toGpxPoint(): GpxPoint = GpxPoint(
    lat = latitude,
    lng = longitude,
    elevation = elevation,
    segmentDistance = segmentDistance,
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
        endedAt = LocalDateTime.parse(endedAt),
        estimatedTime = estimatedTime,
        level = level,
        participantCount = participantCount,
        sigun = sigun,
        imageUrl = imageUrl,
    )
}

fun RecentCourseDto.toRecentCourse(): RecentCourse? {
    return if (courseName == null || distance == null || sigun == null || progressRate == null || recentRideAt == null) {
        null
    } else {
        RecentCourse(
            courseName = courseName,
            distance = distance,
            sigun = sigun,
            progressRate = progressRate,
            recentDateAt = LocalDateTime.parse(
                recentRideAt,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            ),
            imageUrl = courseImageUrl
        )
    }
}