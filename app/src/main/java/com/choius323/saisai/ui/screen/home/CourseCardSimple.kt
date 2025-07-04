package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.LocalFireDepartment
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.theme.SaiTheme
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun CourseCardSimple(
    imageUrl: String,
    sigun: String,
    distance: Double,
    level: Int,
    participantCount: Int,
    modifier: Modifier = Modifier,
    endDate: LocalDateTime? = null,
) {
    Card(
        modifier = modifier
            .size(width = 170.dp, height = 240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // 상단 이미지 및 날짜 배지 섹션
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                // 배경 지도 이미지
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "챌린지 경로 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // 날짜 정보 배지
                if (endDate != null) {
                    DateBadge(
                        date = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                    )
                }
            }

            // 하단 정보 섹션
            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Row {
                    SaiText(
                        text = sigun,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = null,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SaiText(
                        text = "${distance}km · 난이도 ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                    SaiText(
                        text = level.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 현재 도전 인원 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FlashOn,
                        contentDescription = "도전 아이콘",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF8A2BE2)
                    )
                    SaiText(
                        text = "${participantCount}명 도전 중",
                        fontSize = 12.sp,
                        color = Color(0xFF8A2BE2)
                    )
                }
            }
        }
    }
}

@Composable
private fun DateBadge(
    date: String, modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFCDE6666))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.LocalFireDepartment,
            contentDescription = "기간 아이콘",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        SaiText(
            text = "~ $date",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CourseCardSimplePreview() {
    // 샘플 데이터
    val sampleChallenge = CourseListItem(
        courseId = 234,
        imageUrl = "https://placehold.co/600x400/2c2c2c/e0e0e0?text=Map+Image",
        courseName = "여의도 - 반포",
        summary = "dapibus",
        level = 2,
        distance = 8.6,
        estimatedTime = 50.0,
        sigun = "서울시 강남구",
        challengeInfo = null,
    )

    SaiTheme {
        Surface {
            sampleChallenge.apply {
                CourseCardSimple(
                    imageUrl = imageUrl ?: "",
                    sigun = sigun,
                    distance = distance,
                    level = level,
                    participantCount = 671,
                    endDate = LocalDateTime.now().plusDays(7L),
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DateBadgePreview() {
    SaiTheme {
        Surface {
            DateBadge(date = "6/31")
        }
    }
}