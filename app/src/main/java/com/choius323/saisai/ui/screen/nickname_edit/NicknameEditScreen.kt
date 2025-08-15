package com.choius323.saisai.ui.screen.nickname_edit

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
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
            .padding(start = 20.dp, end = 20.dp, top = 25.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min) // Row의 높이를 내부 콘텐츠에 맞게 최소화
                    .background(Color(0x0DFFFFFF), RoundedCornerShape(6.dp))
                    .padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 16.dp)
            ) {
                BasicTextField(
                    value = uiState.nickname,
                    onValueChange = { onEvent(NicknameEditUiEvent.OnNicknameChanged(it)) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
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
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                }
                TextFieldTrailingIcon(
                    lengthText = "${uiState.nickname.length}/${uiState.maxNicknameLength}",
                    isNicknameValid = uiState.isNicknameValid,
                    // modifier = Modifier.weight(1f), // weight 제거
                    onClickDuplicateCheck = { onEvent(NicknameEditUiEvent.OnClickDuplicateCheck) },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SupportingText(uiState.supportingTextType)
        }

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
            enabled = uiState.isDuplicateCheckDone
        ) {
            SaiText("완료", fontSize = 16.sp, fontWeight = FontWeight.W600)
        }
    }
}

@Composable
private fun TextFieldTrailingIcon(
    lengthText: String,
    isNicknameValid: Boolean,
    modifier: Modifier = Modifier,
    onClickDuplicateCheck: () -> Unit,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Spacer(Modifier.width(4.dp))
        SaiText(
            lengthText,
            color = Color(0xFFD3D5D9),
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        SaiText(
            "중복확인",
            Modifier
                .border(
                    1.dp,
                    if (isNicknameValid) SaiColor.Lime else SaiColor.Gray70,
                    RoundedCornerShape(4.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = onClickDuplicateCheck
                )

                .padding(vertical = 6.dp, horizontal = 10.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = (-0.41).sp,
            color = if (isNicknameValid) SaiColor.Lime else SaiColor.Gray70,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SupportingText(
    supportingTextType: SupportingTextType,
    modifier: Modifier = Modifier
) {
    when (supportingTextType) {
        SupportingTextType.NONE -> {}
        else -> {
            Row(modifier, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    supportingTextType.icon!!,
                    contentDescription = null,
                    modifier = modifier.size(14.dp),
                    tint = supportingTextType.color!!
                )
                SaiText(
                    text = supportingTextType.text!!,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    color = supportingTextType.color,
                    modifier = modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview(
    showBackground = true, backgroundColor = 0xFF202326,
    device = "spec:width=1080px,height=400px,dpi=440"
)
@Composable
private fun NicknameEditScreenPreviewTyped() {
    SaiTheme {
        NicknameEditScreenContent(
            uiState = NicknameEditUiState(nickname = "엘라123"),
            onEvent = {}
        )
    }
}

@Preview(
    showBackground = true, backgroundColor = 0xFF202326,
    device = "spec:width=1080px,height=400px,dpi=440"
)
@Composable
private fun NicknameEditScreenPreviewDuplicate() {
    SaiTheme {
        NicknameEditScreenContent(
            uiState = NicknameEditUiState(
                nickname = "엘라12",
                isNicknameValid = true,
                isDuplicateCheckDone = false,
                errorMessage = "이미 사용 중인 닉네임입니다.",
            ),
            onEvent = {}
        )
    }
}

@Preview(
    showBackground = true, backgroundColor = 0xFF202326,
    device = "spec:width=1080px,height=400px,dpi=440"
)
@Composable
private fun NicknameEditScreenPreviewAvailable() {
    SaiTheme {
        NicknameEditScreenContent(
            uiState = NicknameEditUiState(
                nickname = "새로운닉네임",
                isNicknameValid = false,
                isDuplicateCheckDone = true,
            ),
            onEvent = {}
        )
    }
}

