package com.choius323.saisai.ui.screen.ride_history

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.CourseItemImageSection
import com.choius323.saisai.ui.component.CourseItemTitleSection
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.Level
import com.choius323.saisai.ui.model.RideHistoryItem
import com.choius323.saisai.ui.theme.SaiColor
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun RideHistoryItem(
    modifier: Modifier = Modifier,
    course: RideHistoryItem,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape.copy(CornerSize(16.dp)))
            .background(Color(0xFF2B2E31))
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CourseItemImageSection(
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
            rideDate = course.lastRideDate,
            estimatedTime = course.estimatedTime,
            rideProgress = course.progressRate,
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp, bottom = 18.dp, end = 10.dp),
        )
    }
}

@Composable
private fun CourseInformationSection(
    courseName: String,
    distance: Double,
    level: Level,
    rideDate: LocalDate,
    estimatedTime: Double,
    rideProgress: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        CourseItemTitleSection(
            courseName,
            distance,
            level,
            isBookmarked = null,
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 10.dp)
        )

        // 현재 도전 인원 정보
        RideInfo(rideDate, estimatedTime, rideProgress)
    }
}

@Composable
private fun RideInfo(
    rideDate: LocalDate,
    estimatedTime: Double,
    rideProgress: Int,
    modifier: Modifier = Modifier,
) {
    val timeFormatted by remember(estimatedTime) {
        derivedStateOf {
            estimatedTime.toLong().let {
                val hours = TimeUnit.SECONDS.toHours(it)
                val minutes = TimeUnit.SECONDS.toMinutes(it) % 60
                val seconds = it % 60

                String.Companion.format(Locale.KOREA, "%02d:%02d:%02d", hours, minutes, seconds)
            }
        }
    }
    val annotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontWeight = FontWeight.W400,
                color = SaiColor.Gray10,
                fontSize = 13.sp
            )
        ) {
            append(timeFormatted)
            append(" ∙ 완주율")
            withStyle(SpanStyle(fontWeight = FontWeight.W500, color = SaiColor.Lime)) {
                append("${rideProgress}%")
            }
        }
    }
    Column(modifier) {
        SaiText(
            rideDate.format(rideDateFormatter),
            color = SaiColor.Gray70,
            fontSize = 11.sp,
            fontWeight = FontWeight.W400
        )
        SaiText(annotatedString)
    }
}

private val rideDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")