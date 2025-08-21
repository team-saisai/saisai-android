package com.choius323.saisai.ui.screen.nickname_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.pretendardFamily
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun NicknameEditScreen(
    modifier: Modifier = Modifier,
    viewModel: NicknameEditViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is NicknameEditSideEffect.NavigateBack -> goBack()
            is NicknameEditSideEffect.NicknameUpdateSuccess -> goBack()
            is NicknameEditSideEffect.ShowToast -> context.SaiToast(sideEffect.message)
        }
    }

    ProvideAppBar(
        navigationIcon = {
            Icon(
                Icons.AutoMirrored.Default.ArrowBackIos,
                "뒤로 가기",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = goBack)
                    .padding(8.dp)
            )
        },
        title = {
            SaiText(
                "닉네임 수정",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = SaiColor.Gray20
            )
        }
    )

    NicknameEditScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier.imePadding()
    )

    if (uiState.isLoading) {
        FullScreenLoading(isModal = true)
    }
}

@Composable
private fun NicknameEditScreenContent(
    uiState: NicknameEditUiState,
    onEvent: (NicknameEditUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x0DFFFFFF), RoundedCornerShape(6.dp))
                .let {
                    if (uiState.isValid) {
                        Modifier.border(
                            1.dp,
                            SaiColor.Gray60,
                            RoundedCornerShape(6.dp)
                        )
                    } else {
                        it
                    }
                }
                .padding(vertical = 14.dp, horizontal = 16.dp),
        ) {
            BasicTextField(
                value = uiState.nickname,
                onValueChange = { onEvent(NicknameEditUiEvent.OnNicknameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                cursorBrush = SolidColor(Color.White),
                textStyle = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
                    lineHeight = 1.em,
                    fontFamily = pretendardFamily,
                    fontSize = 14.sp,
                    color = Color.White
                )
            ) { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        }
        SupportText(uiState.isPatternValid, "영문/한글/숫자 가능(공백불가)")
        SupportText(uiState.isLengthValid, "1자 이상 7자 이하 입력")
        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onEvent(NicknameEditUiEvent.OnClickDone) },
            modifier = Modifier
                .widthIn(min = 342.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaiColor.Lime,
                contentColor = SaiColor.Gray90,
                disabledContainerColor = SaiColor.Gray80,
                disabledContentColor = SaiColor.Gray90
            ),
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = uiState.isValid
        ) {
            SaiText("완료", fontSize = 16.sp, fontWeight = FontWeight.W600)
        }
    }
}

@Composable
private fun SupportText(isValid: Boolean, text: String, modifier: Modifier = Modifier) {
    val color = if (isValid) Color(0xFF8976ED) else Color(0xFFFF7676)
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (isValid) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline,
            null, Modifier.size(14.dp), tint = color,
        )
        Spacer(Modifier.width(4.dp))
        SaiText(
            text,
            fontSize = 12.sp,
            color = color,
            fontWeight = FontWeight.W400,
            letterSpacing = (-0.41).sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202326, heightDp = DEVICE_HEIGHT_DP)
@Composable
private fun NicknameEditScreenPreviewTyped() {
    SaiTheme {
        NicknameEditScreenContent(
            uiState = NicknameEditUiState(nickname = "엘라123"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202326, heightDp = DEVICE_HEIGHT_DP)
@Composable
private fun NicknameEditScreenPreviewDuplicate() {
    SaiTheme {
        NicknameEditScreenContent(
            uiState = NicknameEditUiState(
                nickname = "엘라12!!",
                isLengthValid = true,
                isPatternValid = false,
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202326, heightDp = DEVICE_HEIGHT_DP)
@Composable
private fun NicknameEditScreenPreviewAvailable() {
    SaiTheme {
        NicknameEditScreenContent(
            uiState = NicknameEditUiState(
                nickname = "새로운닉네임",
                isLengthValid = true,
                isPatternValid = true,
            ),
            onEvent = {}
        )
    }
}

private const val DEVICE_HEIGHT_DP = 300