package com.choius323.saisai.ui.screen.my_page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.component.SaiToast
import com.choius323.saisai.ui.theme.AppTitle
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.Typography
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.text.DecimalFormat

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = viewModel(),
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
        ProfileSection(nickname = uiState.name, email = uiState.email, onClickProfile = {
            onEvent(
                MyPageUiEvent.OnClickProfile
            )
        })
        Spacer(Modifier.height(28.dp))
        AchievementSection(
            onEvent = onEvent,
            rideCount = uiState.rideCount,
            bookmarkCount = uiState.bookmarkCount,
            rewardPoint = uiState.rewardPoint,
            badgeCount = uiState.badgeCount,
            modifier = Modifier
        )
        Spacer(Modifier.height(32.dp))
        MenuSection()
    }
}

@Composable
private fun ProfileSection(
    nickname: String,
    email: String,
    modifier: Modifier = Modifier,
    onClickProfile: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(106.dp)
                .clip(CircleShape)
                .border(1.4.dp, SaiColor.Gray70, CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Person,
                contentDescription = "Profile Image",
                modifier = Modifier.size(44.dp),
                tint = SaiColor.Gray80
            )
        }
        Spacer(Modifier.height(22.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onClickProfile)
        ) {
            SaiText(text = nickname, fontSize = 24.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.size(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = "Edit Profile",
                modifier = Modifier.size(24.dp),
                tint = SaiColor.White
            )
        }

        Spacer(Modifier.height(8.dp))
        SaiText(
            text = email,
            fontSize = 14.sp,
            color = SaiColor.Gray40,
            fontWeight = FontWeight.W300
        )
    }
}

@Composable
private fun AchievementSection(
    rideCount: Int,
    bookmarkCount: Int,
    rewardPoint: Int,
    badgeCount: Int,
    modifier: Modifier = Modifier,
    onEvent: (MyPageUiEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ) {
        AchievementItem(
            "$rideCount ",
            "코스",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickRideCourses) })
        AchievementItem(
            "$bookmarkCount ",
            "코스",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickBookmarkCourses) })
        AchievementItem(
            "${DecimalFormat("#,###").format(rewardPoint)} ",
            "P",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickRewardHistory) })
        AchievementItem(
            "$badgeCount",
            " / 9",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickBadgeAchievement) })
    }
}

@Composable
private fun AchievementItem(value: String, type: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .border(width = 1.dp, color = SaiColor.Gray70, shape = CircleShape)
        )
        SaiText(
            text = buildAnnotatedString {
                withStyle(SpanStyle(SaiColor.Lime)) {
                    append("$value ")
                }
                append(type)
            }, fontSize = 14.sp, fontWeight = FontWeight.Normal, color = SaiColor.White
        )
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
            MyPageScreenContent(MyPageUiState(name = "사용자")) {}
        }
    }
}