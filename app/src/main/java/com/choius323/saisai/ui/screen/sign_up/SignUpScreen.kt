package com.choius323.saisai.ui.screen.sign_up

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.util.SAISAI_TERM_OF_SERVICE
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel(),
    goHome: () -> Unit,
    goBack: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SignUpSideEffect.GoBack -> goBack()
            is SignUpSideEffect.ShowToast -> context.SaiToast(sideEffect.message)
            SignUpSideEffect.GoHome -> goHome()
            SignUpSideEffect.GoTermOfServiceDetail -> {
                val intent = Intent(Intent.ACTION_VIEW, SAISAI_TERM_OF_SERVICE.toUri())
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    context.SaiToast("웹 페이지를 열 수 있는 앱이 설치되어 있지 않습니다.")
                } catch (e: Exception) {
                    context.SaiToast("알 수 없는 오류가 발생했습니다.")
                }
            }
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
        title = { SaiText("회원가입", fontSize = 18.sp) }
    )
    SignUpScreenContent(uiState, modifier, viewModel::onEvent)
}

@Composable
private fun SignUpScreenContent(
    uiState: SignUpUiState,
    modifier: Modifier = Modifier,
    onEvent: (SignUpUiEvent) -> Unit,
) {
    Column(
        modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(Modifier.padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (uiState.isAllChecked) Icons.Default.CheckCircle else Icons.Default.Check,
                "체크",
                Modifier.clickable { onEvent(SignUpUiEvent.OnClickAllCheckBox) }
            )
            Spacer(Modifier.width(6.dp))
            SaiText("서비스 약관 전체 동의")
        }
        HorizontalDivider(color = Color(0xFF34383C))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CheckItem(
                uiState.isCheckedTermOfService,
                "[필수] 이용약관 및 개인정보 처리방침",
                Modifier,
                onClickDetail = { onEvent(SignUpUiEvent.OnClickTermOfServiceDetail) },
                onCheckedChange = { onEvent(SignUpUiEvent.OnClickTermOfServiceCheckBox) }
            )
            CheckItem(
                uiState.isCheckedAge,
                "[필수] 만 14세 이상입니다.",
                Modifier,
                onCheckedChange = { onEvent(SignUpUiEvent.OnClickAgeCheckBox) }
            )
        }
        Spacer(Modifier.weight(1f))
        SaiText(
            "회원가입",
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(if (uiState.isAllChecked) SaiColor.Lime else SaiColor.Gray80)
                .padding(horizontal = 14.dp, vertical = 18.dp)
                .clickable(uiState.isAllChecked) { onEvent(SignUpUiEvent.OnClickSignUpButton) },
            color = if (uiState.isAllChecked) SaiColor.Gray90 else SaiColor.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(heightDp = 400)
@Composable
private fun SignUpScreenContentPreview() {
    SaiTheme {
        Surface {
            SignUpScreenContent(
                uiState = SignUpUiState(
                    isCheckedTermOfService = true,
                    isCheckedAge = false,
                ),
                modifier = Modifier.fillMaxSize(),
                onEvent = {}
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
private fun SignUpScreenContentPreviewAllChecked() {
    SaiTheme {
        Surface {
            SignUpScreenContent(
                uiState = SignUpUiState(
                    isCheckedTermOfService = true,
                    isCheckedAge = true,
                ),
                modifier = Modifier.fillMaxSize(),
                onEvent = {}
            )
        }
    }
}

@Composable
private fun CheckItem(
    isChecked: Boolean,
    content: String,
    modifier: Modifier = Modifier,
    onClickDetail: (() -> Unit)? = null,
    onCheckedChange: () -> Unit,
) {
    Row(
        modifier.padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (isChecked) Icons.Default.CheckCircle else Icons.Default.Check,
            "체크",
            Modifier.clickable(onClick = onCheckedChange)
        )
        SaiText(content, fontSize = 14.sp, color = Color(0xFFD3D5D9))
        Spacer(Modifier.weight(1f))
        if (onClickDetail != null) {
            SaiText(
                "자세히",
                Modifier.clickable { onClickDetail() },
                fontSize = 11.sp,
                textDecoration = TextDecoration.Underline,
                color = Color(0xFF878C90),
            )
        }
    }
}

@Preview
@Composable
private fun CheckItemPreview() {
    SaiTheme {
        Surface {
            CheckItem(
                isChecked = true,
                content = "개인정보 수집 및 이용 동의",
                onClickDetail = {},
                onCheckedChange = {}
            )
        }
    }
}

@Preview
@Composable
private fun CheckItemPreviewWithoutDetail() {
    SaiTheme {
        CheckItem(
            isChecked = false,
            content = "개인정보 수집 및 이용 동의",
            onClickDetail = null,
            onCheckedChange = {}
        )
    }
}

