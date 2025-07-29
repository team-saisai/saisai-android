package com.choius323.saisai.ui.screen.course_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.choius323.saisai.ui.navigation.MainNavItem
import com.choius323.saisai.usecase.GetCourseDetailUseCase
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class CourseDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
) : ViewModel(),
    ContainerHost<CourseDetailUiState, CourseDetailSideEffect> {
    override val container: Container<CourseDetailUiState, CourseDetailSideEffect> =
        container(CourseDetailUiState())

    val courseId = savedStateHandle.toRoute<MainNavItem.CourseDetail>().courseId

    init {
        intent {
            reduce { state.copy(isLoading = true) }
            getCourseDetailUseCase(courseId).collect { result ->
                result.onSuccess { courseDetail ->
                    reduce { state.copy(courseDetail = courseDetail, isLoading = false) }
                }.onFailure {
                    reduce { state.copy(error = it.message, isLoading = false) }
                }
            }
        }
    }

    fun onEvent(event: CourseDetailUiEvent) = when (event) {
        is CourseDetailUiEvent.CourseStartClicked -> intent {
            postSideEffect(CourseDetailSideEffect.StartCourse(courseId))
        }
        is CourseDetailUiEvent.BackClicked -> intent {
            postSideEffect(CourseDetailSideEffect.GoBack)
        }
    }
}