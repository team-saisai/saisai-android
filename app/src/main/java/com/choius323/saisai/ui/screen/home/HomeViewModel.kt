package com.choius323.saisai.ui.screen.home

import android.util.Log
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
        reduce {
            state.copy(isLoading = true)
        }
        getRecentCourseUseCase().combine(getPopularChallengeUseCase()) { item1, item2 ->
            item1 to item2
        }.collect { (recentResult, popularResult) ->
            runCatching {
                val recentData = recentResult.getOrThrow()
                val popularData = popularResult.getOrThrow()
                reduce {
                    state.copy(
                        isLoading = false,
                        recentCourse = recentData,
                        popularChallenges = popularData,
                        isLoaded = true
                    )
                }
            }.onFailure { exception ->
                Log.d(TAG, exception.toString())
                reduce {
                    state.copy(
                        isLoading = false, errorMessage = exception.message ?: "Unknown error"
                    )
                }

            }

        }
    }
}

private const val TAG = "HomeViewModel"
