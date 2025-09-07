package com.choius323.saisai.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.theme.SaiColor

@Composable
fun SaiSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    val trackWidth = 40.dp
    val trackHeight = 22.dp
    val thumbDiameter = 18.dp

    // 내부 패딩
    val internalPadding = (trackHeight - thumbDiameter) / 2

    // 썸의 시작(unchecked) 및 끝(checked) X 좌표 오프셋
    val thumbStartX = internalPadding
    val thumbEndX = trackWidth - thumbDiameter - internalPadding

    val animatedThumbOffsetX by animateDpAsState(
        targetValue = if (checked) thumbEndX else thumbStartX,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffsetX"
    )

    val trackColor = if (checked) SaiColor.LightPurple else SaiColor.Gray70
    val thumbColor = if (checked) SaiColor.Gray00 else SaiColor.Gray50

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(percent = 50))
            .background(trackColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = animatedThumbOffsetX)
                .size(thumbDiameter)
                .background(thumbColor, CircleShape)
        )
    }
}

@Preview
@Composable
private fun SaiSwitchPreviewChecked() {
    SaiSwitch(checked = true)
}

@Preview
@Composable
private fun SaiSwitchPreviewUnchecked() {
    SaiSwitch(checked = false)
}