package com.choius323.saisai.ui.screen.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.CourseListItemVertical
import com.choius323.saisai.ui.component.FullBleedContainer
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.saiToast
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

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    goNotificationList: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    goToDetail: (Long) -> Unit,
    goToRecord: (Long) -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.GoToDetail -> goToDetail(sideEffect.courseId)
            is HomeSideEffect.GoToRecord -> goToRecord(sideEffect.courseId)
            is HomeSideEffect.ShowToast -> context.saiToast(sideEffect.message)
            is HomeSideEffect.GoNotificationList -> goNotificationList()
        }
    }

    HomeBackHandler()

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeUiEvent.LoadData(true))
    }
    ProvideAppBar(
        navigationIcon = {
            SaiText("사이사이", style = Typography.AppTitle)
        },
//        actions = {
//            Icon(
//                Icons.Outlined.Notifications,
//                "Notifications",
//                Modifier.clickable { viewModel.onEvent(HomeUiEvent.OnClickNotification) }
//            )
//        }
    )

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
                this,
                onClickCourse = { onEvent(HomeUiEvent.CourseClicked(it)) },
                onClickResume = { onEvent(HomeUiEvent.ResumeClicked(it)) }
            )
            Spacer(Modifier.height(40.dp))
        }
        SaiText(
            "추천 코스", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp)
        )
        if (trendChallenges.isNotEmpty()) {
            FullBleedContainer(
                modifier = Modifier.height(IntrinsicSize.Max)
            ) { measuredStartPadding, measuredEndPadding ->
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .fillMaxHeight()
                        .padding(
                            start = measuredStartPadding,
                            end = measuredEndPadding
                        ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (courseInfo in trendChallenges) {
                        CourseListItemVertical(
                            courseInfo,
                            Modifier
                                .clickable { onEvent(HomeUiEvent.CourseClicked(courseInfo.courseId)) },
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
            SaiText("추천 코스가 없습니다.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(40.dp))
        SaiText(
            text = "나의 뱃지", fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp)
        )
        BadgeCollectionCard(badges, Modifier.fillMaxWidth())
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun HomeBackHandler() {
    val context = LocalContext.current
    val activity = (context as? Activity)
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        // 1초 안에 다시 눌렀다면 앱을 종료합니다.
        if (System.currentTimeMillis() - backPressedTime <= 1000L) {
            activity?.finish()
        } else {
            backPressedTime = System.currentTimeMillis()
            context.saiToast("한 번 더 누르면 종료됩니다.")
        }
    }
}

@Preview(showBackground = true, name = "HomeScreenContent Preview")
@Composable
private fun HomeScreenContentPreview() {
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
private fun HomeScreenContentPreviewNoContents() {
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
