package com.choius323.saisai.ui.screen.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.ChallengeStatusBadge
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.Level
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate
import java.text.DecimalFormat

@Composable
fun CourseCardSimple(
    imageUrl: String,
    sigun: String,
    distance: Double,
    participantCount: Int,
    modifier: Modifier = Modifier,
    isEventActive: Boolean = false,
    reward: Int? = null,
    level: Level? = null,
    endDate: LocalDate? = null,
) {
    Card(
        modifier = modifier
            .size(width = 170.dp, height = 250.dp),
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

                if (endDate != null) {
                    ChallengeStatusBadge(
                        endDate = endDate,
                        isEvent = isEventActive,
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp)
                    )
                }
            }

            // 하단 정보 섹션
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 14.dp)
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
                        text = "${distance}km",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                    if (level != null) {
                        SaiText(
                            text = " · 난이도 ", style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                        SaiText(
                            text = level.displayText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = level.color,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 현재 도전 인원 정보
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_flash_on_24),
                        contentDescription = "도전 아이콘",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF8A2BE2)
                    )
                    SaiText(
                        text = "${participantCount}명",
                        style = TextStyle(
                            color = Color(0xFF8A2BE2),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                        )
                    )
                    if (reward != null) {
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Outlined.StarOutline,
                            contentDescription = "리워드 아이콘",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF8A2BE2)
                        )
                        SaiText(
                            text = DecimalFormat("#,###").format(reward),
                            style = TextStyle(
                                color = Color(0xFF8A2BE2),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseCardSimplePreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleChallenge = CourseListItem.dummyItem4

    SaiTheme {
        Surface {
            sampleChallenge.apply {
                CourseCardSimple(
                    imageUrl = imageUrl ?: "",
                    sigun = sigun,
                    distance = distance,
                    level = level,
                    participantCount = 671,
                    isEventActive = true,
                    reward = 1200,
                    endDate = LocalDate.now().plusDays(7L),
                    modifier = Modifier
                )
            }
        }
    }
}