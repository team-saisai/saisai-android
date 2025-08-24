package com.choius323.saisai.ui.screen.course_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choius323.saisai.repository.CourseRepository
import com.choius323.saisai.ui.navigation.MainNavItem
import com.choius323.saisai.usecase.GetCourseDetailUseCase
import com.choius323.saisai.usecase.ShouldShowCourseCautionUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.threeten.bp.LocalDate

class CourseDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
    private val shouldShowCourseCautionUseCase: ShouldShowCourseCautionUseCase,
    private val courseRepository: CourseRepository,
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
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(
                        CourseDetailSideEffect.ShowToast(it.message ?: "코스 정보를 불러오는데 실패했습니다.")
                    )
                }
            }
        }
    }

    fun onEvent(event: CourseDetailUiEvent) = when (event) {
        is CourseDetailUiEvent.CourseStartClicked -> courseStartClicked()
        CourseDetailUiEvent.BackClicked -> backClicked()
        is CourseDetailUiEvent.CourseCautionClickedButton -> cautionClickedButton(
            event.isConfirm, event.isCheckNoMoreShow
        )
    }

    private fun courseStartClicked() = intent {
        shouldShowCourseCautionUseCase(LocalDate.now()).collectLatest { shouldShow ->
            if (shouldShow) {
                reduce { state.copy(isShowCourseCaution = shouldShow) }
            } else {
                postSideEffect(CourseDetailSideEffect.StartCourse(courseId))
            }
        }
    }

    private fun cautionClickedButton(isConfirm: Boolean, isCheckedNoMoreShow: Boolean) = intent {
        if (isConfirm) {
            if (isCheckedNoMoreShow) {
                viewModelScope.launch {
                    courseRepository.setLastShowDateCourseCaution(LocalDate.now())
                }
            }
            postSideEffect(CourseDetailSideEffect.StartCourse(courseId))
        }
        reduce { state.copy(isShowCourseCaution = false) }
    }

    private fun backClicked() = intent {
        when {
            state.isShowCourseCaution -> reduce { state.copy(isShowCourseCaution = false) }
            else -> postSideEffect(CourseDetailSideEffect.GoBack)
        }
    }
}