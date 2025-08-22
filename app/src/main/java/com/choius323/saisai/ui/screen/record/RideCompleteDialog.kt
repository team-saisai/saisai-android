package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun RideCompleteDialog(
    imageUrl: String?,
    courseName: String,
    distance: Double,
    rideTime:Long,
    modifier: Modifier = Modifier,
    goHome: () -> Unit,
    goCourseDetail: () -> Unit,
) {
    Box(
        modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = "Go Back",
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .offset(x = 18.dp, y = 8.dp)
                .size(24.dp)
                .clickable(onClick = goCourseDetail),
            tint = Color.White,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SaiText("수고하셨습니다!", fontSize = 18.sp)
            Spacer(Modifier.height(30.dp))
            Column(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .width(IntrinsicSize.Min)
                    .background(SaiColor.Gray90),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(width = 320.dp, 400.dp)
                        .background(color = Color.LightGray),
                    model = imageUrl,
                    contentDescription = "코스 이미지",
                )
                RideCompleteDescription(courseName, distance, rideTime)
            }
            Spacer(Modifier.height(50.dp))
            Button(
                goHome,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x14FFFFFF),
                    contentColor = SaiColor.White
                )
            ) {
                SaiText("홈으로 이동하기", fontSize = 16.sp)
            }
        }
    }
    // TODO: 뒤로가기 이벤트에 goCourseDetail 등록
}

@Composable
private fun RideCompleteDescription(
    courseName: String,
    distance: Double,
    time: Long,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .background(SaiColor.Gray90)
            .fillMaxWidth()
            .padding(top = 19.dp, bottom = 28.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Column {
            SaiText(courseName, fontSize = 20.sp, fontWeight = FontWeight.W500)
            Row(verticalAlignment = Alignment.CenterVertically) {
                SaiText(
                    "총 ${String.format(Locale.KOREA, "%.1f", distance)}km",
                    color = SaiColor.Gray40,
                    fontSize = 14.sp
                )
                Spacer(Modifier.width(7.dp))
                TimeText(time)
            }
        }
    }
}

@Composable
private fun TimeText(
    time: Long,
    modifier: Modifier = Modifier,
) {
    val timeFormatted by remember(time) {
        derivedStateOf {
            val hours = TimeUnit.SECONDS.toHours(time)
            val minutes = TimeUnit.SECONDS.toMinutes(time) % 60
            val seconds = time % 60

            String.format(Locale.KOREA, "%02d:%02d:%02d", hours, minutes, seconds)
        }
    }
    SaiText(timeFormatted, color = SaiColor.LightPurple, fontSize = 14.sp, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
private fun RideCompleteScreenContentPreview() {
    SaiTheme {
        Surface {
            RideCompleteDialog(
                imageUrl = "",
                courseName = "여의대로 - 국회대로",
                distance = 13.2123,
                rideTime = System.currentTimeMillis() - 1000,
                goHome = {},
                goCourseDetail = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}