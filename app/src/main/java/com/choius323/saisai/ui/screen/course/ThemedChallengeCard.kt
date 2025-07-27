package com.choius323.saisai.ui.screen.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.choius323.saisai.ui.model.CourseInfo
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate
import java.text.DecimalFormat

/**
 * 새로운 UI 디자인이 적용된 챌린지 카드 컴포넌트
 * @param courseInfo 표시할 챌린지 데이터
 */
@Composable
fun ThemedChallengeCard(
    courseInfo: CourseInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2B2E31))
    ) {
        // 상단 이미지 및 상태 배지
        ChallengeImageHeader(
            imageUrl = courseInfo.imageUrl,
            endDate = courseInfo.endDate,
        )

        // 하단 상세 정보 섹션
        ChallengeDetails(courseInfo = courseInfo)
    }
}

/**
 * 카드 상단의 이미지와 상태 배지를 표시하는 컴포넌트
 */
@Composable
private fun ChallengeImageHeader(imageUrl: String, endDate: LocalDate) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(200.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "챌린지 경로 이미지",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        ChallengeStatusBadge(endDate = endDate)
    }
}

/**
 * 카드 하단의 상세 정보를 표시하는 컴포넌트
 */
@Composable
private fun ChallengeDetails(courseInfo: CourseInfo) {
    Column(modifier = Modifier.padding(16.dp)) {
        // 거리 및 난이도
        Row {
            SaiText(
                text = "${courseInfo.distance} · 난이도 ${courseInfo.difficulty}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        // 테마 태그
        Spacer(modifier = Modifier.height(16.dp))

        // 도전자 및 완주자 정보
        ParticipantRewardInfo(
            challengerCount = courseInfo.challengerCount,
            reward = courseInfo.completedCount,
            isLong = false
        )
    }
}

/**
 * 챌린지 테마 태그들을 표시하는 컴포넌트
 */
@Composable
fun ThemeTags(themes: List<String>, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = modifier) {
        themes.forEach { theme ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF34383C))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                SaiText(
                    text = theme,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

/**
 * 도전자와 완주자 수를 표시하는 컴포넌트
 */
@Composable
fun ParticipantRewardInfo(challengerCount: Int, reward: Int?, isLong: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 도전자 정보
        Icon(
            painter = painterResource(R.drawable.outline_flash_on_24),
            contentDescription = "도전 아이콘",
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF8A2BE2)
        )
        SaiText(
            text = "${challengerCount}명" + (if (isLong) " 도전중" else ""),
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

@Preview(showBackground = true, name = "진행 중인 챌린지")
@Composable
fun ThemedChallengeCardOngoingPreview() {
    val sampleChallenge = CourseInfo.dummyItem
    AndroidThreeTen.init(LocalContext.current)
    SaiTheme {
        Surface {
            ThemedChallengeCard(courseInfo = sampleChallenge)
        }
    }
}

@Preview(showBackground = true, name = "종료된 챌린지")
@Composable
fun ThemedChallengeCardFinishedPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleChallenge = CourseInfo.dummyItem.copy(endDate = LocalDate.now().minusDays(1))
    SaiTheme {
        Surface {
            ThemedChallengeCard(courseInfo = sampleChallenge)
        }
    }
}