package com.choius323.saisai.ui.screen.record

import android.location.Location
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.component.TopAppBarHeight
import com.choius323.saisai.ui.screen.map.getCurrentLocation
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.util.locationPermissions
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
    BackHandler {
        viewModel.onEvent(RecordUiEvent.OnClickBack)
    }
    ProvideAppBar(
        navigationIcon = {
            if (uiState.rideState != RideState.COMPLETE) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Go Back",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                        .clickable { viewModel.onEvent(RecordUiEvent.OnClickBack) },
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
//        postNotificationPermissions +
        locationPermissions
    ) { resultMap ->
        val allGranted = resultMap.all { it.value }
        viewModel.onEvent(RecordUiEvent.SetPermissionGranted(allGranted))
        if (allGranted) {
            getCurrentLocation(
                context = context,
                callbackLocation = { location: Location? ->
                    if (location != null) {
                        viewModel.onEvent(
                            RecordUiEvent.StartRecording(
                                isPermissionGranted = true,
                                latLng = LatLng.from(location.latitude, location.longitude)
                            )
                        )
                    } else {
                        context.SaiToast("위치 정보를 가져올 수 없습니다.")
                    }
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
            is RecordSideEffect.ShowToast -> context.SaiToast(sideEffect.msg)

            is RecordSideEffect.PermissionCheck ->
                if (permissionState.allPermissionsGranted) {
                    getCurrentLocation(
                        context = context,
                        callbackLocation = { location: Location? ->
                            if (location != null) {
                                viewModel.onEvent(
                                    RecordUiEvent.StartRecording(
                                        isPermissionGranted = permissionState.allPermissionsGranted,
                                        latLng = LatLng.from(location.latitude, location.longitude)
                                    )
                                )
                            } else {
                                context.SaiToast("위치 정보를 가져올 수 없습니다.")
                            }
                        })
                } else {
                    viewModel.onEvent(RecordUiEvent.SetShowPermissionDialog(true))
                }

            is RecordSideEffect.StartRecording ->
                if (permissionState.allPermissionsGranted) {
                    getCurrentLocation(
                        context = context,
                        callbackLocation = { location: Location? ->
                            if (location != null) {
                                viewModel.onEvent(
                                    RecordUiEvent.StartRecording(
                                        isPermissionGranted = permissionState.allPermissionsGranted,
                                        latLng = LatLng.from(location.latitude, location.longitude)
                                    )
                                )
                            } else {
                                context.SaiToast("위치 정보를 가져올 수 없습니다.")
                            }
                        })
                } else {
                    viewModel.onEvent(RecordUiEvent.SetShowPermissionDialog(true))
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
                rideTime = uiState.totalSeconds,
                modifier = modifier.fillMaxSize(),
                goHome = goHome,
                goCourseDetail = { goCourseDetail(courseDetail.courseId) }
            )
        } else {
            Toast.makeText(context, "코스 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    RecordBackDialog(uiState.isShowBackDialog) {
        viewModel.onEvent(RecordUiEvent.OnClickBackDialog(it))
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
        RecordStateDescription(
            uiState = uiState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(354.dp)
                .padding(bottom = 16.dp)
                .navigationBarsPadding(),
            startRecording = {
                getCurrentLocation(
                    context = context,
                    callbackLocation = { location: Location? ->
                        if (location != null) {
                            onEvent(
                                RecordUiEvent.StartRecording(
                                    isPermissionGranted = permissionState.allPermissionsGranted,
                                    latLng = LatLng.from(location.latitude, location.longitude)
                                )
                            )
                        } else {
                            context.SaiToast("위치 정보를 가져올 수 없습니다.")
                        }
                    })
            },
            toggleRecording = { onEvent(RecordUiEvent.OnClickToggleRecording) },
            toggleExpanded = { onEvent(RecordUiEvent.OnToggleExpandedSummary) }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
private fun RecordScreenContentPreview() {
    SaiTheme {
        Surface {
            RecordScreenContent(
                RecordUiState(),
                rememberMultiplePermissionsState(listOf())
            ) {}
        }
    }
}

private const val TAG = "RecordScreen"