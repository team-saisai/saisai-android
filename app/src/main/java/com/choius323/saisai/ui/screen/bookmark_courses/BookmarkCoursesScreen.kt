package com.choius323.saisai.ui.screen.bookmark_courses

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BookmarkCoursesScreen(
    modifier: Modifier = Modifier,
    viewModel: BookmarkCoursesViewModel = koinViewModel(),
    goCourseDetail: (Long) -> Unit,
    goBack: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    HandleAppBar(uiState.editMode, onEvent = viewModel::onEvent)
    BackHandler {
        when {
            uiState.showDeleteDialog -> viewModel.onEvent(BookmarkCoursesUiEvent.OnClickDialogDismiss)
            uiState.editMode -> viewModel.onEvent(BookmarkCoursesUiEvent.OnClickCancel)
            else -> viewModel.onEvent(BookmarkCoursesUiEvent.OnClickBack)
        }
    }
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is BookmarkCoursesSideEffect.GoBack -> goBack()
            is BookmarkCoursesSideEffect.GoCourseDetail -> goCourseDetail(sideEffect.courseId)
            is BookmarkCoursesSideEffect.ShowToast -> {
                context.SaiToast(sideEffect.message)
            }
        }
    }

    BookmarkCoursesScreenContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
    DeleteBookmarkDialog(
        uiState.showDeleteDialog,
        uiState.deletedIndexList.size.takeIf { it != 0 } ?: uiState.courseList.size,
        onDismissRequest = { viewModel.onEvent(BookmarkCoursesUiEvent.OnClickDialogDismiss) },
        onConfirm = { viewModel.onEvent(BookmarkCoursesUiEvent.OnClickDialogConfirm) },
        modifier = modifier,
    )
    if (uiState.isLoading) {
        FullScreenLoading(isModal = true)
    }
}

@Composable
private fun BookmarkCoursesScreenContent(
    uiState: BookmarkCoursesUiState,
    modifier: Modifier = Modifier,
    onEvent: (BookmarkCoursesUiEvent) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        BookmarkCoursesListSection(
            courseList = uiState.courseList,
            selectedIndexList = uiState.selectedIndices,
            isEditMode = uiState.editMode,
            onCourseClick = { index -> onEvent(BookmarkCoursesUiEvent.OnClickCourse(index)) },
            onCourseSelect = { index -> onEvent(BookmarkCoursesUiEvent.OnClickDeleteItem(index)) },
            modifier = Modifier.weight(1f),
            loadMore = { onEvent(BookmarkCoursesUiEvent.LoadMore) },
            isLoadingMore = uiState.isLoadingMore,
            deletedIndexList = uiState.deletedIndexList,
            onClickBookmark = { index -> onEvent(BookmarkCoursesUiEvent.OnClickBookmark(index)) },
        )
        if (uiState.editMode) {
            DeleteSnackbar(
                uiState.selectedIndices.size,
                onClickDeleteAll = { onEvent(BookmarkCoursesUiEvent.OnClickDeleteAll) },
                onClickDeleteSelected = { onEvent(BookmarkCoursesUiEvent.OnClickDeleteSelected) },
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun HandleAppBar(
    editMode: Boolean,
    onEvent: (BookmarkCoursesUiEvent) -> Unit,
) {
    if (editMode) {
        ProvideAppBar(
            { TextButton("취소") { onEvent(BookmarkCoursesUiEvent.OnClickCancel) } },
            { SaiText("저장한 코스", fontSize = 18.sp) },
            { TextButton("완료") { onEvent(BookmarkCoursesUiEvent.OnClickConfirm) } }
        )
    } else {
        ProvideAppBar(
            {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBackIos,
                    "뒤로 가기",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onEvent(BookmarkCoursesUiEvent.OnClickBack) }
                        .padding(8.dp)
                )
            },
            { SaiText("저장한 코스", fontSize = 18.sp) },
            { TextButton("편집") { onEvent(BookmarkCoursesUiEvent.OnClickEdit) } }
        )
    }
}

@Composable
private fun TextButton(text: String, color: Color = Color.Unspecified, onClick: () -> Unit) {
    SaiText(
        text, fontSize = 14.sp, color = color,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 11.5.dp, horizontal = 7.5.dp)
    )
}

