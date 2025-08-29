package com.choius323.saisai.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

/**
 * 도전자와 완주자 수를 표시하는 컴포넌트
 */
@Composable
fun ParticipantRewardInfo(
    challengerCount: Int,
    reward: Int?,
    isLong: Boolean,
    iconSize: Dp = 15.dp,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 참가 정보
        Icon(
            Icons.Rounded.AccessibilityNew,
            contentDescription = "참가 아이콘",
            modifier = Modifier.size(iconSize),
            tint = Color(0xFF9783FF)
        )
        SaiText(
            text = "${challengerCount}명" + (if (isLong) " 참가" else ""),
            style = TextStyle(
                color = Color(0xFF9783FF),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
            )
        )
        if (reward != null && reward > 0) {
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = "리워드 아이콘",
                modifier = Modifier.size(iconSize),
                tint = Color(0xFF9783FF)
            )
            SaiText(
                text = DecimalFormat("#,###").format(reward),
                style = TextStyle(
                    color = Color(0xFF9783FF),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                )
            )
        }
    }
}