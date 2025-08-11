package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.model.BadgeInfo
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

/**
 * 뱃지 컬렉션을 최대 8개까지 보여주는 카드 컴포넌트
 * @param badges 표시할 뱃지 리스트
 */
@Composable
fun BadgeCollectionCard(
    badges: List<BadgeInfo>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2B2E31))
            .padding(20.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            maxItemsInEachRow = 4,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            repeat(8) {
                BadgeItem(
                    badgeInfo = badges.getOrNull(it),
                    modifier = Modifier
                        .size(60.dp)
                        .weight(1f, false)
                )
            }
        }
    }
}

/**
 * 개별 뱃지를 표시하는 컴포넌트 (이미지 + 이름)
 * @param badgeInfo 표시할 뱃지 데이터
 */
@Composable
private fun BadgeItem(
    badgeInfo: BadgeInfo?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = badgeInfo?.imageUrl,
        contentDescription = "${badgeInfo?.name} 뱃지",
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .border(1.dp, SaiColor.Gray70, CircleShape),
        contentScale = ContentScale.Crop,
    )
}

// Compose Preview를 위한 코드
@Preview(showBackground = true)
@Composable
fun BadgeCollectionCardPreview() {
    val sampleBadges = List(7) { index ->
        BadgeInfo(
            id = index,
            name = "뱃지 이름",
            imageUrl = "",
        )
    }

    SaiTheme {
        Surface {
            BadgeCollectionCard(badges = sampleBadges)
        }
    }
}
