package com.choius323.saisai.ui.screen.bookmark_courses

import android.util.Log
import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.CourseRepository
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class BookmarkCoursesViewModel(
    private val courseRepository: CourseRepository,
) : ViewModel(), ContainerHost<BookmarkCoursesUiState, BookmarkCoursesSideEffect> {

    override val container: Container<BookmarkCoursesUiState, BookmarkCoursesSideEffect> =
        container(BookmarkCoursesUiState())

    init {
        fetchCourses(false)
    }

    fun onEvent(event: BookmarkCoursesUiEvent) = when (event) {
        is BookmarkCoursesUiEvent.OnClickEdit -> enterEditMode()
        is BookmarkCoursesUiEvent.OnClickCancel -> cancelEditMode()
        is BookmarkCoursesUiEvent.OnClickDeleteAll -> showDeleteDialog()
        is BookmarkCoursesUiEvent.OnClickDialogConfirm -> deleteSelectedCourses()
        is BookmarkCoursesUiEvent.OnClickDialogDismiss -> dismissDeleteDialog()
        is BookmarkCoursesUiEvent.OnClickBack -> goBack()
        is BookmarkCoursesUiEvent.LoadMore -> fetchCourses(true)
        is BookmarkCoursesUiEvent.OnClickDeleteItem -> toggleItemSelection(event.index)
        is BookmarkCoursesUiEvent.OnClickCourse -> intent {
            postSideEffect(BookmarkCoursesSideEffect.GoCourseDetail(state.courseList[event.index].courseId))
        }

        is BookmarkCoursesUiEvent.OnClickDeleteSelected -> intent {
            reduce {
                state.copy(
                    deletedIndexList = state.deletedIndexList + state.selectedIndices,
                    selectedIndices = emptyList()
                )
            }
        }

        is BookmarkCoursesUiEvent.OnClickConfirm -> intent {
            if (state.deletedIndexList.isNotEmpty()) showDeleteDialog()
            else cancelEditMode()
        }

        is BookmarkCoursesUiEvent.OnClickBookmark -> deleteCourses(listOf(event.index))
    }

    private fun fetchCourses(isLoadMore: Boolean = false, nextPageParam: Int? = null) = intent {
        Log.d(TAG, "fetchCourseList isLoadMore: $isLoadMore")
        if ((nextPageParam != null && state.isLoading) || state.isLoadingMore || state.isLastPage) return@intent

        val nextPage: Int
        if (isLoadMore) {
            nextPage = nextPageParam ?: (state.page + 1)
            reduce { state.copy(isLoadingMore = true) }
        } else {
            nextPage = 1
            reduce { state.copy(isLoading = true) }
        }

        courseRepository.getBookmarkedCourses(page = nextPage).collectLatest { result ->
            result.onSuccess { coursePage ->
                reduce {
                    state.copy(
                        courseList = if (isLoadMore) state.courseList.take((nextPage - 1) * 10) + coursePage.content else coursePage.content,
                        isLoading = false,
                        isLoadingMore = false,
                        isLastPage = coursePage.isLastPage,
                        page = nextPage
                    )
                }
            }.onFailure {
                reduce {
                    state.copy(
                        isLoading = false,
                        isLoadingMore = false,
                    )
                }
                postSideEffect(BookmarkCoursesSideEffect.ShowToast(it.message ?: "목록을 불러오지 못했습니다."))
            }
        }
    }

    private fun goBack() = intent {
        postSideEffect(BookmarkCoursesSideEffect.GoBack)
    }

    private fun toggleItemSelection(index: Int) = intent {
        val selected = state.selectedIndices.toMutableList()
        if (selected.contains(index)) {
            selected.remove(index)
        } else {
            selected.add(index)
        }
        reduce { state.copy(selectedIndices = selected) }
    }

    private fun enterEditMode() = intent {
        reduce {
            state.copy(
                editMode = true,
                selectedIndices = emptyList(),
                deletedIndexList = emptyList(),
            )
        }
    }

    private fun cancelEditMode() = intent {
        reduce {
            state.copy(
                editMode = false,
                selectedIndices = emptyList(),
                deletedIndexList = emptyList(),
            )
        }
    }

    private fun deleteSelectedCourses() = intent {
        val indexList = state.deletedIndexList.ifEmpty { state.courseList.indices.toList() }
        deleteCourses(indexList)
    }

    private fun deleteCourses(indexList: List<Int>) = intent {
        val firstIndex = indexList.min()
        val selectedIds = indexList.map { state.courseList[it].courseId }

        reduce { state.copy(isLoading = true) }
        courseRepository.deleteBookmarkedCourses(selectedIds).collectLatest { result ->
            result.onSuccess {
                reduce {
                    state.copy(
                        isLoading = false,
                        editMode = false,
                        showDeleteDialog = false,
                        isLastPage = false,
                        selectedIndices = emptyList(),
                        courseList = state.courseList.take((firstIndex / 10) * 10)
                    )
                }
                showToast("${selectedIds.size}개의 코스가 삭제되었습니다.")
                fetchCourses(nextPageParam = firstIndex / 10)
            }.onFailure { throwable ->
                reduce {
                    state.copy(isLoading = false)
                }
                showToast(throwable.message ?: "코스를 삭제하지 못했습니다.")
            }
        }
    }

    private fun showDeleteDialog() = intent {
        reduce { state.copy(showDeleteDialog = true) }
    }

    private fun dismissDeleteDialog() = intent {
        reduce { state.copy(showDeleteDialog = false) }
    }

    private fun showToast(message: String) = intent {
        postSideEffect(BookmarkCoursesSideEffect.ShowToast(message))
    }
}

private const val TAG = "BookmarkCoursesViewModel"