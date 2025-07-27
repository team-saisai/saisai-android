package com.choius323.saisai.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.model.Level
import com.choius323.saisai.ui.screen.course.ParticipantRewardInfo
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDate

@Composable
fun CourseListItemHorizontal(
    modifier: Modifier = Modifier,
    course: CourseListItem,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape.copy(CornerSize(16.dp)))
            .background(Color(0xFF2B2E31))
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageSection(
            course.imageUrl,
            course.challengeEndedAt,
            course.isEventActive,
            Modifier.size(width = 160.dp, height = 132.dp)
        )
        Spacer(Modifier.width(4.dp))
        CourseInformationSection(
            courseName = course.courseName,
            distance = course.distance,
            level = course.level,
            participantCount = course.courseChallengerCount,
            reward = course.reward,
            isLong = true,
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp, bottom = 18.dp, end = 10.dp),
        )
    }
}


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
    endDate: LocalDate,
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
        CourseInformationSection(
            courseName = courseName,
            distance = distance,
            level = level,
            participantCount = participantCount,
            reward = reward,
            isLong = false,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 18.dp),
        )
    }
}

@Composable
private fun ImageSection(
    imageUrl: Any?,
    endDate: LocalDate,
    isEventActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "코스 이미지",
            modifier = Modifier,
            contentScale = ContentScale.Crop
        )
        ChallengeStatusBadge(
            endDate = endDate,
            isEvent = isEventActive,
            modifier = Modifier.padding(6.dp)
        )
    }
}

@Composable
private fun TitleSection(
    courseName: String,
    distance: Double,
    level: Level,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        Column(Modifier.weight(1f)) {
            SaiText(
                text = courseName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            SaiText(
                buildAnnotatedString {
                    append("${distance}km · 난이도 ")
                    withStyle(SpanStyle(color = level.color)) {
                        append(level.displayText)
                    }
                },
                fontSize = 12.sp,
                color = SaiColor.Gray40,
            )
        }
        Icon(
            Icons.Outlined.BookmarkBorder,
            contentDescription = "북마크",
            modifier = Modifier
                .size(26.dp)
                .clickable {/*TODO: 북마크*/ },
        )
    }
}

@Composable
private fun CourseInformationSection(
    courseName: String,
    distance: Double,
    level: Level,
    participantCount: Int,
    reward: Int,
    isLong: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        TitleSection(courseName, distance, level)

        Spacer(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 10.dp)
        )

        // 현재 도전 인원 정보
        ParticipantRewardInfo(participantCount, reward, isLong)
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
                        challengeEndedAt = LocalDate.now().minusDays(7),
                        isEventActive = false
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