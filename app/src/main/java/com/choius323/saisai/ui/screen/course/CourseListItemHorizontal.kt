package com.choius323.saisai.ui.screen.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun CourseListItemHorizontal(
    modifier: Modifier = Modifier,
    course: CourseListItem,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. 왼쪽 이미지 및 뱃지
            Box(
                modifier = Modifier.fillMaxHeight()
            ) {
                AsyncImage(
                    model = course.imageUrl,
                    contentDescription = "${course.courseName} 이미지",
                    modifier = Modifier
                        .size(width = 170.dp, height = 140.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
                ChallengeStatusBadge(course.challengeEndedAt)
            }

            Spacer(Modifier.width(16.dp))
            // 2. 오른쪽 정보 영역
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                SaiText(
                    text = course.courseName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                // 거리 및 난이도
                SaiText(
                    text = "${course.distance}km · 난이도 ${course.level}", // level을 "하/중/상"으로 변환하는 로직 추가 가능
                    fontSize = 12.sp,
                    color = Color(155, 160, 165)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 테마 태그
                ThemeTags(listOf("테마1", "테마2"))

                Spacer(modifier = Modifier.height(6.dp))

                // 도전자 및 완주자 정보
                ParticipantInfo(
                    challengerCount = course.courseChallengerCount,
                    completedCount = course.courseFinisherCount,
                )
            }
        }
    }
}

@Composable
private fun ChallengeStatusBadge(endTime: LocalDate?) {
    if (endTime == null) return
    val isFinished = endTime.isBefore(LocalDate.now())
    val badgeText = if (isFinished) "챌린지 종료" else "~${
        endTime.format(
            DateTimeFormatter.ofPattern("M/d")
        )
    }"
    val badgeColor =
        if (isFinished) Color.Gray.copy(alpha = 0.9f) else Color(0xFFF08080).copy(alpha = 0.9f)

    Row(
        modifier = Modifier
            .padding(5.dp)
            .height(26.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(badgeColor)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (!isFinished) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "기간 아이콘",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        SaiText(
            text = badgeText, color = Color.White, fontSize = 12.sp
        )
    }
}

@Preview
@Composable
fun CourseListItemHorizontalPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val course = CourseListItem.dummyItem1
    SaiTheme {
        Surface {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CourseListItemHorizontal(course = course)
                CourseListItemHorizontal(
                    course = course.copy(
                        challengeEndedAt = LocalDate.now().minusDays(7)
                    )
                )
                CourseListItemHorizontal(
                    course = course.copy(
                        challengeEndedAt = LocalDate.now().plusDays(7)
                    )
                )
            }
        }
    }
}
