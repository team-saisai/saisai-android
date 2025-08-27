package com.choius323.saisai.ui.screen.my_page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.UserProfile
import com.choius323.saisai.ui.theme.SaiColor

@Composable
fun MyPageAchievementSection(
    userProfile: UserProfile,
    modifier: Modifier = Modifier,
    onEvent: (MyPageUiEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ) {
        // 코스 주행 기록
        AchievementItem(
            icon = R.drawable.ic_clock,
            "${userProfile.rideCount}",
            " 코스",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickRideCourses) })
        // 북마크 코스 목록
        AchievementItem(
            R.drawable.ic_bookmark,
            "${userProfile.bookmarkCount}",
            " 코스",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickBookmarkCourses) })
        // 리워드 목록
        AchievementItem(
            R.drawable.ic_point,
            "${userProfile.reward}",
            " P",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickRewardHistory) })
        // 뱃지 목록
        AchievementItem(
            R.drawable.ic_trophy,
            "${userProfile.badgeCount}",
            "/9 뱃지",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickBadgeAchievement) })
    }
}

@Composable
private fun AchievementItem(
    icon: Any?,
    value: String,
    type: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.Companion
                .width(44.dp)
                .height(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(icon, null)
        }
        Spacer(
            Modifier
                .heightIn(min = 16.dp)
                .weight(1f)
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

@Preview
@Composable
fun MyPageAchievementSectionPreview() {
    val userProfile = UserProfile.sample1
    MyPageAchievementSection(
        userProfile = userProfile,
        onEvent = {}
    )
}