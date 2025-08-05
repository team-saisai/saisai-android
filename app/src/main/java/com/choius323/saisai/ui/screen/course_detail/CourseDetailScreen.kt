package com.choius323.saisai.ui.screen.course_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.component.ChangeStatusBarIconsColor
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.screen.map.MapScreen
import com.choius323.saisai.ui.screen.map.MapUiEvent
import com.choius323.saisai.ui.screen.map.MapViewModel
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CourseDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CourseDetailViewModel = koinViewModel(),
    mapViewModel: MapViewModel = koinViewModel(),
    goRecordScreen: (courseId: Long) -> Unit,
    goBack: () -> Unit,
) {
    ChangeStatusBarIconsColor()
    val uiState by viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CourseDetailSideEffect.StartCourse -> goRecordScreen(viewModel.courseId)
            is CourseDetailSideEffect.GoBack -> goBack()
        }
    }
    ProvideAppBar(
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Go Back",
                modifier = Modifier.clickable(onClick = goBack)
            )
        })
    LaunchedEffect(uiState.courseDetail) {
        mapViewModel.onEvent(MapUiEvent.SetRoute(uiState.courseDetail?.gpxPointList ?: emptyList()))
    }
    CourseDetailScreenContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent,
        mapContent = {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = mapViewModel
            )
        },
    )
}


@Composable
fun CourseDetailScreenContent(
    uiState: CourseDetailUiState,
    modifier: Modifier = Modifier,
    onEvent: (CourseDetailUiEvent) -> Unit,
    mapContent: @Composable () -> Unit,
) {
    Box(modifier) {
        mapContent()
        if (uiState.courseDetail != null) {
            CourseDetailDescription(
                uiState.courseDetail,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .widthIn(min = 354.dp)
                    .padding(bottom = 32.dp),
                onChallengeClick = { onEvent(CourseDetailUiEvent.CourseStartClicked) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseDetailScreenContentPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleDetails = CourseDetail.sample

    SaiTheme {
        Surface {
            CourseDetailScreenContent(
                uiState = CourseDetailUiState(courseDetail = sampleDetails),
                modifier = Modifier.fillMaxSize(),
                mapContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        SaiText("지도 영역")
                    }
                },
                onEvent = {}
            )
        }
    }
}