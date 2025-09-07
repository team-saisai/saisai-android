package com.choius323.saisai.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.theme.SaiTheme
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
    FlowRow(
        itemVerticalAlignment = Alignment.CenterVertically,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 참가 정보
        Row {
            IconText(
                imageVector = Icons.Rounded.AccessibilityNew,
                iconSize = iconSize,
                text = "${challengerCount}명" + (if (isLong) " 참가" else ""),
                contentDescription = "참가 아이콘",
            )
        }
        Row {
            if (reward != null && reward > 0) {
                IconText(
                    imageVector = Icons.Outlined.StarOutline,
                    iconSize = iconSize,
                    text = DecimalFormat("#,###").format(reward),
                    contentDescription = "리워드 아이콘",
                )
            }
        }
    }
}

@Composable
private fun IconText(
    imageVector: ImageVector,
    iconSize: Dp,
    text: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val color = remember { Color(0xFF9783FF) }
    Row(
        modifier.semantics { this.contentDescription = contentDescription },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = color
        )
        SaiText(
            text = text,
            color = color,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
        )
    }
}

@Preview
@Composable
fun ParticipantRewardInfoPreview() {
    SaiTheme {
        ParticipantRewardInfo(
            challengerCount = 10,
            reward = 1000,
            isLong = true,
            iconSize = 15.dp
        )
    }
}

@Preview
@Composable
fun ParticipantRewardInfoPreviewSmallWidth() {
    SaiTheme {
        Box(Modifier.width(100.dp)) {
            ParticipantRewardInfo(
                challengerCount = 10,
                reward = 1000,
                isLong = true,
                iconSize = 15.dp
            )
        }
    }
}