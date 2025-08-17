package com.choius323.saisai.data.course.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RideHistoryDataDto(
    val totalElements: Long,
    val totalPages: Int,
    val size: Int,
    val content: List<RideHistoryItemDto>,
    val number: Int,
    val sort: SortDto,
    val numberOfElements: Int,
    val pageable: PageableDto,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
)
