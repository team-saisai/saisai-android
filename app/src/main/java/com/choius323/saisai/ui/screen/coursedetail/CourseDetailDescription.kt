package com.choius323.saisai.ui.screen.coursedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.model.GpxPoint
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun CourseDetailDescription(
    detail: CourseDetail,
    modifier: Modifier = Modifier,
    onChallengeClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(18.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier) {
                CourseInfoSection(detail)
                Spacer(modifier = Modifier.height(6.dp))
                ThemeTagsSection(listOf("테마1", "테마2"))
                Spacer(modifier = Modifier.height(18.dp))
                ParticipantInfoSection(detail.inProgressUserCount, detail.completeUserCount)
            }
            Spacer(modifier = Modifier.weight(1f))

            // 우측 '도전하기' 버튼 영역
            ChallengeButton(
                modifier = Modifier
                    .align(Alignment.Bottom)
            )
        }
    }
}

@Composable
private fun CourseInfoSection(detail: CourseDetail) {
    Column {
        SaiText(
            text = detail.courseName, fontSize = 20.sp, fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        SaiText(
            text = "${detail.distance}km · 난이도 ${detail.level}",
            fontSize = 12.sp,
            color = Color.LightGray
        )
    }
}

@Composable
private fun ThemeTagsSection(themes: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        themes.forEach { theme ->
            Surface(
                modifier = Modifier
                // .padding(horizontal = 10.dp, vertical = 5.dp)
                ,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 2.dp
            ) {
                SaiText(
                    text = theme,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
private fun ParticipantInfoSection(challengerCount: Int, completedCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.FlashOn,
            contentDescription = "도전자 아이콘",
            tint = Color(0xFF8032FD),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        SaiText(
            text = "$challengerCount 도전중",
            fontSize = 14.sp,
            color = Color(0xFF8032FD),
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Default.CheckCircleOutline,
            contentDescription = "완주자 아이콘",
            tint = Color(0xFF8032FD),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        SaiText(
            text = "$completedCount 완주",
            fontSize = 14.sp,
            color = Color(0xFF8032FD),
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ChallengeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFC9FF66))
            .padding(vertical = 12.dp, horizontal = 5.5.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.DirectionsBike,
            contentDescription = "도전하기 아이콘",
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        SaiText(
            text = "도전하기", style = MaterialTheme.typography.labelMedium.copy(
                color = Color.Black, fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CourseDetailDescriptionPreview() {
    val sampleDetail = CourseDetail(
        courseId = 236,
        courseName = "올림픽대로코스",
        summary = "conubia",
        level = 4193,
        distance = 4.5,
        estimatedTime = 25.3,
        sigun = "sapien",
        imageUrl = "",
        inProgressUserCount = 9838,
        completeUserCount = 8441,
        gpxPointList = GpxPoint.gpxPointsSample.take(5)
    )
    SaiTheme() {
        Surface(
            Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            CourseDetailDescription(
                detail = sampleDetail,
                onChallengeClick = {},
            )
        }
    }
}