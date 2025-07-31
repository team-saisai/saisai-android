package com.choius323.saisai.ui.screen.course_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.CourseListItemHorizontal
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CourseListScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseListViewModel = koinViewModel(),
    onClickCourse: (Long) -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    ProvideAppBar(
        navigationIcon = {
            SaiText("코스", fontSize = 20.sp, fontWeight = FontWeight.W600)
        }
    )

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CourseListSideEffect.GoCourseDetail -> onClickCourse(sideEffect.courseId)
            is CourseListSideEffect.ShowToast -> context.SaiToast(sideEffect.message)
        }
    }

    CourseListScreenContent(uiState, modifier, viewModel::onEvent)
}

@Composable
fun CourseListScreenContent(
    uiState: CourseListUiState,
    modifier: Modifier = Modifier,
    onEvent: (CourseListUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        CourseListHead(
            courseType = uiState.selectedCourseType,
            courseSort = uiState.selectedSort,
            onSelectedSort = { onEvent(CourseListUiEvent.OnClickSortType(it)) },
            setCourseType = { onEvent(CourseListUiEvent.OnClickCourseType(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(IntrinsicSize.Min)
        )
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
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(courseList, { _, course -> course.courseId }) { index, course ->
            CourseListItemHorizontal(
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            onEvent(CourseListUiEvent.CourseClicked(course.courseId))
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
private fun CourseListScreenContentPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleCourseList = listOf(
        CourseListItem.dummyItem1,
        CourseListItem.dummyItem2,
        CourseListItem.dummyItem3
    )
    SaiTheme {
        Surface {
            CourseListScreenContent(
                uiState = CourseListUiState(
                    courseList = sampleCourseList
                ),
                onEvent = {}
            )
        }
    }
}