package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.ChallengeStatusBadge
import com.choius323.saisai.ui.component.TitleSection
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.Level
import com.choius323.saisai.ui.screen.course.ParticipantRewardInfo
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate

@Composable
fun CourseListItemVertical(
    imageUrl: String,
    courseName: String,
    distance: Double,
    participantCount: Int,
    level: Level,
    modifier: Modifier = Modifier,
    isEventActive: Boolean = false,
    reward: Int = 0,
    endDate: LocalDate? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
            .background(Color(0xFF2B2E31)),
    ) {
        // 상단 이미지 및 날짜 배지 섹션
        ImageSection(
            imageUrl = imageUrl,
            endDate = endDate,
            isEventActive = isEventActive,
            modifier = modifier
                .size(width = 200.dp, height = 160.dp),
        )

        // 하단 정보 섹션
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 18.dp)
        ) {
            TitleSection(courseName, distance, level)

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 10.dp)
            )

            // 현재 도전 인원 정보
            ParticipantRewardInfo(participantCount, reward, isLong = false)
        }
    }
}

@Composable
private fun ImageSection(
    imageUrl: Any,
    endDate: LocalDate?,
    isEventActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
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
}

@Preview(showBackground = true)
@Composable
fun CourseListItemPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleChallenge = CourseListItem.dummyItem4

    SaiTheme {
        Surface {
            sampleChallenge.apply {
                CourseListItemVertical(
                    imageUrl = imageUrl ?: "",
                    courseName = courseName,
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