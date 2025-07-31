package com.choius323.saisai.ui.screen.course_list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.choius323.saisai.usecase.GetAllCoursesUseCase
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class CourseListViewModel(
    private val getCourseListUseCase: GetAllCoursesUseCase,
) : ViewModel(), ContainerHost<CourseListUiState, CourseListSideEffect> {
    override val container: Container<CourseListUiState, CourseListSideEffect> =
        container(CourseListUiState())

    init {
        fetchCourseList(isLoadMore = false)
    }

    fun onEvent(event: CourseListUiEvent) = when (event) {
        is CourseListUiEvent.CourseClicked -> intent {
            postSideEffect(CourseListSideEffect.GoCourseDetail(event.courseId))
        }

        is CourseListUiEvent.OnClickCourseType -> intent {
            reduce { state.copy(selectedCourseType = event.courseType) }
        }

        is CourseListUiEvent.OnClickSortType -> intent {
            reduce { state.copy(selectedSort = event.sort) }
        }

        is CourseListUiEvent.LoadMore -> fetchCourseList(true)
    }

    private fun fetchCourseList(isLoadMore: Boolean = false) = intent {
        Log.d(TAG, "fetchCourseList isLoadMore: $isLoadMore")
        if (state.isLoading || state.isLoadingMore || state.isLastPage) return@intent

        val nextPage: Int
        if (isLoadMore) {
            nextPage = state.page + 1
            reduce { state.copy(isLoadingMore = true) }
        } else {
            nextPage = 1
            reduce { state.copy(isLoading = true) }
        }

        reduce { state.copy(isLoading = true) }
        getCourseListUseCase(page = nextPage).collectLatest { result ->
            result.onSuccess { coursePage ->
                reduce {
                    state.copy(
                        courseList = if (isLoadMore) state.courseList + coursePage.content else coursePage.content,
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
                postSideEffect(CourseListSideEffect.ShowToast(it.message ?: "목록을 불러오지 못했습니다."))
            }
        }
    }
}

private const val TAG = "CourseListViewModel"