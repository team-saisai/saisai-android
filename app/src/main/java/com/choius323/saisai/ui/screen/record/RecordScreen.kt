package com.choius323.saisai.ui.screen.record

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.ChangeStatusBarIconsColor
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.HandlePermissionActions
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.TopAppBarHeight
import com.choius323.saisai.ui.screen.map.getCurrentLocation
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.util.locationPermissions
import com.choius323.saisai.util.postNotificationPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kakao.vectormap.LatLng
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

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
    val uiState by viewModel.collectAsState()
    ProvideAppBar(
        navigationIcon = {
            if (uiState.rideState != RideState.COMPLETE) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Go Back",
                    modifier = Modifier.clickable(onClick = onBack),
                    tint = SaiColor.Gray90
                )
            }
        },
        title = {
            if (uiState.rideState != RideState.COMPLETE) {
                SaiText(
                    uiState.courseDetail?.courseName ?: "",
                    color = SaiColor.Gray90, fontSize = 18.sp, fontWeight = FontWeight.W500
                )
            }
        }
    )

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
                if (permissionState.allPermissionsGranted) {
                    getCurrentLocation(
                        context = context,
                        callbackLocation = { location ->
                            viewModel.onEvent(
                                RecordUiEvent.StartRecording(
                                    isPermissionGranted = permissionState.allPermissionsGranted,
                                    nowLatLng = LatLng.from(location.latitude, location.longitude)
                                )
                            )
                        })
                } else {
                    viewModel.onEvent(RecordUiEvent.SetShowPermissionDialog(true))
                }
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
            RideCompleteDialog(
                courseDetail.imageUrl,
                courseDetail.courseName,
                courseDetail.distance,
                endTime = uiState.totalSeconds,
                modifier = modifier.fillMaxSize(),
                goHome = goHome,
                goCourseDetail = { goCourseDetail(courseDetail.courseId) }
            )
        } else {
            Toast.makeText(context, "코스 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    if (uiState.isLoading) {
        FullScreenLoading(isModal = true)
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
    val context = LocalContext.current
    Box(modifier) {
        RecordMapSection(uiState, Modifier.fillMaxSize(), onEvent)
        RecordTimerText(
            totalSeconds = uiState.totalSeconds,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = TopAppBarHeight + 12.dp),
            rideState = uiState.rideState,
        )
        Button(
            onClick = { onEvent(RecordUiEvent.ClickedStart(permissionState.allPermissionsGranted)) },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            SaiText("기록 시작")
        }
        RecordStateDescription(
            uiState = uiState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(354.dp)
                .padding(bottom = 32.dp),
            startRecording = {
                getCurrentLocation(
                    context = context,
                    callbackLocation = { location ->
                        onEvent(
                            RecordUiEvent.StartRecording(
                                isPermissionGranted = permissionState.allPermissionsGranted,
                                nowLatLng = LatLng.from(location.latitude, location.longitude)
                            )
                        )
                    })
            },
            toggleRecording = { onEvent(RecordUiEvent.OnClickToggleRecording) },
            toggleExpanded = { onEvent(RecordUiEvent.OnToggleExpandedSummary) }
        )
    }
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

private const val TAG = "RecordScreen"