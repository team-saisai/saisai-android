package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import kotlin.math.roundToInt

@Composable
fun DescriptionContent(
    nowCheckPointIdx: Int,
    totalCheckPointSize: Int,
    rideState: RideState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (rideState == RideState.PAUSED) {
            Row(
                Modifier
                    .background(Color(0x29FC9292), RoundedCornerShape(50.dp))
                    .padding(top = 6.dp, bottom = 6.dp, end = 12.dp, start = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Rounded.Pause, "일시 정지", Modifier.size(14.dp), tint = Color(0xFFFF7676))
                SaiText(
                    "일시정지 중",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFFFF7676)
                )
            }
        } else {
            SaiText("체크포인트 달성률", fontSize = 10.sp, color = SaiColor.Gray40)
            Row(verticalAlignment = Alignment.Bottom) {
                val percentage =
                    ((nowCheckPointIdx + 1).toFloat() / totalCheckPointSize * 100).roundToInt()
                SaiText(
                    "${if (percentage == 100 && rideState != RideState.COMPLETE) 99 else percentage}%",
                    color = SaiColor.Lime,
                    fontSize = 22.sp
                )
                Spacer(Modifier.width(5.dp))
                SaiText(
                    "${nowCheckPointIdx + 1} / $totalCheckPointSize points",
                    Modifier.padding(vertical = 2.dp),
                    color = SaiColor.White,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(Modifier.height(11.dp))
        RecordStateProgressBar(nowCheckPointIdx, totalCheckPointSize, Modifier)
    }
}

@Composable
private fun RecordStateProgressBar(
    nowCheckPointIdx: Int,
    totalCheckPointSize: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .height(4.dp)
                    .background(SaiColor.Gray85)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (index in 0 until totalCheckPointSize) {
                    val pointColor =
                        if (index <= nowCheckPointIdx) SaiColor.Lime else SaiColor.Gray85

                    CheckpointCircle(color = pointColor)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (index in 0 until totalCheckPointSize - 1) {
                    val isLineChecked = index < nowCheckPointIdx
                    val lineColor = if (isLineChecked) SaiColor.Lime else Color.Transparent

                    // 가로 막대
                    Spacer(
                        modifier = modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(lineColor)
                    )
                }
            }
        }
        ProgressText(
            Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
        )
    }
}

@Composable
private fun CheckpointCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(13.dp)
            .background(color = color.copy(0.5f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .background(color = color, shape = CircleShape),
        )
    }
}

@Composable
private fun ProgressText(modifier: Modifier = Modifier) {
    val density = LocalDensity.current

    var goTextWidthPx by remember { mutableIntStateOf(0) }
    var finTextWidthPx by remember { mutableIntStateOf(0) }

    val goTextOffset = with(density) { -(goTextWidthPx / 2).toDp() }
    val finTextOffset = with(density) { (finTextWidthPx / 2).toDp() }

    Box(modifier = modifier) {
        SaiText(
            text = "GO",
            color = SaiColor.White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .onGloballyPositioned { coordinates ->
                    goTextWidthPx = coordinates.size.width
                }
                .offset(x = goTextOffset + 6.5.dp)
        )
        SaiText(
            text = "FIN",
            color = SaiColor.White,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onGloballyPositioned { coordinates ->
                    finTextWidthPx = coordinates.size.width
                }
                .offset(x = finTextOffset - 6.5.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF555555)
@Composable
private fun DescriptionContentPreview() {
    SaiTheme {
        DescriptionContent(
            nowCheckPointIdx = 2,
            totalCheckPointSize = 5,
            rideState = RideState.COMPLETE
        )
    }
}

@Preview
@Composable
private fun DescriptionContentPreviewCompleted() {
    SaiTheme {
        DescriptionContent(
            nowCheckPointIdx = 4,
            totalCheckPointSize = 5,
            rideState = RideState.RECORDING
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF555555)
@Composable
private fun DescriptionContentPreviewNoStarted() {
    SaiTheme {
        DescriptionContent(
            nowCheckPointIdx = -1,
            totalCheckPointSize = 5,
            rideState = RideState.RECORDING
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF555555)
@Composable
private fun DescriptionContentPausedPreview() {
    SaiTheme {
        DescriptionContent(
            nowCheckPointIdx = 3,
            totalCheckPointSize = 5,
            rideState = RideState.PAUSED
        )
    }
}
