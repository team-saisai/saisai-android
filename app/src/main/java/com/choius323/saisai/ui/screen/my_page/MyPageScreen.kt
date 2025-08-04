package com.choius323.saisai.ui.screen.my_page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.UserProfile
import com.choius323.saisai.ui.theme.AppTitle
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.Typography
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = koinViewModel(),
    goProfile: () -> Unit,
    goBadgeAchievement: () -> Unit,
    goSetting: () -> Unit,
    goNotificationList: () -> Unit,
    goBookmarkCourses: () -> Unit,
    goRodeListCourse: () -> Unit,
    goRewardHistory: () -> Unit,
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MyPageSideEffect.ShowToast -> {
                context.SaiToast(sideEffect.message)
            }

            MyPageSideEffect.GoProfile -> goProfile()
            MyPageSideEffect.GoBadgeAchievement -> goBadgeAchievement()
            MyPageSideEffect.GoSetting -> goSetting()
            MyPageSideEffect.GoNotification -> goNotificationList()
            MyPageSideEffect.GoBookmarkCourses -> goBookmarkCourses()
            MyPageSideEffect.GoRodeListCourses -> goRodeListCourse()
            MyPageSideEffect.GoRewardHistory -> goRewardHistory()
        }
    }
    ProvideAppBar(
        navigationIcon = {
            SaiText("Mypage", style = Typography.AppTitle)
        },
        actions = {
            Icon(
                Icons.Outlined.Notifications, contentDescription = "Notifications",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = goNotificationList)
            )
        }
    )
    MyPageScreenContent(uiState, modifier, viewModel::onEvent)
    if (uiState.isLoading) {
        FullScreenLoading()
    }
}

@Composable
fun MyPageScreenContent(
    uiState: MyPageUiState,
    modifier: Modifier = Modifier,
    onEvent: (MyPageUiEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 25.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(36.dp))
        MyPageProfileSection(userProfile = uiState.userProfile, onClickProfile = {
            onEvent(
                MyPageUiEvent.OnClickProfile
            )
        })
        Spacer(Modifier.height(28.dp))
        MyPageAchievementSection(
            onEvent = onEvent,
            userProfile = uiState.userProfile,
            modifier = Modifier,
        )
        Spacer(Modifier.height(32.dp))
        MenuSection()
    }
}

@Composable
private fun MenuSection(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        HorizontalDivider(color = Color(0xFF31353A))
        MenuItem(text = "APP 설정") { /* TODO: Handle App Setting Click */ }
        HorizontalDivider(color = Color(0xFF31353A))
        MenuItem(text = "서비스 이용 약관") { /* TODO: Handle Terms of Service Click */ }
        HorizontalDivider(color = Color(0xFF31353A))
        MenuItem(text = "로그아웃") {
            coroutineScope.launch { SessionManager.onLogout() }
        }
        HorizontalDivider(color = Color(0xFF31353A))
    }
}

@Composable
private fun MenuItem(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 23.dp)
            .padding(start = 12.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SaiText(text = text, fontSize = 15.sp, color = SaiColor.White, fontWeight = FontWeight.W400)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
            contentDescription = "$text 메뉴",
            tint = SaiColor.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MyPageScreenContentPreview() {
    SaiTheme {
        Surface {
            MyPageScreenContent(MyPageUiState(userProfile = UserProfile.sample1)) {}
        }
    }
}