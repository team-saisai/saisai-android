package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.model.ChallengeInfoDto
import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.CourseDetailDataDto
import com.choius323.saisai.data.course.remote.model.PopularChallengeItemDto
import com.choius323.saisai.data.course.remote.model.RewardInfoDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.ui.model.ChallengeInfo
import com.choius323.saisai.ui.model.CourseDetailInfo
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.CoursePage
import com.choius323.saisai.ui.model.PopularChallengeListItem
import com.choius323.saisai.ui.model.RewardInfo
import org.threeten.bp.LocalDateTime


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

fun CourseDetailDataDto.toCourseDetailInfo(): CourseDetailInfo {
    return CourseDetailInfo(
        courseId = courseId,
        courseName = courseName,
        contents = contents,
        level = level,
        distanceInKm = distance,
        estimatedTimeInHours = estimatedTime,
        sigun = sigun,
        tourInfo = tourInfo,
        travelerInfo = travelerInfo,
        gpxPath = gpxpath,
        completeUserCount = completeUserCount,
        rewardInfo = rewardInfo?.toRewardUiInfo()
    )
}

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