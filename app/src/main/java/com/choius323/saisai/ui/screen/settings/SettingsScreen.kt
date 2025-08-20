package com.choius323.saisai.ui.screen.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.HandlePermissionActions
import com.choius323.saisai.ui.component.PermissionDialog
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiDialog
import com.choius323.saisai.ui.component.SaiSwitch
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.LoginType
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.util.GoogleAccountUtil
import com.choius323.saisai.util.KakaoAccountUtil
import com.choius323.saisai.util.locationPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    goBack: () -> Unit,
    goLogin: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    val googleSignInClient = remember { GoogleSignIn.getClient(context, GoogleAccountUtil.gso) }
    val googleSignInLauncher = GoogleAccountUtil.rememberGoogleSignInLauncherWithAccount(
        onSuccess = { account, accessToken ->
            viewModel.onEvent(SettingsUiEvent.OnReLoginSuccess(LoginType.GOOGLE, accessToken))
        },
        onError = { message, exception ->
            viewModel.onEvent(SettingsUiEvent.OnAccountManageFailed(message))
            exception?.let { Log.e("GoogleLogin", "Error during sign-in", it) }
        }
    )

    val permissionState = rememberMultiplePermissionsState(
        locationPermissions
    ) { resultMap ->
        val allGranted = resultMap.all { it.value }
        viewModel.onEvent(SettingsUiEvent.SetPermissionGranted(allGranted))
    }
    LaunchedEffect(permissionState.allPermissionsGranted) {
        viewModel.onEvent(SettingsUiEvent.SetPermissionGranted(permissionState.allPermissionsGranted))
    }
    HandlePermissionActions(
        permissionState,
        isShowPermissionDialog = uiState.isShowPermissionDialog,
        setShowPermissionDialog = { viewModel.onEvent(SettingsUiEvent.SetShowPermissionDialog(it)) },
        onPermissionAllGranted = {
            if (permissionState.allPermissionsGranted) {
                viewModel.onEvent(SettingsUiEvent.SetPermissionGranted(true))
            } else {
                viewModel.onEvent(SettingsUiEvent.SetShowPermissionDialog(true))
            }
        })

    ProvideAppBar(navigationIcon = {
        Icon(
            Icons.AutoMirrored.Default.ArrowBackIos,
            "뒤로 가기",
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = goBack)
                .padding(8.dp)
        )
    }, title = {
        SaiText("APP 설정", fontSize = 18.sp)
    })
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SettingsSideEffect.GoBack -> goBack()
            is SettingsSideEffect.ShowToast -> context.SaiToast(sideEffect.message)
            SettingsSideEffect.GoLogin -> goLogin()
            is SettingsSideEffect.ReLoginOAuth -> when (sideEffect.loginType) {
                LoginType.KAKAO -> {
                    KakaoAccountUtil.kakaoLogin(context, { accessToken ->
                        viewModel.onEvent(
                            SettingsUiEvent.OnReLoginSuccess(
                                sideEffect.loginType,
                                accessToken
                            )
                        )
                    }, {
                        viewModel.onEvent(SettingsUiEvent.OnAccountManageFailed(it.message.toString()))
                    })
                }

                LoginType.GOOGLE -> {
                    val signInIntent = googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)
                }
            }

            SettingsSideEffect.PermissionRequest -> permissionState.launchMultiplePermissionRequest()
        }
    }
    SettingsScreenContent(uiState, modifier, viewModel::onEvent)
    SaiDialog(
        isShow = uiState.isShowLogOutDialog,
        content = "로그아웃 하시겠습니까?",
        confirmButtonText = "로그아웃",
        modifier = Modifier,
    ) {
        viewModel.onEvent(SettingsUiEvent.OnClickLogOutDialogButton(it))
    }
    SaiDialog(
        isShow = uiState.isShowDeleteAccountDialog,
        content = "회원탈퇴 하시겠습니까?",
        subContent = "탈퇴 후에는 복구할 수 없습니다.",
        confirmButtonText = "회원탈퇴",
        confirmButtonColor = Color(0xFFFF7676),
        modifier = Modifier,
    ) {
        viewModel.onEvent(SettingsUiEvent.OnClickDeleteAccountDialogButton(it))
    }
    if (uiState.isLoading) {
        FullScreenLoading(isModal = true)
    }
}

@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    modifier: Modifier = Modifier,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    if (uiState.isGPSPermissionGranted) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    } else {
                        onEvent(SettingsUiEvent.PermissionRequest)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuText("위치 정보 수집 권한")
            Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                SaiSwitch(uiState.isGPSPermissionGranted)
            }
        }
        HorizontalDivider(color = Color(0xFF31353A))
        Spacer(Modifier.height(40.dp))
        SaiText(
            "계정 관리",
            Modifier.padding(start = 8.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            color = SaiColor.White
        )
        Spacer(Modifier.height(12.dp))
        MenuText(
            "로그아웃",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(SettingsUiEvent.OnClickLogOut) })
        HorizontalDivider(color = Color(0xFF31353A))
        MenuText(
            "회원탈퇴",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(SettingsUiEvent.OnClickDeleteAccount) })
    }
}

@Composable
private fun MenuText(text: String, modifier: Modifier = Modifier) {
    SaiText(
        text,
        modifier.padding(vertical = 21.dp, horizontal = 8.dp),
        fontSize = 15.sp,
        fontWeight = FontWeight.W400,
        color = SaiColor.Gray20
    )
}

@Preview
@Composable
private fun SettingsScreenContentPreview() {
    SettingsScreenContent(
        uiState = SettingsUiState(),
        modifier = Modifier,
        onEvent = {})
}