package com.choius323.saisai.ui.screen.record

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.coroutineScope
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.HandlePermissionActions
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.screen.map.getCurrentLocation
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.util.locationPermissions
import com.choius323.saisai.util.postNotificationPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel = koinViewModel(),
    goHome: () -> Unit,
    goCourseDetail: (Long) -> Unit,
    onBack: () -> Unit,
) {
    ChangeStatusBarIconsColor()
    val context = LocalContext.current
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
    val permissionState = rememberMultiplePermissionsState(
        postNotificationPermissions + locationPermissions
    ) { resultMap ->
        val allGranted = resultMap.all { it.value }
        viewModel.onEvent(RecordUiEvent.SetPermissionGranted(allGranted))
        if (allGranted) {
            getCurrentLocation(
                context = context,
                callbackLocation = { location ->
                    viewModel.onEvent(
                        RecordUiEvent.StartRecording(
                            isPermissionGranted = true,
                            nowLatLng = LatLng.from(location.latitude, location.longitude)
                        )
                    )
                })
        }
    }
    HandlePermissionActions(
        permissionState,
        isShowPermissionDialog = uiState.isShowPermissionDialog,
        setShowPermissionDialog = { viewModel.onEvent(RecordUiEvent.SetShowPermissionDialog(it)) },
        onPermissionAllGranted = {
            if (permissionState.allPermissionsGranted) {
                viewModel.onEvent(RecordUiEvent.SetPermissionGranted(true))
            } else {
                viewModel.onEvent(RecordUiEvent.SetShowPermissionDialog(true))
            }
        })
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RecordSideEffect.NavigateBack -> onBack()
            is RecordSideEffect.PermissionRequest -> permissionState.launchMultiplePermissionRequest()
            is RecordSideEffect.ShowToast -> Toast.makeText(
                context, sideEffect.msg, Toast.LENGTH_SHORT
            ).show()

            is RecordSideEffect.PermissionCheck -> viewModel.onEvent(
                RecordUiEvent.SetPermissionGranted(
                    permissionState.allPermissionsGranted
                )
            )

            is RecordSideEffect.StartRecording -> {
                getCurrentLocation(
                    context = context,
                    callbackLocation = { location ->
                        viewModel.onEvent(
                            RecordUiEvent.StartRecording(
                                isPermissionGranted = permissionState.allPermissionsGranted,
                                nowLatLng = LatLng.from(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                        )
                    })
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onEvent(RecordUiEvent.SetPermissionGranted(permissionState.allPermissionsGranted))
    }
    RecordScreenContent(
        uiState = uiState,
        permissionState = permissionState,
        modifier = modifier,
        onEvent = viewModel::onEvent,
    )
    if (uiState.isShowCompleteDialog) {
        val courseDetail = uiState.courseDetail
        if (courseDetail != null) {
            println("isShowCompleteDialog: ${uiState.isShowCompleteDialog}")
            RideCompleteDialog(
                courseDetail.imageUrl,
                courseDetail.courseName,
                uiState.totalRideDistance,
                endTime = uiState.startTime,
                modifier = modifier.fillMaxSize(),
                goHome = goHome,
                goCourseDetail = { goCourseDetail(courseDetail.courseId) }
            )
        } else {
            Toast.makeText(context, "코스 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    if (uiState.isLoading) {
        FullScreenLoading()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordScreenContent(
    uiState: RecordUiState,
    permissionState: MultiplePermissionsState,
    modifier: Modifier = Modifier,
    onEvent: (RecordUiEvent) -> Unit = {},
) {
    Box(modifier) {
        RecordMapSection(uiState, Modifier.fillMaxSize(), onEvent)
        TimerText(
            startTime = uiState.startTime,
            modifier = Modifier.align(Alignment.TopCenter),
            isRecording = uiState.isRecording
        )
        Button(
            onClick = { onEvent(RecordUiEvent.ClickedStart(permissionState.allPermissionsGranted)) },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            SaiText("기록 시작")
        }
        RecordStateDescription(
            uiState = uiState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
        Log.d(TAG, "$startTime $elapsedTimeInSeconds")
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

            String.format(Locale.KOREA, "%02d:%02d:%02d", hours, minutes, seconds)
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

private const val TAG = "RecordScreen"