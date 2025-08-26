package com.choius323.saisai.ui.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.component.SaiText
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun RecordTimerText(
    totalSeconds: Long,
    rideState: RideState,
    modifier: Modifier = Modifier,
) {
    if (rideState != RideState.COMPLETE) {
        val timeFormatted = totalSeconds.let {
            val hours = TimeUnit.SECONDS.toHours(it)
            val minutes = TimeUnit.SECONDS.toMinutes(it) % 60
            val seconds = it % 60

            String.Companion.format(Locale.KOREA, "%02d:%02d:%02d", hours, minutes, seconds)
        }

        SaiText(
            text = timeFormatted,
            modifier = modifier
                .background(
                    if (rideState == RideState.RECORDING) Color(0x33B0A2FF) else Color(
                        0x29FC9292
                    ), RoundedCornerShape(40.dp)
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            color = if (rideState == RideState.RECORDING) SaiColor.LightPurple else Color(0xFFFF7676)
        )
    }
}

@Preview
@Composable
private fun TimerTextPreview() {
    SaiTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                RecordTimerText(
                    totalSeconds = 1000L,
                    rideState = RideState.RECORDING,
                )
                RecordTimerText(
                    totalSeconds = 500L,
                    rideState = RideState.PAUSED,
                )
            }
        }
    }
}

private const val TAG = "RecordTimerText"