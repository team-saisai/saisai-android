package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.coroutineScope
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.screen.map.MapScreen
import com.choius323.saisai.ui.screen.map.MapUiEvent
import com.choius323.saisai.ui.screen.map.MapViewModel
import com.choius323.saisai.ui.theme.SaiTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel = koinViewModel(),
    mapViewModel: MapViewModel = koinViewModel(),
    onBack: () -> Unit,
) {
    ProvideAppBar(
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Go Back",
                modifier = Modifier.clickable(onClick = onBack)
            )
        }
    )

    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RecordSideEffect.StartRecording -> mapViewModel.onEvent(MapUiEvent.StartRecording)
        }
    }
    LaunchedEffect(uiState.courseDetail) {
        mapViewModel.onEvent(
            MapUiEvent.SetRoute(
                uiState.courseDetail?.gpxPointList ?: emptyList()
            )
        )
    }
    RecordScreenContent(
        uiState = uiState,
        modifier = modifier,
        mapContent = {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = mapViewModel
            )
        },
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun RecordScreenContent(
    uiState: RecordUiState,
    modifier: Modifier = Modifier,
    mapContent: @Composable () -> Unit,
    onEvent: (RecordUiEvent) -> Unit = {},
) {
    Box(modifier) {
        mapContent()
        TimerText(
            startTime = uiState.startTime,
            modifier = Modifier.align(Alignment.TopCenter),
            isRecording = uiState.isRecording
        )
        Button(
            onClick = { onEvent(RecordUiEvent.ClickedStart) },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            SaiText("기록 시작")
        }
    }
}

@Composable
private fun TimerText(
    startTime: Long,
    isRecording: Boolean,
    modifier: Modifier = Modifier,
) {
    var elapsedTimeInSeconds by remember { mutableLongStateOf(0L) }

    LifecycleResumeEffect(startTime, isRecording) {
        println("$startTime $elapsedTimeInSeconds")
        val job = lifecycle.coroutineScope.launch {
            while (isRecording) {
                val now = System.currentTimeMillis()
                elapsedTimeInSeconds = (now - startTime) / 1000

                delay(1000L)
            }
        }
        onPauseOrDispose {
            job.cancel()
        }
    }

    val timeFormatted by remember(elapsedTimeInSeconds) {
        derivedStateOf {
            val hours = TimeUnit.SECONDS.toHours(elapsedTimeInSeconds)
            val minutes = TimeUnit.SECONDS.toMinutes(elapsedTimeInSeconds) % 60
            val seconds = elapsedTimeInSeconds % 60

            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    SaiText(
        text = timeFormatted,
        modifier = modifier
            .offset(y = 12.dp)
            .background(Color(0x996054A8), CircleShape)
            .padding(vertical = 8.dp, horizontal = 16.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun RecordScreenContentPreview() {

    SaiTheme {
        Surface {
            // RecordScreenContent(
            //
            // )
        }
    }
}

@Preview
@Composable
private fun TimerTextPreview() {
    SaiTheme {
        Surface {
            Box(Modifier.padding(bottom = 16.dp)) {
                TimerText(System.currentTimeMillis(), false, Modifier)
            }
        }
    }
}