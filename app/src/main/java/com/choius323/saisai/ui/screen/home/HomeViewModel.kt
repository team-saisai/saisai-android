package com.choius323.saisai.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.usecase.GetPopularChallengeUseCase
import com.choius323.saisai.usecase.GetRecentCourseUseCase
import com.choius323.saisai.usecase.GetUserInfoUseCase
import com.choius323.saisai.usecase.ToggleBookmarkCourseUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container


class HomeViewModel(
    private val getRecentCourseUseCase: GetRecentCourseUseCase,
    private val getPopularChallengeUseCase: GetPopularChallengeUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val toggleBookmarkCourseUseCase: ToggleBookmarkCourseUseCase,
    private val accountRepository: AccountRepository,
) : ViewModel(),
    ContainerHost<HomeUiState, HomeSideEffect> {

    override val container = container<HomeUiState, HomeSideEffect>(HomeUiState()) // Initial state

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.LoadData -> loadData(event.isForceLoad)
            is HomeUiEvent.OnClickBookmark -> toggleBookmark(event.courseId, event.isBookmarked)
            is HomeUiEvent.CourseClicked -> intent {
                postSideEffect(HomeSideEffect.GoToDetail(event.courseId))
            }

            HomeUiEvent.OnClickNotification -> intent {
                postSideEffect(HomeSideEffect.GoNotificationList)
            }
        }
    }

    private fun loadData(isForceLoad: Boolean) = intent {
        if ((state.isLoaded && isForceLoad.not()) || state.isLoading) return@intent
        reduce {
            state.copy(isLoading = true)
        }
        getUserName()
        gerUserBadgeList()
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

    private fun gerUserBadgeList() = intent {
        accountRepository.getUserBadgeList().collectLatest { result ->
            runCatching {
                val badgeList = result.getOrThrow()
                reduce {
                    state.copy(badges = badgeList)
                }
            }.onFailure { throwable ->
                Log.e(TAG, throwable.message, throwable)
                postSideEffect(
                    HomeSideEffect.ShowToast(throwable.message ?: "배지를 가져오지 못했습니다.")
                )
            }
        }
    }

    private fun getUserName() = intent {
        getUserInfoUseCase().collectLatest { result ->
            runCatching {
                val name = result.getOrThrow()
                reduce {
                    state.copy(name = name)
                }
            }.onFailure { exception ->
                Log.e(TAG, exception.toString())
                postSideEffect(
                    HomeSideEffect.ShowToast(exception.message ?: "Unknown error")
                )
            }
        }
    }

    private fun toggleBookmark(courseId: Long, isBookmarked: Boolean) = intent {
        reduce { state.copy(isLoading = true) }
        toggleBookmarkCourseUseCase(courseId, isBookmarked).collectLatest { result ->
            result.onSuccess { changedBookmark ->
                reduce {
                    state.copy(
                        popularChallenges = state.popularChallenges.map { course ->
                            if (course.courseId == courseId) {
                                course.copy(isBookmarked = changedBookmark)
                            } else {
                                course
                            }
                        },
                        isLoading = false
                    )
                }
            }.onFailure {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(HomeSideEffect.ShowToast(it.message ?: "북마크를 변경하지 못했습니다."))
            }
        }
    }
}

private const val TAG = "HomeViewModel"
