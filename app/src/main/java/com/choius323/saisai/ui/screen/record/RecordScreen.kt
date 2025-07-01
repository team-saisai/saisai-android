package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseDetails
import com.choius323.saisai.ui.model.Position
import com.choius323.saisai.ui.screen.map.MapScreen
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    ProvideAppBar(
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Go Back",
                modifier = Modifier.clickable(onClick = onBack)
            )
        })
    val route = listOf(
        Position(37.517145, 126.945064),
        Position(37.515084, 127.000494),
        Position(37.536378, 127.025900),
        Position(37.523328, 127.088259),
        Position(37.517145, 126.945064),
        Position(37.515084, 127.000494),
        Position(37.603300, 126.807988)
    )
    RecordScreenContent(
        route = route,
        modifier = modifier,
        { MapScreen(emptyList(), modifier = Modifier.fillMaxSize()) },
    )
}

@Composable
fun RecordScreenContent(
    route: List<Position>,
    modifier: Modifier = Modifier,
    mapContent: @Composable () -> Unit,
    onEvent: () -> Unit = {},
) {
    val sampleDetails = CourseDetails(
        name = "여의대로 - 국회대로",
        distance = "8.6km",
        difficulty = "하",
        themes = listOf("테마 1", "테마 1"),
        challengerCount = 207,
        completedCount = 412
    )
    Box(modifier) {
        mapContent()
        RecordCourseDescription(
            sampleDetails,
            modifier = Modifier.align(Alignment.BottomCenter),
            onChallengeClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecordScreenContentPreview() {
    val sampleDetails = CourseDetails(
        name = "여의대로 - 국회대로",
        distance = "8.6km",
        difficulty = "하",
        themes = listOf("테마 1", "테마 1"),
        challengerCount = 207,
        completedCount = 412
    )

    SaiTheme {
        Surface {
            RecordScreenContent(
                route = emptyList(),
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
                }
            )
        }
    }
}