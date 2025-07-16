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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseInfo
import com.choius323.saisai.util.DateTimeFormat
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate

/**
 * 새로운 UI 디자인이 적용된 챌린지 카드 컴포넌트
 * @param courseInfo 표시할 챌린지 데이터
 */
@Composable
fun ThemedChallengeCard(
    courseInfo: CourseInfo,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // 상단 이미지 및 상태 배지
            ChallengeImageHeader(
                imageUrl = courseInfo.imageUrl,
                endDate = courseInfo.endDate,
            )

            // 하단 상세 정보 섹션
            ChallengeDetails(courseInfo = courseInfo)
        }
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
 * 챌린지 진행 상태에 따라 다른 배지를 보여주는 컴포넌트
 */
@Composable
fun ChallengeStatusBadge(
    endDate: LocalDate,
    modifier: Modifier = Modifier,
) {
    val isFinished = remember(endDate) { endDate.isBefore(LocalDate.now()) }
    if (isFinished) {
        EndedBadge(modifier)
    } else {
        DateBadge(endDate = endDate, modifier = modifier)
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

        Spacer(modifier = Modifier.height(12.dp))

        // 테마 태그
        ThemeTags(themes = courseInfo.themes)

        Spacer(modifier = Modifier.height(16.dp))

        // 도전자 및 완주자 정보
        ParticipantInfo(
            challengerCount = courseInfo.challengerCount,
            completedCount = courseInfo.completedCount
        )
    }
}

/**
 * 진행 중인 챌린지의 종료 날짜를 표시하는 배지
 */
@Composable
fun DateBadge(endDate: LocalDate, modifier: Modifier = Modifier) {
    val endDateStr = remember(endDate) { endDate.format(DateTimeFormat.monthDay) }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFCDE6666))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.LocalFireDepartment,
            contentDescription = "기간 아이콘",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        SaiText(
            text = "~$endDateStr",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        )
    }
}

/**
 * 종료된 챌린지를 표시하는 배지
 */
@Composable
private fun EndedBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Gray.copy(alpha = 0.9f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        SaiText(
            text = "챌린지 종료",
            style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
    }
}

/**
 * 챌린지 테마 태그들을 표시하는 컴포넌트
 */
@Composable
fun ThemeTags(themes: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        themes.forEach { theme ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                SaiText(
                    text = theme,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

/**
 * 도전자와 완주자 수를 표시하는 컴포넌트
 */
@Composable
fun ParticipantInfo(challengerCount: Int, completedCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 도전자 정보
        Icon(
            imageVector = Icons.Default.FlashOn,
            contentDescription = "도전 아이콘",
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF8A2BE2)
        )
        Spacer(modifier = Modifier.width(4.dp))
        SaiText(
            text = "${challengerCount}명 도전 중",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
        )

        Spacer(modifier = Modifier.width(6.dp))

        // 완주자 정보
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "완주 아이콘",
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF8A2BE2)
        )
        Spacer(modifier = Modifier.width(4.dp))
        SaiText(
            text = "${completedCount}명 완주",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
        )
    }
}

@Preview(showBackground = true, name = "진행 중인 챌린지")
@Composable
fun ThemedChallengeCardOngoingPreview() {
    val sampleChallenge = CourseInfo.dummyItem
    AndroidThreeTen.init(LocalContext.current)
    MaterialTheme {
        ThemedChallengeCard(courseInfo = sampleChallenge)
    }
}

@Preview(showBackground = true, name = "종료된 챌린지")
@Composable
fun ThemedChallengeCardFinishedPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleChallenge = CourseInfo.dummyItem.copy(endDate = LocalDate.now().minusDays(1))
    MaterialTheme {
        ThemedChallengeCard(courseInfo = sampleChallenge)
    }
}