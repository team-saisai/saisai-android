package com.choius323.saisai.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.crop(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    layout(
        placeable.width - (horizontal * 2).toPx().toInt(),
        placeable.height - (vertical * 2).toPx().toInt()
    ) {
        placeable.placeRelative(-horizontal.toPx().toInt(), -vertical.toPx().toInt())
    }
}

/**
 * 부모의 크기와 관계 없이 화면의 가로 방향 끝까지 그리는 Modifier
 */
fun Modifier.fullBleed(
    onPaddingMeasured: (start: Dp, end: Dp) -> Unit = { _, _ -> },
): Modifier = composed {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }

    var startPadding by remember { mutableStateOf(0.dp) }
    var endPadding by remember { mutableStateOf(0.dp) }

    onGloballyPositioned { coordinates ->
        if (coordinates.isAttached) {
            val globalPositionX = with(density) { coordinates.positionInRoot().x.toDp() }
            val componentWidth = with(density) { coordinates.size.width.toDp() }
            val currentStartPadding = globalPositionX.coerceAtLeast(0.dp)
            val currentEndPadding =
                (screenWidth - (globalPositionX + componentWidth)).coerceAtLeast(0.dp)

            if (startPadding != currentStartPadding || endPadding != currentEndPadding) {
                startPadding = currentStartPadding
                endPadding = currentEndPadding
                onPaddingMeasured(startPadding, endPadding)
            }
        }
    }.layout { measurable, constraints ->
        val totalExpansion = startPadding.roundToPx() + endPadding.roundToPx()
        val newConstraints = constraints.copy(
            maxWidth = constraints.maxWidth + totalExpansion
        )
        val placeable = measurable.measure(newConstraints)

        layout(constraints.maxWidth, placeable.height) {
            placeable.placeRelative(-startPadding.roundToPx(), 0)
        }
    }
}

@Composable
fun FullBleedContainer(
    modifier: Modifier = Modifier,
    content: @Composable (startPadding: Dp, endPadding: Dp) -> Unit,
) {
    var startPadding by remember { mutableStateOf(0.dp) }
    var endPadding by remember { mutableStateOf(0.dp) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fullBleed { start, end ->
                // Modifier가 측정한 값을 상태에 저장
                startPadding = start
                endPadding = end
            }
    ) {
        content(startPadding, endPadding)
    }
}