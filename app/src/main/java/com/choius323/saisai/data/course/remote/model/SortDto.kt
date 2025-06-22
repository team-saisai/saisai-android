package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SortDto(
    val empty: Boolean = true,
    val sorted: Boolean = false,
    val unsorted: Boolean = false,
)