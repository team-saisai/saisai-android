package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.UserBadge
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

/**
 * 뱃지 컬렉션을 보여주는 카드 컴포넌트
 * @param badgeList 표시할 뱃지 리스트
 */
@Composable
fun BadgeCollectionCard(
    badgeList: List<UserBadge>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.padding(vertical = 24.dp, horizontal = 14.dp)
    ) {
        for (y in 0..badgeList.size / 3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(26.dp),
            ) {
                for (x in 0..2) {
                    val index = y * 3 + x
                    if (index < badgeList.size) {
                        BadgeGridItem(badgeList[index])
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeGridItem(
    badge: UserBadge,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            badge.imageUrl ?: R.drawable.img_badge_lock,
            contentDescription = "뱃지 이미지",
            modifier = Modifier
                .widthIn(max = 80.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(8.dp))
        SaiText(
            badge.name,
            modifier = Modifier.widthIn(max = 85.dp),
            color = SaiColor.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BadgeCollectionCardPreview() {
    val sampleBadges = listOf(
        UserBadge.sample1, UserBadge.sample2, UserBadge.sample3,
        UserBadge.sample4
    )

    SaiTheme {
        Surface {
            BadgeCollectionCard(badgeList = sampleBadges)
        }
    }
}
