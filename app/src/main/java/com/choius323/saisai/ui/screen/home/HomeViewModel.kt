package com.choius323.saisai.ui.screen.home

import androidx.lifecycle.ViewModel
import com.choius323.saisai.usecase.GetPopularChallengeUseCase
import com.choius323.saisai.usecase.GetRecentCourseUseCase
import kotlinx.coroutines.flow.combine
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container


class HomeViewModel(
    private val getRecentCourseUseCase: GetRecentCourseUseCase,
    private val getPopularChallengeUseCase: GetPopularChallengeUseCase,
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
        // getCourseDetailUseCase("남파랑길 1코스").collectLatest {
        //     Log.d(TAG, it.toString())
        // }
        reduce {
            state.copy(isLoading = true)
        }
        getRecentCourseUseCase().combine(getPopularChallengeUseCase()) { item1, item2 ->
            item1 to item2
        }.collect { (recentResult, popularResult) ->
            val exception = recentResult.exceptionOrNull() ?: popularResult.exceptionOrNull()
            if (exception != null) {
                reduce {
                    state.copy(
                        isLoading = false, errorMessage = exception.message ?: "Unknown error"
                    )
                }
            } else {
                val recentData = recentResult.getOrThrow()
                val popularData = popularResult.getOrThrow() // popularData가 PopularCourse 타입이라고 가정

                state.copy(
                    isLoading = false,
                    recentChallenge = recentData,
                    popularChallenges = popularData,
                    errorMessage = null,
                    isLoaded = true
                )
            }
            recentResult.fold(onSuccess = { courseInfo ->
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
}

private const val TAG = "HomeViewModel"
