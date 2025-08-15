package com.choius323.saisai.ui.screen.settings

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiSwitch
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
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
            SaiText("APP 설정", fontSize = 18.sp)
        }
    )
    SettingsScreenContent(uiState, modifier, viewModel::onEvent)
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
        MenuText("로그아웃", modifier = Modifier.fillMaxWidth().clickable { onEvent(SettingsUiEvent.OnClickLogOut) })
        HorizontalDivider(color = Color(0xFF31353A))
        MenuText(
            "회원탈퇴",
            modifier = Modifier.fillMaxWidth().clickable { onEvent(SettingsUiEvent.OnClickDeleteAccount) }
        )
    }
}

@Composable
private fun MenuText(text: String, modifier: Modifier = Modifier) {
    SaiText(
        text,
        modifier
            .padding(vertical = 21.dp, horizontal = 8.dp),
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
        onEvent = {}
    )
}
