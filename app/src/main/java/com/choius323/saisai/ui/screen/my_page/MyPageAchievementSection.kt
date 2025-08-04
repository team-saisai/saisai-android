package com.choius323.saisai.ui.screen.my_page

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.UserProfile
import com.choius323.saisai.ui.theme.SaiColor
import java.text.DecimalFormat

@Composable
fun MyPageAchievementSection(
    userProfile: UserProfile,
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
            "${userProfile.rideCount}",
            " 코스",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickRideCourses) })
        AchievementItem(
            "${userProfile.bookmarkCount}",
            " 코스",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickBookmarkCourses) })
        AchievementItem(
            DecimalFormat("#,###").format(userProfile.reward),
            " P",
            modifier = Modifier.clickable { onEvent(MyPageUiEvent.OnClickRewardHistory) })
        AchievementItem(
            "${userProfile.badgeCount}",
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
            modifier = Modifier.Companion
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