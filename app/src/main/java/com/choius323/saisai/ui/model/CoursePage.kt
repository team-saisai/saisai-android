package com.choius323.saisai.ui.model

data class CoursePage(
    val content: List<CourseListItem>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPageNumber: Int,
    val pageSize: Int,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val isEmpty: Boolean,
)