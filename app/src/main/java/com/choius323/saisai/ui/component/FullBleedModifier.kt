package com.choius323.saisai.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 부모의 크기와 관계 없이 화면의 가로 방향 끝까지 그리도록 최적화된 Modifier
 *
 * @param onPaddingMeasured 컴포저블이 화면 가장자리로부터 얼마나 떨어져 있는지 패딩 값을 전달합니다.
 * 이 콜백을 사용하면 FullBleedContainer 내부의 컨텐츠가 이 값을 활용할 수 있습니다.
 */
fun Modifier.fullBleed(
    onPaddingMeasured: (start: Dp, end: Dp) -> Unit = { _, _ -> },
): Modifier = this then FullBleedElement(onPaddingMeasured)

// 1. ModifierNodeElement: Modifier와 Modifier.Node를 연결하는 보일러플레이트
private data class FullBleedElement(
    val onPaddingMeasured: (start: Dp, end: Dp) -> Unit,
) : ModifierNodeElement<FullBleedNode>() {
    override fun create(): FullBleedNode {
        return FullBleedNode(onPaddingMeasured)
    }

    override fun update(node: FullBleedNode) {
        node.onPaddingMeasured = onPaddingMeasured
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "fullBleed"
        properties["onPaddingMeasured"] = onPaddingMeasured
    }
}

// 2. Modifier.Node: 실제 로직을 담고 있는 노드
private class FullBleedNode(
    var onPaddingMeasured: (start: Dp, end: Dp) -> Unit,
) : Modifier.Node(), LayoutModifierNode, GlobalPositionAwareModifierNode {

    private var startPaddingPx: Int = 0
    private var endPaddingPx: Int = 0

    // 3. onGloballyPositioned: 위치가 변경될 때 호출
    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        val rootCoordinates = coordinates.findRootCoordinates()
        val rootWidth = rootCoordinates.size.width
        val boundsInRoot = coordinates.boundsInRoot()

        // 화면 왼쪽 끝부터 컴포저블 시작점까지의 거리 (Padding Start)
        val currentStartPaddingPx = boundsInRoot.left.toInt().coerceAtLeast(0)
        // 컴포저블의 오른쪽 끝부터 루트의 오른쪽 끝까지의 거리
        val currentEndPaddingPx = (rootWidth - boundsInRoot.right).toInt().coerceAtLeast(0)

        if (startPaddingPx != currentStartPaddingPx || endPaddingPx != currentEndPaddingPx) {
            startPaddingPx = currentStartPaddingPx
            endPaddingPx = currentEndPaddingPx

            with(requireDensity()) {
                onPaddingMeasured(startPaddingPx.toDp(), endPaddingPx.toDp())
            }

            invalidateMeasurement()
        }
    }

    // 4. measure: LayoutModifierNode의 일부로, 측정 로직을 담당
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val totalExpansion = startPaddingPx + endPaddingPx
        // 제약조건을 양쪽 패딩만큼 늘려서 자식(measurable)을 측정
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = constraints.maxWidth + totalExpansion
            )
        )

        // 레이아웃의 크기는 원래 제약조건(constraints.maxWidth)을 따름
        return layout(constraints.maxWidth, placeable.height) {
            // 자식을 왼쪽 패딩만큼 음수 위치에 배치하여 확장 효과를 줌
            placeable.placeRelative(-startPaddingPx, 0)
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