package com.choius323.saisai.ui.screen.settings

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiDialog
import com.choius323.saisai.ui.component.SaiSwitch
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.LoginType
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.util.AccountUtil
import com.choius323.saisai.util.GoogleAccountUtil
import com.choius323.saisai.util.KakaoAccountUtil
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    goBack: () -> Unit,
    goLogin: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val googleSignInClient = remember { GoogleSignIn.getClient(context, GoogleAccountUtil.gso) }
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.result // 로그인 성공한 Google 계정 정보
                    Log.d("GoogleLogin", "로그인 성공: ${account.email}")

                    // 액세스 토큰은 백그라운드 스레드에서 가져와야 합니다. (네트워크 통신 발생)
                    coroutineScope.launch {
                        try {
                            val accessToken = withContext(Dispatchers.IO) {
                                // "oauth2:" 접두사와 함께 필요한 스코프를 명시합니다.
                                GoogleAuthUtil.getToken(
                                    context,
                                    account.account!!,
                                    "oauth2:${Scope("https://www.googleapis.com/auth/userinfo.profile").scopeUri}"
                                )
                            }
                            Log.d("GoogleLogin", "액세스 토큰 수신 성공: $accessToken")
                            viewModel.onEvent(SettingsUiEvent.OnReLoginSuccess(LoginType.GOOGLE,accessToken))

                        } catch (e: Exception) {
                            Log.e("GoogleLogin", "액세스 토큰 요청 실패", e)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("GoogleLogin", "로그인 결과 처리 실패", e)
                }
            } else {
                Log.w("GoogleLogin", "로그인 결과가 OK가 아닙니다. resultCode: ${result.resultCode}")
            }
        }
    )
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
            is SettingsSideEffect.ReLoginOAuth ->when (sideEffect.loginType) {
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
                    signInLauncher.launch(signInIntent)
                }
            }
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
    Column(
        modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onEvent(SettingsUiEvent.OnGPSPermissionGranted(!uiState.isGPSPermissionGranted)) },
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
        uiState = SettingsUiState(), modifier = Modifier, onEvent = {})
}
