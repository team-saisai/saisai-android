package com.choius323.saisai.ui.screen.bookmark_courses

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class BookmarkCoursesViewModel(
    // TODO: UseCases, Repository 등 주입
) : ViewModel(), ContainerHost<BookmarkCoursesUiState, BookmarkCoursesSideEffect> {

    override val container: Container<BookmarkCoursesUiState, BookmarkCoursesSideEffect> =
        container(BookmarkCoursesUiState())

    fun onEvent(event: BookmarkCoursesUiEvent) = when (event) {
        else -> Unit
    }

    private fun showToast(message: String) = intent {
        postSideEffect(BookmarkCoursesSideEffect.ShowToast(message))
    }
}