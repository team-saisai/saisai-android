package com.choius323.saisai.ui.screen.home

import com.choius323.saisai.ui.model.BadgeInfo
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.PopularChallengeListItem

data class HomeUiState(
    val name: String? = null,
    val location: String = "",
    val temperature: String = "",
    val recentChallenge: CourseListItem? = null,
    val popularChallenges: List<PopularChallengeListItem> = emptyList(),
    val aroundChallenges: List<CourseListItem> = emptyList(),
    val badges: List<BadgeInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoaded: Boolean = false,
)

sealed interface HomeUiEvent {
    data class LoadData(val isForceLoad: Boolean = false) : HomeUiEvent
    data class CourseClicked(val courseId: Int) : HomeUiEvent
}

sealed interface HomeSideEffect {
    data class GoToDetail(val courseId: Int) : HomeSideEffect
}