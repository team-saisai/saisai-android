package com.choius323.saisai.ui.component

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
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
import com.choius323.saisai.ui.screen.course.ParticipantRewardInfo
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
            .clip(CircleShape.copy(CornerSize(12.dp)))
            .background(Color(0xFF2B2E31))
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageSection(course)
        Spacer(Modifier.width(4.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 18.dp),
            verticalArrangement = Arrangement.Center
        ) {
            SaiText(
                text = course.courseName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            SaiText(
                buildAnnotatedString {
                    append("${course.distance}km · 난이도 ")
                    withStyle(SpanStyle(color = course.level.color)) {
                        append(course.level.displayText)
                    }
                },
                fontSize = 12.sp,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(10.dp))

            ParticipantRewardInfo(
                challengerCount = course.courseChallengerCount,
                reward = course.reward,
                isLong = true
            )
        }
    }
}

@Composable
private fun ImageSection(
    course: CourseListItem,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxHeight()
    ) {
        AsyncImage(
            model = course.imageUrl,
            contentDescription = "${course.courseName} 이미지",
            modifier = Modifier
                .size(width = 170.dp, height = 140.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
        ChallengeStatusBadge(
            course.challengeEndedAt,
            isEvent = course.isEventActive,
            modifier = Modifier.padding(6.dp)
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
