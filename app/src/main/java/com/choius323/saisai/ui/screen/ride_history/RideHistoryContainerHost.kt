package com.choius323.saisai.ui.screen.ride_history

import com.choius323.saisai.ui.model.CourseSort
import com.choius323.saisai.ui.model.RideHistoryItem

data class RideHistoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val editMode: Boolean = false,
    val courseList: List<RideHistoryItem> = emptyList(),
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val page: Int = 1,
    val selectedIndices: List<Int> = emptyList(),
    val showDeleteDialog: Boolean = false,
    val deletedIndexList: List<Int> = emptyList(),
    val isRidingOnly: Boolean = false,
    val sort: CourseSort = CourseSort.newest
)

sealed interface RideHistoryUiEvent {
    data object OnClickBack : RideHistoryUiEvent
    data class OnClickCourse(val index: Int) : RideHistoryUiEvent
    data class OnClickDeleteItem(val index: Int) : RideHistoryUiEvent
    data object OnClickEdit : RideHistoryUiEvent
    data object OnClickCancel : RideHistoryUiEvent
    data object OnClickConfirm : RideHistoryUiEvent
    data object OnClickDialogConfirm : RideHistoryUiEvent
    data object OnClickDialogDismiss : RideHistoryUiEvent
    data object OnClickDeleteAll : RideHistoryUiEvent
    data object LoadMore : RideHistoryUiEvent
    data object OnClickDeleteSelected : RideHistoryUiEvent
    data class OnClickRideHistory(val index: Int) : RideHistoryUiEvent
    data object OnClickEmptyButton : RideHistoryUiEvent
    data class OnClickShowRidingOnly(val isRidingOnly: Boolean) : RideHistoryUiEvent
    data class OnSelectedCourseSort(val sort: CourseSort) : RideHistoryUiEvent
}

sealed interface RideHistorySideEffect {
    data class ShowToast(val message: String) : RideHistorySideEffect
    data object GoBack : RideHistorySideEffect
    data class GoCourseDetail(val courseId: Long) : RideHistorySideEffect
    data object GoCourseList : RideHistorySideEffect
}