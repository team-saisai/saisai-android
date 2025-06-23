package com.choius323.saisai.repository

import com.choius323.saisai.data.course.remote.model.ChallengeInfoDto
import com.choius323.saisai.data.course.remote.model.CourseDataDto
import com.choius323.saisai.data.course.remote.model.SaiResponseDto
import com.choius323.saisai.ui.model.ChallengeInfo
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.CoursePage


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
        challengeStatus = challengeStatus, challengeEndedTime = challengeEndedTime
    )
}