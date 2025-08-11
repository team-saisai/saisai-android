package com.choius323.saisai.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
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
    var passwordVisible by remember { mutableStateOf(false) }

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

        // 임시 로그인
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .offset(y = -(20).dp)
        ) {
            TextField(
                uiState.email,
                onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
                label = { Text("이메일") },
                placeholder = { Text("이메일 주소를 입력해주세요") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                uiState.password,
                onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                label = { Text("비밀번호") },
                placeholder = { Text("비밀번호를 입력해주세요") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보이기"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )
            Button({ onEvent(LoginUiEvent.LoginButtonClicked) }) {
                Text("로그인")
            }
        }
        LoginButtons(
            modifier = Modifier.offset(y = 130.dp),
            onGoogleSuccess = {},
            onKakaoSuccess = {},
        )
    }
}

@Composable
fun LoginButtons(
    modifier: Modifier = Modifier,
    onGoogleSuccess: (idToken: String) -> Unit,
    onKakaoSuccess: (accessToken: String) -> Unit,
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
                        GoogleAccountUtil.googleSignIn(context, onGoogleSuccess, { })
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
                    KakaoAccountUtil.kakaoLogin(context, onKakaoSuccess, {})
                },
        )
    }
}

private const val TAG = "LoginScreen"