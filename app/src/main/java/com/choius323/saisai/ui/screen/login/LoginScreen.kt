package com.choius323.saisai.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.saiToast
import com.choius323.saisai.ui.model.LoginType
import com.choius323.saisai.ui.theme.AppTitle
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.Typography
import com.choius323.saisai.util.GoogleAccountUtil
import com.choius323.saisai.util.KakaoAccountUtil
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

var isShownSplash = false

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    goSignUp: (token: String, loginType: String) -> Unit,
    goHome: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    var pendingNavigation by remember { mutableStateOf<(() -> Unit)?>(null) }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.ShowToast -> {
                context.saiToast(sideEffect.message)
            }

            is LoginSideEffect.GoHome -> {
                Log.d(TAG, "SideEffectGoHome")
                pendingNavigation = { goHome() }
            }

            is LoginSideEffect.GoSignUp -> {
                pendingNavigation = { goSignUp(sideEffect.token, sideEffect.loginType.name) }
            }
        }
    }

    LaunchedEffect(uiState.isDelayed, pendingNavigation) {
        Log.d(TAG, "LaunchedEffect: ${uiState.isDelayed}, $pendingNavigation")
        if (uiState.isDelayed && pendingNavigation != null) {
            isShownSplash = true
            pendingNavigation?.invoke()
        }
    }

    LoginScreenContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
    if (uiState.isLoading && uiState.isDelayed) {
        FullScreenLoading()
    }
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    onEvent: (LoginUiEvent) -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_splash_screen),
            contentDescription = "스플래시 배경 이미지",
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    if (uiState.isDelayed) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x00000000),
                                    Color(0x00202326),
                                    Color(0xE5151719),
                                )
                            ),
                            topLeft = Offset(x = 0f, y = size.height * 0.5f)
                        )
                    }
                },
            contentScale = ContentScale.Crop
        )
        // 앱 타이틀
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight(0.48f),
            verticalArrangement = Arrangement.Bottom,
        ) {
            SaiText(
                text = "사이사이",
                style = Typography.AppTitle.copy(fontSize = 42.sp, fontWeight = FontWeight.W400),
                color = SaiColor.White,
                lineHeight = 1.em
            )
            SaiText(
                text = "일상 사이 스며드는 라이딩 코스",
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                color = SaiColor.White
            )
        }
        Box(Modifier.fillMaxHeight(0.76f), contentAlignment = Alignment.BottomCenter) {
            LoginButtons(
                isShow = uiState.isDelayed,
                onGoogleSuccess = { onEvent(LoginUiEvent.SuccessOAuthLogin(LoginType.GOOGLE, it)) },
                onKakaoSuccess = { onEvent(LoginUiEvent.SuccessOAuthLogin(LoginType.KAKAO, it)) },
                onError = { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenContentPreview() {
    LoginScreenContent(
        uiState = LoginUiState(isDelayed = false)
    ) {}
}

@Preview
@Composable
private fun LoginScreenContentPreviewDelayed() {
    LoginScreenContent(
        uiState = LoginUiState(isDelayed = true)
    ) {}
}

@Preview(device = "spec:parent=pixel_5, orientation=landscape")
@Composable
private fun LoginScreenContentPreviewLandscape() {
    LoginScreenContent(
        uiState = LoginUiState(isDelayed = true)
    ) {}
}

@Composable
fun LoginButtons(
    isShow: Boolean,
    modifier: Modifier = Modifier,
    onGoogleSuccess: (idToken: String) -> Unit,
    onKakaoSuccess: (accessToken: String) -> Unit,
    onError: (Throwable) -> Unit = {},
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    if (isShow) {
        Row(
            modifier, horizontalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Image(
                painterResource(R.drawable.ic_google),
                "구글 로그인",
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            GoogleAccountUtil.googleSignIn(context, onGoogleSuccess, onError)
                        }
                    },
            )
            Image(
                painterResource(R.drawable.ic_kakao_talk),
                "구글 로그인",
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .clickable {
                        KakaoAccountUtil.kakaoLogin(context, onKakaoSuccess, onError)
                    },
            )
        }
    }
}

private const val TAG = "LoginScreen"