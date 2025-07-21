package com.choius323.saisai.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.ui.component.ProvideAppBar
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.AppTitle
import com.choius323.saisai.ui.theme.SaiTheme
import com.choius323.saisai.ui.theme.Typography
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun MyPageScreen(modifier: Modifier = Modifier) {
    ProvideAppBar(
        navigationIcon = {
            SaiText("Mypage", style = Typography.AppTitle)
        },
        actions = {
            Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
        }
    )
    MyPageScreenContent(modifier)
}

@Composable
fun MyPageScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(36.dp))
        ProfileSection(nickname = "델라", email = "saisai@naver.com")
        Spacer(Modifier.height(36.dp))
        AchievementSection()
        Spacer(Modifier.height(32.dp))
        MenuSection()
    }
}

@Composable
private fun ProfileSection(nickname: String, email: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(106.dp)
                .clip(CircleShape)
                .border(1.4.dp, Color(0xFF51565B), CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_user_mono),
                contentDescription = "Profile Image",
                modifier = Modifier.size(44.dp),
                tint = Color(0xFF34383C)
            )
        }
        Spacer(Modifier.height(22.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* 프로필 수정 화면으로 이동 */ }
        ) {
            SaiText(text = nickname, fontSize = 24.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.size(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = "Edit Profile",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }

        Spacer(Modifier.height(8.dp))
        SaiText(
            text = email,
            fontSize = 14.sp,
            color = Color(0xFF9BA0A5),
            fontWeight = FontWeight.W300
        )
    }
}

@Composable
private fun AchievementSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ) {
        AchievementItem("132 ", "코스")
        AchievementItem("24 ", "코스")
        AchievementItem("${DecimalFormat("#,###").format(1280)} ", "P")
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
                .border(width = 1.dp, color = Color(0xFF51565B), shape = CircleShape)
        )
        SaiText(
            text = buildAnnotatedString {
                withStyle(SpanStyle(Color(0xFFC9FF66))) {
                    append("$value ")
                }
                append(type)
            }, fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color.White
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
        SaiText(text = text, fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.W400)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
            contentDescription = "$text 메뉴",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MyPageScreenContentPreview() {
    SaiTheme {
        MyPageScreenContent()
    }
}