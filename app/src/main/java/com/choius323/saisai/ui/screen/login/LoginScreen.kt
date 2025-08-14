package com.choius323.saisai.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
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

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    goHome: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.LoginSuccess -> goHome()
            is LoginSideEffect.ShowToast -> {
                context.SaiToast(sideEffect.message)
            }
        }
    }

    LoginScreenContent(
        uiState = uiState,
        modifier = modifier,
        onEvent = viewModel::onEvent
    )
    if (uiState.isLoading) {
        FullScreenLoading()
    }
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    onEvent: (LoginUiEvent) -> Unit,
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_background_full),
            contentDescription = "스플래시 배경 이미지",
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF222B40),
                                Color(0x00222B40)
                            )
                        )
                    )
                },
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-50).dp)
        ) {
            SaiText(
                text = "사이사이",
                style = Typography.AppTitle.copy(fontSize = 42.sp, fontWeight = FontWeight.W400),
                color = SaiColor.White
            )
            SaiText(
                text = "일상 사이 스며드는 라이딩 코스",
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                color = SaiColor.White
            )
        }
        LoginButtons(
            modifier = Modifier.offset(y = 130.dp),
            onGoogleSuccess = { onEvent(LoginUiEvent.SuccessOAuthLogin(LoginType.GOOGLE, it)) },
            onKakaoSuccess = { onEvent(LoginUiEvent.SuccessOAuthLogin(LoginType.KAKAO, it)) },
            onError = { }
        )
    }
}

@Composable
fun LoginButtons(
    modifier: Modifier = Modifier,
    onGoogleSuccess: (idToken: String) -> Unit,
    onKakaoSuccess: (accessToken: String) -> Unit,
    onError: (Throwable) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp), modifier = modifier
    ) {
        Image(
            painterResource(R.drawable.ic_google),
            "구글 로그인",
            modifier = Modifier
                .size(48.dp)
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
                .size(48.dp)
                .clip(CircleShape)
                .clickable {
                    KakaoAccountUtil.kakaoLogin(context, onKakaoSuccess, onError)
                },
        )
    }
}

private const val TAG = "LoginScreen"