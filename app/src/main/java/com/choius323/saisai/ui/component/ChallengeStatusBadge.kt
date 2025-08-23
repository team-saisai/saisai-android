package com.choius323.saisai.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.util.DateTimeFormat
import org.threeten.bp.LocalDate

/**
 * 챌린지 진행 상태에 따라 다른 배지를 보여주는 컴포넌트
 */
@Composable
fun ChallengeStatusBadge(
    endDate: LocalDate?,
    modifier: Modifier = Modifier,
    isEvent: Boolean = false,
    isColor: Boolean = true,
) {
    val isFinished = remember(endDate) { endDate?.isBefore(LocalDate.now()) }
    Row(
        modifier
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isFinished == true) {
            EndedBadge(Modifier)
        } else if (isFinished == false && endDate != null) {
            DateBadge(endDate = endDate, modifier = Modifier, isColor = isColor)
        }
        if (isEvent) {
            EventBadge(Modifier.fillMaxHeight(), isColor)
        }
    }
}

/**
 * 진행 중인 챌린지의 종료 날짜를 표시하는 배지
 */
@Composable
private fun DateBadge(endDate: LocalDate, modifier: Modifier = Modifier, isColor: Boolean = true) {
    val endDateStr = remember(endDate) { endDate.format(DateTimeFormat.monthDay) }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isColor) Color(0xFCDE6666) else SaiColor.Gray60)
            .padding(start = 4.dp, top = 3.dp, bottom = 3.dp, end = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.LocalFireDepartment,
            contentDescription = "기간 아이콘",
            tint = SaiColor.White,
            modifier = Modifier.Companion.size(20.dp)
        )
        SaiText(
            text = "~$endDateStr",
            style = TextStyle(
                color = SaiColor.White,
                fontWeight = FontWeight.Companion.Medium,
                fontSize = 12.sp
            )
        )
    }
}

/**
 * 종료된 챌린지를 표시하는 배지
 */
@Composable
private fun EndedBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .background(Color(0xFF37393C))
            .padding(horizontal = 9.dp, vertical = 6.dp),
    ) {
        SaiText(
            text = "챌린지 종료",
            style = TextStyle(color = SaiColor.White, fontSize = 12.sp),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun EventBadge(modifier: Modifier = Modifier, isColor: Boolean = true) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isColor) SaiColor.LightPurple else SaiColor.Gray60)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SaiText(
            text = "이벤트",
            style = TextStyle(
                color = SaiColor.White,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        )
    }
}