package com.choius323.saisai.ui.screen.bookmark_courses

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.component.bottomNavigationBarHeight
import com.choius323.saisai.ui.theme.SaiColor
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BookmarkCoursesScreen(
    modifier: Modifier = Modifier,
    viewModel: BookmarkCoursesViewModel = koinViewModel(),
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
            is BookmarkCoursesSideEffect.ShowToast -> {
                context.SaiToast(sideEffect.message)
            }
        }
    }
    DeleteBookmarkDialog(
        uiState.showDeleteDialog,
        uiState.selectedIndices.size,
        onDismissRequest = { viewModel.onEvent(BookmarkCoursesUiEvent.OnClickDialogDismiss) },
        onConfirm = { viewModel.onEvent(BookmarkCoursesUiEvent.OnClickDialogConfirm) },
        modifier = modifier,
    )

    BookmarkCoursesScreenContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
    if (uiState.isLoading) {
        FullScreenLoading()
    }
}

@Composable
private fun BookmarkCoursesScreenContent(
    uiState: BookmarkCoursesUiState,
    modifier: Modifier = Modifier,
    onEvent: (BookmarkCoursesUiEvent) -> Unit,
) {
    Box(modifier = modifier) {
        BookmarkCoursesListSection(
            courseList = uiState.courseList,
            selectedIndexList = uiState.selectedIndices,
            isEditMode = uiState.editMode,
            onCourseClick = { courseId -> onEvent(BookmarkCoursesUiEvent.OnClickCourse(courseId)) },
            onCourseSelect = { courseId -> onEvent(BookmarkCoursesUiEvent.OnClickDeleteItem(courseId)) },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 12.dp, end = 12.dp)
        )
        if (uiState.editMode) {
            DeleteSnackbar(
                uiState.courseList.size,
                onClickDeleteAll = { onEvent(BookmarkCoursesUiEvent.OnClickDeleteAll) },
                onClickDeleteSelected = { /* TODO: 선택삭제 버튼 */ },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun DeleteSnackbar(
    count: Int,
    onClickDeleteAll: () -> Unit,
    onClickDeleteSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(bottomNavigationBarHeight + 48.dp)
            .background(SaiColor.Gray80)
            .padding(start = 20.dp, end = 14.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (count == 0) {
            Spacer(Modifier.weight(1f))
            SaiText(
                "전체 삭제",
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable(onClick = onClickDeleteAll)
                    .padding(horizontal = 10.dp, vertical = 11.5.dp)
            )
        } else {
            SaiText("${count}개 선택", fontSize = 16.sp)
            SaiText(
                "선택 삭제",
                fontSize = 14.sp,
                color = Color(0xFFF47C7C),
                modifier = Modifier
                    .clickable(onClick = onClickDeleteSelected)
                    .padding(horizontal = 10.dp, vertical = 11.5.dp)
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
            {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBackIos,
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
    } else {
        ProvideAppBar(
            { TextButton("취소") { onEvent(BookmarkCoursesUiEvent.OnClickCancel) } },
            { SaiText("저장한 코스", fontSize = 18.sp) },
            { TextButton("완료") { onEvent(BookmarkCoursesUiEvent.OnClickConfirm) } }
        )
    }
}

@Composable
private fun TextButton(text: String, onClick: () -> Unit) {
    SaiText(
        text, fontSize = 14.sp,
        modifier = Modifier
            .padding(vertical = 11.5.dp, horizontal = 7.5.dp)
            .clickable(onClick = onClick)
    )
}
