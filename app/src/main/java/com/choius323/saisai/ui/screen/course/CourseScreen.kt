package com.choius323.saisai.ui.screen.course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.ChallengeInfo
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.threeten.bp.LocalDateTime

@Composable
fun CourseScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseListViewModel = koinViewModel(),
    onClickCourse: (String) -> Unit,
) {
    val uiState by viewModel.collectAsState()

    ProvideAppBar(
        navigationIcon = {
            SaiText("코스")
        }
    )

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CourseListSideEffect.GoCourseDetail -> onClickCourse(sideEffect.courseName)
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
    val themeScrollState = rememberScrollState()
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
                .horizontalScroll(themeScrollState)
        ) {
            for ((index, theme) in listOf(
                "챌린지 중", "테마1", "테마2",
                "테마3", "테마4", "테마5"
            ).withIndex()) {
                ElevatedFilterChip(
                    selected = index == 0,
                    onClick = {},
                    label = { SaiText(theme) },
                    colors = FilterChipDefaults.elevatedFilterChipColors().copy(
                        labelColor = Color.White,
                        containerColor = Color.Gray,
                    )
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(uiState.courseList) { course ->
                CourseListItemHorizontal(
                    Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                onEvent(
                                    CourseListUiEvent.CourseClicked(course.courseName)
                                )
                            }
                        ),
                    course = course
                )
            }
        }
    }
}

@Preview
@Composable
private fun CourseScreenContentPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleCourseList = listOf(
        CourseListItem(
            courseId = "123",
            courseName = "한강 라이딩 코스",
            summary = "서울의 아름다운 풍경을 따라",
            level = 2,
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "서울",
            imageUrl = "https://example.com/image.jpg",
            challengeInfo = ChallengeInfo(
                challengeStatus = "진행중", challengeEndedTime = LocalDateTime.now().plusDays(7)
            )
        ),
        CourseListItem(
            courseId = "quam",
            courseName = "Regina Ellis",
            summary = "nostra",
            level = 2,
            distance = 4.5,
            estimatedTime = 6.7,
            sigun = "vis",
            imageUrl = "https://www.google.com/#q=dicant",
            challengeInfo = null
        ),
        CourseListItem(
            courseId = "123",
            courseName = "한강 라이딩 코스",
            summary = "서울의 아름다운 풍경을 따라",
            level = 2,
            distance = 15.5,
            estimatedTime = 1.5,
            sigun = "서울",
            imageUrl = "https://example.com/image.jpg",
            challengeInfo = ChallengeInfo(
                challengeStatus = "진행중", challengeEndedTime = LocalDateTime.now().minusDays(7)
            )
        )
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