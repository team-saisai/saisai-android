package com.choius323.saisai.ui.screen.ride_history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.component.SortDropDown
import com.choius323.saisai.ui.model.CourseSort
import com.choius323.saisai.ui.screen.bookmark_courses.DeleteBookmarkDialog
import com.choius323.saisai.ui.screen.bookmark_courses.DeleteSnackBar
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RideHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: RideHistoryViewModel = koinViewModel(),
    goCourseDetail: (Long) -> Unit,
    goCourseList: () -> Unit,
    goBack: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    HandleAppBar(uiState.editMode, onEvent = viewModel::onEvent)
    BackHandler {
        when {
            uiState.showDeleteDialog -> viewModel.onEvent(RideHistoryUiEvent.OnClickDialogDismiss)
            uiState.editMode -> viewModel.onEvent(RideHistoryUiEvent.OnClickCancel)
            else -> viewModel.onEvent(RideHistoryUiEvent.OnClickBack)
        }
    }
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RideHistorySideEffect.GoBack -> goBack()
            is RideHistorySideEffect.GoCourseDetail -> goCourseDetail(sideEffect.courseId)
            is RideHistorySideEffect.GoCourseList -> goCourseList()
            is RideHistorySideEffect.ShowToast -> {
                context.SaiToast(sideEffect.message)
            }
        }
    }

    RideHistoryScreenContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
    DeleteBookmarkDialog(
        uiState.showDeleteDialog,
        uiState.deletedIndexList.size.takeIf { it != 0 } ?: uiState.courseList.size,
        onDismissRequest = { viewModel.onEvent(RideHistoryUiEvent.OnClickDialogDismiss) },
        onConfirm = { viewModel.onEvent(RideHistoryUiEvent.OnClickDialogConfirm) },
        modifier = modifier,
    )
    if (uiState.isLoading) {
        FullScreenLoading(isModal = true)
    }
}

@Composable
private fun RideHistoryScreenContent(
    uiState: RideHistoryUiState,
    modifier: Modifier = Modifier,
    onEvent: (RideHistoryUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        RidingHeadLine(
            isEditMode = uiState.editMode,
            isRidingOnly = uiState.isRidingOnly,
            selectedCourseSort = uiState.sort,
            modifier = Modifier,
            onSelectedCourseSort = { onEvent(RideHistoryUiEvent.OnSelectedCourseSort(it)) },
            onClickShowRidingOnly = { onEvent(RideHistoryUiEvent.OnClickShowRidingOnly(it)) },
        )
        RideHistoryListSection(
            courseList = uiState.courseList,
            selectedIndexList = uiState.selectedIndices,
            isEditMode = uiState.editMode,
            onCourseClick = { index -> onEvent(RideHistoryUiEvent.OnClickCourse(index)) },
            onCourseSelect = { index -> onEvent(RideHistoryUiEvent.OnClickDeleteItem(index)) },
            modifier = Modifier.weight(1f),
            loadMore = { onEvent(RideHistoryUiEvent.LoadMore) },
            isLoadingMore = uiState.isLoadingMore,
            onClickEmptyButton = { onEvent(RideHistoryUiEvent.OnClickEmptyButton) },
            deletedIndexList = uiState.deletedIndexList,
        )
        if (uiState.editMode) {
            DeleteSnackBar(
                uiState.selectedIndices.size,
                onClickDeleteAll = { onEvent(RideHistoryUiEvent.OnClickDeleteAll) },
                onClickDeleteSelected = { onEvent(RideHistoryUiEvent.OnClickDeleteSelected) },
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun RidingHeadLine(
    isEditMode: Boolean,
    isRidingOnly: Boolean,
    selectedCourseSort: CourseSort,
    modifier: Modifier = Modifier,
    onSelectedCourseSort: (CourseSort) -> Unit,
    onClickShowRidingOnly: (Boolean) -> Unit = {},
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = { onClickShowRidingOnly(isRidingOnly.not()) })
                .padding(start = 4.dp, top = 5.dp, bottom = 5.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isRidingOnly,
                onCheckedChange = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(4.dp))
            SaiText("주행 중인 코스만 보기")
        }
        if (isEditMode.not()) {
            SortDropDown(
                sortList = listOf(
                    CourseSort.newest,
                    CourseSort.ordest,
                ),
                selected = selectedCourseSort,
                onSelectedSort = onSelectedCourseSort,
            )
        }
    }
}

@Composable
private fun HandleAppBar(
    editMode: Boolean,
    onEvent: (RideHistoryUiEvent) -> Unit,
) {
    if (editMode) {
        ProvideAppBar(
            { TextButton("취소") { onEvent(RideHistoryUiEvent.OnClickCancel) } },
            { SaiText("코스 기록", fontSize = 18.sp) },
            { TextButton("완료") { onEvent(RideHistoryUiEvent.OnClickConfirm) } }
        )
    } else {
        ProvideAppBar(
            {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBackIos,
                    "뒤로 가기",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onEvent(RideHistoryUiEvent.OnClickBack) }
                        .padding(8.dp)
                )
            },
            { SaiText("코스 기록", fontSize = 18.sp) },
            { TextButton("편집") { onEvent(RideHistoryUiEvent.OnClickEdit) } }
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

