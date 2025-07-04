package com.choius323.saisai.ui.screen.course

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
        fetchCourseList()
    }

    fun onEvent(event: CourseListUiEvent) = when (event) {
        is CourseListUiEvent.CourseClicked -> intent {
            postSideEffect(CourseListSideEffect.GoCourseDetail(event.courseId))
        }
    }

    private fun fetchCourseList() = intent {
        reduce { state.copy(isLoading = true) }
        getCourseListUseCase(page = 1).collectLatest { result ->
            result.onSuccess { courses ->
                reduce { state.copy(courseList = courses.content, isLoading = false) }
            }.onFailure {
                reduce { state.copy(isLoading = false, error = it.message ?: "Error") }
            }
        }
    }
}