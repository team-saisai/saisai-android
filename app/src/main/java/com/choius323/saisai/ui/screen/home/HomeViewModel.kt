package com.choius323.saisai.ui.screen.home

import androidx.lifecycle.ViewModel
import com.choius323.saisai.usecase.GetAllCoursesUseCase
import com.choius323.saisai.usecase.GetRecentCourseUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container


class HomeViewModel(
    private val getRecentCourseUseCase: GetRecentCourseUseCase,
    private val getAllCourseUseCase: GetAllCoursesUseCase,
) : ViewModel(),
    ContainerHost<HomeUiState, HomeSideEffect> {

    override val container = container<HomeUiState, HomeSideEffect>(HomeUiState()) // Initial state

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.LoadData -> loadData(event.isForceLoad)
            is HomeUiEvent.CourseClicked -> intent {
                postSideEffect(HomeSideEffect.GoToDetail(event.courseId))
            }
        }
    }

    private fun loadData(isForceLoad: Boolean) = intent {
        if ((state.isLoaded && isForceLoad.not()) || state.isLoading) return@intent
        reduce {
            state.copy(isLoading = true)
        }
        getRecentCourseUseCase().fold(onSuccess = { courseInfo ->
            reduce {
                state.copy(isLoading = false, recentChallenge = courseInfo, isLoaded = true)
            }
        }, onFailure = { exception ->
            reduce {
                state.copy(
                    isLoading = false, errorMessage = exception.message ?: "Unknown error"
                )
            }
        })
    }
}

private const val TAG = "HomeViewModel"
