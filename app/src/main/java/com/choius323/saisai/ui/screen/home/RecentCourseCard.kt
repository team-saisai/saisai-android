package com.choius323.saisai.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.RecentCourse
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun RecentCourseCard(
    recentCourse: RecentCourse,
    modifier: Modifier = Modifier,
    onClickCourse: (courseId: Long) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF292929))
            .clickable { onClickCourse(recentCourse.courseId) }
    ) {
        AsyncImage(
            model = recentCourse.imageUrl,
            contentDescription = "${recentCourse.courseName} 이미지",
            contentScale = ContentScale.Crop,
            error = ColorPainter(Color.DarkGray),
            placeholder = ColorPainter(Color.DarkGray),
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.45f)
                .align(Alignment.CenterEnd)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF292929), Color.Transparent),
                            startX = 0f,
                            endX = 20.dp.toPx(),
                        )
                    )
                }
        )

        // 2. 왼쪽 정보 영역
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // "최근 코스" 타이틀
            SaiText(
                text = "최근 코스",
                color = SaiColor.Gray40,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500
            )
            SaiText(
                text = recentCourse.courseName,
                color = SaiColor.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(30.dp))

            // 날짜, 총거리, 완주율 정보
            Column {
                SaiText(
                    text = recentCourse.recentDateAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    color = SaiColor.Gray70,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Gray, fontSize = 14.sp)) {
                            append("총거리 ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = SaiColor.Lime,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("${recentCourse.distance}km")
                        }
                        withStyle(style = SpanStyle(color = Color.Gray, fontSize = 14.sp)) {
                            append("  달성률   ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = SaiColor.Lime,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("${recentCourse.progressRate.roundToInt()}%")
                        }
                    }
                )
            }
        }

        // 3. 오른쪽 하단 버튼
        val isCompleted = recentCourse.progressRate >= 100.0
        Button(
            onClick = { onClickCourse(recentCourse.courseId) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 18.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SaiColor.Purple
            )
        ) {
            Text(
                text = if (isCompleted) "다시 도전하기" else "이어하기",
                color = SaiColor.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF202326)
@Composable
fun RecentCourseCardCompletedPreview() {
    val sampleCourse = RecentCourse(
        courseId = 1L,
        courseName = "샘플 코스",
        distance = 5.8,
        sigun = "서울시",
        progressRate = 100.0,
        recentDateAt = LocalDate.of(2025, 6, 10),
        imageUrl = null
    )
    SaiTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                RecentCourseCard(recentCourse = sampleCourse, onClickCourse = {})
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202326)
@Composable
fun RecentCourseCardInProgressPreview() {
    val sampleCourse = RecentCourse(
        courseId = 2L,
        courseName = "진행중 코스",
        distance = 10.2,
        sigun = "경기도",
        progressRate = 75.5,
        recentDateAt = LocalDate.of(2025, 6, 10),
        imageUrl = null
    )
    SaiTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                RecentCourseCard(recentCourse = sampleCourse, onClickCourse = {})
            }
        }
    }
}