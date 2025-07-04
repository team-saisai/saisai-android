package com.choius323.saisai.ui.screen.home

import android.R.attr.level
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.BadgeInfo
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.PopularChallengeListItem
import com.choius323.saisai.ui.model.RecentCourse
import com.choius323.saisai.ui.theme.SaiTheme
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import org.threeten.bp.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    goToDetail: (Long) -> Unit,
) {
    val uiState by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.GoToDetail -> {
                goToDetail(sideEffect.courseId)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeUiEvent.LoadData())
    }
    ProvideAppBar(navigationIcon = {
        SaiText("사이사이", fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }, actions = {
        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
    })

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    if (uiState.errorMessage != null) {
        Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }
    HomeScreenContent(
        name = uiState.name,
        location = uiState.location,
        temperature = uiState.temperature,
        recentChallenge = uiState.recentCourse,
        trendChallenges = uiState.popularChallenges,
        aroundChallenges = uiState.aroundChallenges,
        badges = uiState.badges,
        modifier = modifier,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun HomeScreenContent(
    name: String?,
    location: String,
    temperature: String,
    recentChallenge: RecentCourse?,
    trendChallenges: List<PopularChallengeListItem>,
    aroundChallenges: List<CourseListItem>,
    badges: List<BadgeInfo>,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
    ) {
        Greeting(
            name, location, temperature
        )
        Spacer(Modifier.height(40.dp))
        recentChallenge?.apply {
            CourseCardSimple(
                modifier = Modifier.clickable { onEvent(HomeUiEvent.CourseClicked(0)) },
                imageUrl = imageUrl ?: "",
                sigun = sigun,
                distance = distance,
                level = level,
                participantCount = 265
            )
            Spacer(Modifier.height(40.dp))
        }
        SaiText(
            "인기 챌린지", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp)
        )
        if (trendChallenges.isNotEmpty()) {
            LazyRow {
                items(trendChallenges) { courseInfo ->
                    CourseCardSimple(
                        imageUrl = courseInfo.imageUrl ?: "",
                        sigun = courseInfo.sigun,
                        distance = courseInfo.distance,
                        level = courseInfo.level,
                        participantCount = courseInfo.participantCount,
                        modifier = Modifier.clickable { onEvent(HomeUiEvent.CourseClicked(0)) }

                    )
                }
            }
        } else {
            SaiText("인기 챌린지가 없습니다.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(40.dp))
        SaiText(
            text = "뱃지 컬렉션", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp)
        )
        BadgeCollectionCard(badges)
        Spacer(Modifier.height(40.dp))
        SaiText(
            text = "내 주변 코스", fontSize = 18.sp, modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable { onEvent(HomeUiEvent.CourseClicked(0)) }
        )
        if (aroundChallenges.isNotEmpty()) {
            LazyRow() {
                items(aroundChallenges) { courseInfo ->
                    CourseCardSimple(
                        imageUrl = courseInfo.imageUrl ?: "",
                        sigun = courseInfo.sigun,
                        distance = courseInfo.distance,
                        level = courseInfo.level,
                        participantCount = 125,
                        modifier = modifier

                    )
                }
            }
        } else {
            SaiText("인기 챌린지가 없습니다.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, name = "HomeScreenContent Preview")
@Composable
fun HomeScreenContentPreview() {
    val dummyCourses = listOf(
        CourseListItem(
            courseId = 12345,
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            courseName = "여의도 - 반포",
            summary = "dapibus",
            level = 2,
            distance = 8.6,
            estimatedTime = 50.0,
            sigun = "서울시 강남구",
            challengeInfo = null,
        ),
        CourseListItem(
            courseId = 123451,
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            courseName = "한강 수영장 - 여의도",
            summary = "dapibus",
            level = 1,
            distance = 5.2,
            estimatedTime = 30.0,
            sigun = "서울시 강남구",
            challengeInfo = null,
        ),
        CourseListItem(
            courseId = 1234,
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            courseName = "여의도 - 반포",
            summary = "dapibus",
            level = 2,
            distance = 8.6,
            estimatedTime = 50.0,
            sigun = "서울시 강남구",
            challengeInfo = null,
        ),
        CourseListItem(
            courseId = 12345123,
            imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
            courseName = "한강 수영장 - 여의도",
            summary = "dapibus",
            level = 1,
            distance = 5.2,
            estimatedTime = 30.0,
            sigun = "서울시 강남구",
            challengeInfo = null,
        ),
    )
    val dummyBadges = listOf(
        BadgeInfo(1, "첫 완주", "badge1.url"),
        BadgeInfo(2, "100km 돌파", "badge2.url"),
        BadgeInfo(3, "산악왕", "badge3.url")
    )
    val dummyTrendChallenges = listOf(
        PopularChallengeListItem(
            challengeStatus = "eruditi",
            courseName = "Camille Sheppard",
            distance = 6.7,
            endedAt = LocalDateTime.now(),
            estimatedTime = 5284,
            level = 1223,
            participantCount = 6935,
            sigun = "neque",
            imageUrl = "",
            courseId = 12
        ),
        PopularChallengeListItem(
            challengeStatus = "scripta",
            courseName = "Alejandro Padilla",
            distance = 12.13,
            endedAt = LocalDateTime.now(),
            estimatedTime = 7293,
            level = 7323,
            participantCount = 5304,
            sigun = "solum",
            imageUrl = "",
            courseId = 561
        )
    )
    val dummyRecentCourse = RecentCourse(
        courseName = "Chase McDonald",
        distance = 24.25,
        sigun = "sententiae",
        progressRate = 26.27,
        recentDateAt = LocalDateTime.now().minusDays(3),
        imageUrl = "http://www.bing.com/search?q=vitae",
        courseId = 125
    )

    SaiTheme {
        HomeScreenContent(
            name = "사이",
            location = "서울시 강남구",
            temperature = "25°C",
            recentChallenge = dummyRecentCourse,
            trendChallenges = dummyTrendChallenges,
            aroundChallenges = dummyCourses.takeLast(2),
            badges = dummyBadges,
            modifier = Modifier.padding(all = 0.dp),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "HomeScreenContent Preview (No Contents Challenge)")
@Composable
fun HomeScreenContentPreviewNoContents() {
    val dummyCourses = emptyList<CourseListItem>()
    val dummyBadges = emptyList<BadgeInfo>()

    SaiTheme {
        Scaffold { innerPadding ->
            HomeScreenContent(
                name = "방문자",
                location = "부산시 해운대구",
                temperature = "22°C",
                recentChallenge = null,
                trendChallenges = emptyList(),
                aroundChallenges = emptyList(),
                badges = emptyList(),
                modifier = Modifier.padding(innerPadding),
                onEvent = {},
            )
        }
    }
}
