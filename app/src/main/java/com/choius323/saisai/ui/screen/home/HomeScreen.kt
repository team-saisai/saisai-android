package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.CourseListItemVertical
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.BadgeInfo
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.RecentCourse
import com.choius323.saisai.ui.model.UserBadge
import com.choius323.saisai.ui.theme.AppTitle
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.Typography
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.threeten.bp.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    goNotificationList: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    goToDetail: (Long) -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.GoToDetail -> goToDetail(sideEffect.courseId)
            is HomeSideEffect.ShowToast -> context.SaiToast(sideEffect.message)
            is HomeSideEffect.GoNotificationList -> goNotificationList()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeUiEvent.LoadData())
    }
    ProvideAppBar(navigationIcon = {
        SaiText("사이사이", style = Typography.AppTitle)
    }, actions = {
        Icon(
            Icons.Outlined.Notifications,
            "Notifications",
            Modifier.clickable { viewModel.onEvent(HomeUiEvent.OnClickNotification) }
        )
    })

    HomeScreenContent(
        name = uiState.name,
        recentChallenge = uiState.recentCourse,
        trendChallenges = uiState.popularChallenges,
        badges = uiState.badges,
        modifier = modifier,
        onEvent = viewModel::onEvent,
    )
    if (uiState.isLoading) {
        FullScreenLoading(isModal = true)
    }
}

@Composable
fun HomeScreenContent(
    name: String?,
    recentChallenge: RecentCourse?,
    trendChallenges: List<CourseListItem>,
    badges: List<UserBadge>,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
    ) {
        Greeting(name)
        Spacer(Modifier.height(36.dp))
        recentChallenge?.apply {
            RecentCourseCard(
                this, onClickCourse = { onEvent(HomeUiEvent.CourseClicked(it)) }
            )
            Spacer(Modifier.height(40.dp))
        }
        SaiText(
            "인기 챌린지", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp)
        )
        if (trendChallenges.isNotEmpty()) {
            Box(Modifier.layout { measurable, constraints ->
                // 부모가 준 최대 너비 제약에 오른쪽 패딩(22.dp)만큼을 더합니다.
                val newConstraints = constraints.copy(
                    maxWidth = constraints.maxWidth + 22.dp.roundToPx()

                )
                val placeable = measurable.measure(newConstraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 11.dp)
                        .height(IntrinsicSize.Max)
                ) {
                    for (courseInfo in trendChallenges) {
                        CourseListItemVertical(
                            imageUrl = courseInfo.imageUrl ?: "",
                            courseName = courseInfo.courseName,
                            distance = courseInfo.distance,
                            level = courseInfo.level,
                            isEventActive = courseInfo.isEventActive,
                            endDate = courseInfo.challengeEndedAt,
                            reward = courseInfo.reward,
                            participantCount = courseInfo.participantsCount,
                            modifier = Modifier
                                .clickable { onEvent(HomeUiEvent.CourseClicked(courseInfo.courseId)) },
                            isBookmarked = courseInfo.isBookmarked,
                            onClickBookmark = {
                                onEvent(
                                    HomeUiEvent.OnClickBookmark(
                                        courseInfo.courseId,
                                        courseInfo.isBookmarked
                                    )
                                )
                            }
                        )
                    }
                }
            }
        } else {
            SaiText("인기 챌린지가 없습니다.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(40.dp))
        SaiText(
            text = "나의 뱃지", fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp)
        )
        BadgeCollectionCard(badges, Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true, name = "HomeScreenContent Preview")
@Composable
fun HomeScreenContentPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val dummyCourses = listOf(
        CourseListItem.dummyItem1,
        CourseListItem.dummyItem2,
        CourseListItem.dummyItem3,
        CourseListItem.dummyItem4,
    )
    val dummyBadges = listOf(
        UserBadge.sample1, UserBadge.sample2, UserBadge.sample3, UserBadge.sample4,
    )
    val dummyRecentCourse = RecentCourse(
        courseName = "Chase McDonald",
        distance = 24.25,
        sigun = "sententiae",
        progressRate = 26.27,
        recentDateAt = LocalDate.now().minusDays(3),
        imageUrl = "http://www.bing.com/search?q=vitae",
        courseId = 125
    )

    SaiTheme {
        HomeScreenContent(
            name = "사이",
            recentChallenge = dummyRecentCourse,
            trendChallenges = dummyCourses.take(2),
            badges = dummyBadges,
            modifier = Modifier.padding(all = 0.dp),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "HomeScreenContent Preview (No Contents Challenge)")
@Composable
fun HomeScreenContentPreviewNoContents() {
    emptyList<CourseListItem>()
    emptyList<BadgeInfo>()

    SaiTheme {
        Scaffold { innerPadding ->
            HomeScreenContent(
                name = "방문자",
                recentChallenge = null,
                trendChallenges = emptyList(),
                badges = emptyList(),
                modifier = Modifier.padding(innerPadding),
                onEvent = {},
            )
        }
    }
}
