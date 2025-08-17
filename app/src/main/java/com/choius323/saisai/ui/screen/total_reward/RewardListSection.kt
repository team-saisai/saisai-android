package com.choius323.saisai.ui.screen.total_reward

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.EmptyCourseList
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.RewardInfo
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun RewardListSection(
    rewardInfoList: List<RewardInfo>,
    modifier: Modifier = Modifier,
    onClickEmptyButton: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (rewardInfoList.isEmpty()) {
            EmptyCourseList(
                content = "획득한 리워드가 없습니다.",
                modifier = Modifier.fillMaxSize(),
                onClick = onClickEmptyButton
            )
        }
        for ((index, item) in rewardInfoList.withIndex()) {
            RewardInfoItem(rewardInfo = item)
            if (index != rewardInfoList.lastIndex) {
                HorizontalDivider(color = SaiColor.Gray80)
            }
        }
    }
}

@Composable
private fun RewardInfoItem(rewardInfo: RewardInfo) {
    val dateFormatted = rewardInfo.acquiredAt.format(rewardDateFormat)
    val pointsFormatted = (if (rewardInfo.reward > 0) "+" else "") + rewardInfo.reward.toString()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            SaiText(
                text = dateFormatted,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = SaiColor.Gray60,
            )
            Spacer(modifier = Modifier.height(6.dp))
            SaiText(
                text = rewardInfo.courseName,
                fontSize = 15.sp,
                fontWeight = FontWeight.W400,
                color = SaiColor.Gray10,
            )
        }
        SaiText(
            text = pointsFormatted,
            fontSize = 15.sp,
            fontWeight = FontWeight.W400,
            color = SaiColor.White,
        )
    }
}

@Preview(heightDp = 300)
@Composable
private fun RewardListSectionPreviewEmpty() {
    SaiTheme {
        RewardListSection(emptyList()) { }
    }
}

@Preview
@Composable
private fun RewardListSectionPreview() {
    AndroidThreeTen.init(LocalContext.current)
    SaiTheme {
        RewardListSection(listOf(RewardInfo.sample1, RewardInfo.sample2)) { }
    }
}

private val rewardDateFormat = DateTimeFormatter.ofPattern("yyyy. MM. dd")