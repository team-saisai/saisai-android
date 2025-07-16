package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.BadgeInfo
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
    Card(
        modifier = modifier
            .size(354.dp, 240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            SaiText("뱃지 모음집 1", fontSize = 16.sp)
            Spacer(Modifier.height(10.dp))
            FlowRow(
                modifier = Modifier.fillMaxSize(),
                maxLines = 2,
                maxItemsInEachRow = 4,
//                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(8) {
                    val badge = badges.getOrNull(it)
                    if (badge == null) {
                        Box(
                            Modifier
                                .weight(1f)
                                .size(width = 60.dp, height = 76.dp)
                        )
                    } else {
                        BadgeItem(
                            badgeInfo = badge,
                            modifier = Modifier
                                .weight(1f)
                                .size(width = 60.dp, height = 76.dp)
                        )
                    }
                }
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
    badgeInfo: BadgeInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 원형 뱃지 이미지
        AsyncImage(
            model = badgeInfo.imageUrl,
            contentDescription = "${badgeInfo.name} 뱃지123123",
            modifier = Modifier
                .size(54.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(4.dp))

        // 뱃지 이름
        SaiText(
            text = badgeInfo.name,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
        )
    }
}

// Compose Preview를 위한 코드
@Preview(showBackground = true)
@Composable
fun BadgeCollectionCardPreview() {
    val sampleBadges = List(7) { index ->
        BadgeInfo(
            id = index,
            name = "뱃지 이름",
            imageUrl = "https://placehold.co/100x100/e0e0e0/b0b0b0?text=B" // 플레이스홀더 이미지
        )
    }

    SaiTheme {
        Surface {
            BadgeCollectionCard(badges = sampleBadges)
        }
    }
}
