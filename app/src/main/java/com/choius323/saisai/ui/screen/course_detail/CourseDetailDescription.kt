package com.choius323.saisai.ui.screen.course_detail

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.R
import com.choius323.saisai.ui.component.ChallengeStatusBadge
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen

@Composable
fun CourseDetailDescription(
    detail: CourseDetail,
    modifier: Modifier = Modifier,
    onChallengeClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val time by remember(detail.estimatedTime) {
        derivedStateOf {
            val hour = detail.estimatedTime.toInt() / 60
            val minute = detail.estimatedTime.toInt() % 60
            "${hour}h ${minute}m"
        }
    }

    Column(modifier) {
        ChallengeStatusBadge(
            detail.challengeEndedAt,
            isEvent = detail.isEventActive,
            modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { expanded = expanded.not() }
                .background(Color(0xE642464A))
                .padding(18.dp)
                .animateContentSize(animationSpec = tween(500)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    CourseInfoSection(detail)
                    Spacer(modifier = Modifier.height(20.dp))
                    ParticipantInfoSection(detail.inProgressUserCount, detail.completeUserCount)
                }

                // 우측 '도전하기' 버튼 영역
                Column(
                    Modifier
                        .align(Alignment.Top)
                        .padding(start = 8.dp)
                        .width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ChallengeButton(modifier = Modifier, onClick = onChallengeClick)
                    Spacer(Modifier.height(6.dp))
                    SaiText(time, color = SaiColor.Lime, maxLines = 1)
                }
            }
            if (expanded) {
                SaiText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    text = detail.summary
                )
            }
        }
        CourseDetailDescriptionBottomButton(
            expanded = expanded,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            expanded = expanded.not()
        }
    }
}


@Composable
private fun CourseInfoSection(detail: CourseDetail) {
    Column {
        SaiText(
            text = detail.courseName, fontSize = 20.sp, fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        SaiText(
            buildAnnotatedString {
                append("${detail.distance}km · 난이도 ")
                withStyle(SpanStyle(color = detail.level.color)) {
                    append(detail.level.displayText)
                }
            },
            fontSize = 12.sp,
            color = SaiColor.Gray40
        )
    }
}

@Composable
private fun ParticipantInfoSection(challengerCount: Int, completedCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.outline_flash_on_24),
            contentDescription = "도전자 아이콘",
            tint = SaiColor.LightPurple,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        SaiText(
            text = "${challengerCount}명 도전중",
            fontSize = 14.sp,
            color = SaiColor.LightPurple,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Default.CheckCircleOutline,
            contentDescription = "완주자 아이콘",
            tint = SaiColor.LightPurple,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        SaiText(
            text = "${completedCount}명 완주",
            fontSize = 14.sp,
            color = SaiColor.LightPurple,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ChallengeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SaiColor.Lime)
            .padding(vertical = 12.dp, horizontal = 5.5.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.DirectionsBike,
            contentDescription = "도전하기 아이콘",
            tint = SaiColor.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        SaiText(
            text = "도전하기",
            style = MaterialTheme.typography.labelMedium.copy(
                color = SaiColor.Black, fontWeight = FontWeight.SemiBold
            ),
        )
    }
}

@Preview
@Composable
private fun CourseDetailDescriptionPreview() {
    AndroidThreeTen.init(LocalContext.current)
    val sampleDetail = CourseDetail.sample
    SaiTheme(darkTheme = true) {
        Surface {
            CourseDetailDescription(
                detail = sampleDetail,
                onChallengeClick = {},
            )
        }
    }
}