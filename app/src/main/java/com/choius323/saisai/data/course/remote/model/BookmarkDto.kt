package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkDto(
    val isCourseBookmarked: Boolean,
)