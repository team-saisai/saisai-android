package com.choius323.saisai.ui.screen.record

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ForkRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.model.CourseDetail
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun RecordStateDescription(
    uiState: RecordUiState,
    modifier: Modifier = Modifier,
    toggleRecording: () -> Unit,
    toggleExpanded: () -> Unit,
) {
    val courseDetail = uiState.courseDetail ?: return
    val distance by remember(uiState.totalRideDistance) {
        derivedStateOf {
            buildAnnotatedString {
                withStyle(SpanStyle(color = SaiColor.Lime)) {
                    append("${uiState.totalRideDistance}km")
                }
                append(" / ${courseDetail.distance}km")
            }
        }
    }
    Column(
        modifier = modifier
    ) {
        RecordDescriptionSummary(
            courseDetail = uiState.courseDetail,
            expanded = uiState.isExpandedSummary,
            modifier = Modifier
                .animateContentSize(animationSpec = tween(500)),
            toggleExpanded = toggleExpanded
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xE642464A))
                .border(
                    1.4.dp,
                    color = if (uiState.isRecording) Color(0xFFD4D8CD) else Color(0xFFDE6666),
                    RoundedCornerShape(16.dp)
                )
                .padding(top = 18.dp, bottom = 18.dp, start = 20.dp, end = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(distance, fontSize = 24.sp)
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF42464A).copy(alpha = 0.9f))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth((uiState.totalRideDistance / courseDetail!!.distance).toFloat())
                            .fillMaxHeight()
                            .background(SaiColor.Lime)
                    )
                }
                Spacer(Modifier.height(20.dp))
                Row {
                    Icon(Icons.Default.Route, null)
                    SaiText("다음 체크포인트: ")
                }
            }
            Spacer(Modifier.width(28.dp))
            RecordingToggleButton(uiState.isRecording, toggleRecording = toggleRecording)
        }
    }
}

@Composable
private fun RecordingToggleButton(
    isRecording: Boolean,
    modifier: Modifier = Modifier,
    toggleRecording: () -> Unit,
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SaiColor.Lime)
            .padding(top = 9.dp, bottom = 11.dp, start = 6.dp, end = 6.dp)
            .clickable(onClick = toggleRecording),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (isRecording) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "도전하기 아이콘",
            tint = SaiColor.Black,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        SaiText(
            text = if (isRecording) "일시정지" else "이어하기",
            style = MaterialTheme.typography.labelMedium.copy(
                color = SaiColor.Black, fontWeight = FontWeight.SemiBold
            ),
        )
    }
}

@Composable
private fun RecordDescriptionSummary(
    courseDetail: CourseDetail,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    toggleExpanded: () -> Unit,
) {
    if (expanded) {
        RecordDescriptionSummaryOpened(
            detail = courseDetail,
            modifier = modifier,
            toggleExpanded = toggleExpanded
        )
    } else {
        RecordDescriptionSummaryClosed(modifier, toggleExpanded)
    }
}

@Composable
private fun RecordDescriptionSummaryOpened(
    detail: CourseDetail,
    modifier: Modifier = Modifier,
    toggleExpanded: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xE642464A))
            .clickable(onClick = toggleExpanded)
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        SaiText(text = detail.courseName, fontSize = 16.sp, fontWeight = FontWeight.W600)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            buildAnnotatedString {
                append("${detail.distance}km · 난이도 ")
                withStyle(SpanStyle(color = detail.level.color)) {
                    append(detail.level.displayText)
                }
            },
            fontSize = 11.sp,
            color = SaiColor.Gray40
        )
        Spacer(Modifier.height(14.dp))
        SaiText(
            modifier = Modifier.fillMaxWidth(),
            text = detail.summary,
            lineHeight = 22.sp,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun RecordDescriptionSummaryClosed(
    modifier: Modifier = Modifier,
    toggleExpanded: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0xE642464A))
            .clickable(onClick = toggleExpanded)
            .padding(vertical = 6.dp)
            .padding(start = 8.dp, end = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.ForkRight, null)
        Spacer(Modifier.width(4.dp))
        SaiText("경로 소개", fontSize = 13.sp)
    }
}

@Preview
@Composable
private fun RecordDescriptionSummaryPreview() {
    SaiTheme {
        Surface {
            RecordDescriptionSummary(
                courseDetail = CourseDetail.sample,
                expanded = false,
                toggleExpanded = {}
            )
        }
    }
}

@Preview
@Composable
private fun RecordDescriptionSummaryExpandedPreview() {
    SaiTheme {
        Surface {
            RecordDescriptionSummary(
                courseDetail = CourseDetail.sample,
                expanded = true,
                toggleExpanded = {}
            )
        }
    }
}

@Preview
@Composable
private fun RecordStateDescriptionPreview() {
    SaiTheme {
        Surface {
            RecordStateDescription(
                uiState = RecordUiState(
                    courseDetail = CourseDetail.sample,
                    isRecording = true,
                    totalRideDistance = 5.3
                ), Modifier, {}, {}
            )
        }
    }
}

@Preview
@Composable
private fun RecordStateDescriptionStopPreview() {
    SaiTheme {
        Surface {
            RecordStateDescription(
                uiState = RecordUiState(
                    courseDetail = CourseDetail.sample,
                    totalRideDistance = 5.3
                ), Modifier, {}, {}
            )
        }
    }
}