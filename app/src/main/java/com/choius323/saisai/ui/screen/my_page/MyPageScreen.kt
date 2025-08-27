package com.choius323.saisai.ui.screen.my_page

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.choius323.saisai.BuildConfig
import com.choius323.saisai.ui.component.FullScreenLoading
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.model.UserProfile
import com.choius323.saisai.ui.theme.AppTitle
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.Typography
import com.choius323.saisai.util.SAISAI_TERM_OF_SERVICE
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
    goRideHistoryCourse: () -> Unit,
    goRewardHistory: () -> Unit,
    goTermsOfService: () -> Unit,
    goNicknameEdit: (String) -> Unit,
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
            MyPageSideEffect.GoRodeListCourses -> goRideHistoryCourse()
            MyPageSideEffect.GoRewardHistory -> goRewardHistory()
            is MyPageSideEffect.GoNicknameEdit -> goNicknameEdit(sideEffect.nickname)
            MyPageSideEffect.GoTermsOfService -> {
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
            SaiText("Mypage", style = Typography.AppTitle)
        },
//        actions = {
//            Icon(
//                Icons.Outlined.Notifications, contentDescription = "Notifications",
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable(onClick = goNotificationList)
//            )
//        }
    )
    LaunchedEffect(Unit) {
        viewModel.onEvent(MyPageUiEvent.InitScreen)
    }
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
        MyPageProfileSection(
            userProfile = uiState.userProfile,
            onClickProfile = { onEvent(MyPageUiEvent.OnClickProfile) },
            onClickNickname = { onEvent(MyPageUiEvent.OnClickNicknameEdit) })
        Spacer(Modifier.height(28.dp))
        MyPageAchievementSection(
            onEvent = onEvent,
            userProfile = uiState.userProfile,
            modifier = Modifier,
        )
        Spacer(Modifier.height(32.dp))
        MenuSection(
            onClickAppSettings = { onEvent(MyPageUiEvent.OnClickAppSettings) },
            onClickTermsOfService = { onEvent(MyPageUiEvent.OnClickTermsOfService) }
        )
    }
}

@Composable
private fun MenuSection(
    modifier: Modifier = Modifier,
    onClickAppSettings: () -> Unit,
    onClickTermsOfService: () -> Unit,
) {
    Dp.Unspecified
    Column(modifier = modifier) {
        HorizontalDivider(color = Color(0xFF31353A))
        MenuItem(text = "APP 설정", onClick = onClickAppSettings)
        HorizontalDivider(color = Color(0xFF31353A))
        MenuItem(text = "서비스 이용 약관", onClick = onClickTermsOfService)
        HorizontalDivider(color = Color(0xFF31353A))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 23.dp)
                .padding(start = 12.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SaiText("버전 정보", fontSize = 15.sp, fontWeight = FontWeight.W400)
            SaiText(
                "ver ${BuildConfig.VERSION_NAME}",
                color = SaiColor.Gray40,
                fontSize = 15.sp,
                fontWeight = FontWeight.W400
            )
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