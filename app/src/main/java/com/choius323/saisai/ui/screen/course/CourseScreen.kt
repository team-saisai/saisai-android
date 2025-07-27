package com.choius323.saisai.ui.screen.course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.component.CourseListItemHorizontal
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CourseScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseListViewModel = koinViewModel(),
    onClickCourse: (Long) -> Unit,
) {
    val uiState by viewModel.collectAsState()

    ProvideAppBar(
        navigationIcon = {
            SaiText("코스")
        }
    )

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CourseListSideEffect.GoCourseDetail -> onClickCourse(sideEffect.courseId)
        }
    }

    CourseScreenContent(uiState, modifier, viewModel::onEvent)
}

@Composable
fun CourseScreenContent(
    uiState: CourseListUiState,
    modifier: Modifier = Modifier,
    onEvent: (CourseListUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // TODO: 챌린지 코스 여부, 정렬 기준
        }
        Spacer(Modifier.height(20.dp))
        CourseListSection(
            courseList = uiState.courseList,
            isLoadingMore = uiState.isLoadingMore,
            onEvent = onEvent
        )
    }
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun CourseListSection(
    courseList: List<CourseListItem>,
    isLoadingMore: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (CourseListUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(courseList, { _, course -> course.courseId }) { index, course ->
            CourseListItemHorizontal(
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            onEvent(
                                CourseListUiEvent.CourseClicked(course.courseId)
                            )
                        }
                    ),
                course = course
            )
            if (index == courseList.lastIndex) {
                onEvent(CourseListUiEvent.LoadMore)
            }
        }
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Preview
@Composable
private fun CourseScreenContentPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleCourseList = listOf(
        CourseListItem.dummyItem1,
        CourseListItem.dummyItem2,
        CourseListItem.dummyItem3
    )
    SaiTheme {
        Surface {
            CourseScreenContent(
                uiState = CourseListUiState(
                    courseList = sampleCourseList
                ),
                onEvent = {}
            )
        }
    }
}