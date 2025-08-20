package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor
import kotlin.math.roundToInt

@Composable
fun DescriptionContent(
    nowCheckPointIdx: Int,
    totalCheckPointSize: Int,
    rideState: RideState,
    modifier: Modifier = Modifier,
) {
    val progress = nowCheckPointIdx.toFloat() / totalCheckPointSize
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
                SaiText(
                    "${(progress * 100).roundToInt()}%",
                    color = SaiColor.Lime,
                    fontSize = 22.sp
                )
                Spacer(Modifier.width(5.dp))
                SaiText(
                    "$nowCheckPointIdx / $totalCheckPointSize points",
                    Modifier.padding(vertical = 2.dp),
                    color = SaiColor.White,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(Modifier.height(11.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SaiColor.Gray70)
        ) {
            Box(
                Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(SaiColor.Lime)
            )
        }
    }
}