package com.choius323.saisai.ui.screen.badge_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.UserBadgeDetail
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun BadgeDetailDialog(
    badgeDetail: UserBadgeDetail,
    modifier: Modifier = Modifier,
    closeBadgeDialog: () -> Unit,
) {
    Box(
        modifier
            .background(Color(0x804A4D50)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(SaiColor.Gray85)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(18.dp)
                    .clickable(onClick = closeBadgeDialog)
            )
            BadgeDetailDialogContent(badgeDetail, Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun BadgeDetailDialogContent(badgeDetail: UserBadgeDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            badgeDetail.imageUrl,
            contentDescription = "뱃지 이미지",
            modifier = Modifier
                .size(140.dp)
                .border(1.dp, SaiColor.Gray50, CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(12.dp))
        BadgeDetailDialogDescription(badgeDetail)
    }
}

@Composable
private fun BadgeDetailDialogDescription(
    badgeDetail: UserBadgeDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 28.5.dp, horizontal = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SaiText(
                badgeDetail.name,
                color = SaiColor.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(Modifier.height(7.dp))
            SaiText(
                "첫번째 코스 완주 시", color = SaiColor.Lime, fontSize = 14.sp, fontWeight = FontWeight.W400
            )
        }
        Spacer(Modifier.height(16.dp))
        SaiText(
            badgeDetail.description,
            color = SaiColor.Gray10,
            fontSize = 15.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun BadgeDetailDialogPreview() {
    SaiTheme {
        Surface {
            BadgeDetailDialog(
                badgeDetail = UserBadgeDetail.sample1,
                closeBadgeDialog = {}
            )
        }
    }
}