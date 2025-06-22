package com.choius323.saisai.ui.screen.home

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
import com.choius323.saisai.ui.model.CourseInfo
import com.choius323.saisai.ui.theme.SaiTheme
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    goToDetail: (Int) -> Unit,
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
        recentChallenge = uiState.recentChallenge,
        trendChallenges = uiState.trendChallenges,
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
    recentChallenge: CourseInfo?,
    trendChallenges: List<CourseInfo>,
    aroundChallenges: List<CourseInfo>,
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
        recentChallenge?.let {
            CourseCardSimple(
                courseInfo = it,
                modifier = Modifier.clickable { onEvent(HomeUiEvent.CourseClicked(it.courseId)) })
            Spacer(Modifier.height(40.dp))
        }
        SaiText(
            "인기 챌린지", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp)
        )
        if (trendChallenges.isNotEmpty()) {
            LazyRow {
                items(trendChallenges) { courseInfo ->
                    CourseCardSimple(courseInfo = courseInfo)
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
            text = "내 주변 코스", fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp)
        )
        if (aroundChallenges.isNotEmpty()) {
            LazyRow() {
                items(aroundChallenges) { courseInfo ->
                    CourseCardSimple(courseInfo = courseInfo)
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
        CourseInfo(
            1, "img1.url", "2024-12-31", "서울", "부산", "450km", "상", 100, listOf("국토종주", "자연"), 50
        ), CourseInfo(
            2, "img2.url", "2025-01-15", "강릉", "속초", "80km", "중", 250, listOf("해안도로"), 120
        ), CourseInfo(
            3,
            "img3.url",
            "2024-11-30",
            "제주 한바퀴",
            "제주 한바퀴",
            "234km",
            "중",
            500,
            listOf("섬일주", "해안"),
            200
        )
    )
    val dummyBadges = listOf(
        BadgeInfo(1, "첫 완주", "badge1.url"),
        BadgeInfo(2, "100km 돌파", "badge2.url"),
        BadgeInfo(3, "산악왕", "badge3.url")
    )

    SaiTheme { // Assuming your theme is named SaiSaiTheme. Adjust if necessary.
        HomeScreenContent(
            name = "사이",
            location = "서울시 강남구",
            temperature = "25°C",
            recentChallenge = dummyCourses.first(),
            trendChallenges = dummyCourses + dummyCourses,
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
    val dummyCourses = emptyList<CourseInfo>()
    val dummyBadges = emptyList<BadgeInfo>()

    SaiTheme { // Assuming your theme is named SaiSaiTheme. Adjust if necessary.
        Scaffold { innerPadding ->
            HomeScreenContent(
                name = "방문자",
                location = "부산시 해운대구",
                temperature = "22°C",
                recentChallenge = null, // Test case with no recent challenge
                trendChallenges = dummyCourses,
                aroundChallenges = dummyCourses,
                badges = dummyBadges,
                modifier = Modifier.padding(innerPadding),
                onEvent = {},
            )
        }
    }
}
