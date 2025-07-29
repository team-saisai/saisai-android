package com.choius323.saisai.ui.screen.course_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.choius323.saisai.R
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import kotlin.math.atan
import kotlin.math.tan

@Composable
fun CourseDetailDescriptionBottomButton(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_arrow_container),
            contentDescription = "background shape",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Icon(
            painter = painterResource(R.drawable.icon_arrow_down),
            contentDescription = if (expanded) "접기" else "펼치기",
            tint = SaiColor.White,
            modifier = Modifier.rotate(if (expanded) 180f else 0f)
        )
    }
}

@Stable
private class InvertedTrapezoidShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val cornerRadius = 10f
        val path = Path()
        val width = size.width
        val height = size.height
        val bottomWidth = width * 0.9f
        val horizontalMargin = (width - bottomWidth) / 2f

        // 각도 계산
        val angle = atan(height / horizontalMargin)
        val angleDegree = (angle * 180 / Math.PI).toFloat()

        // 원의 중심점 계산
        val bottomCircleCenter = Offset(
            cornerRadius / tan(angle / 2),
            height - cornerRadius
        )

        path.apply {
            // 왼쪽 상단에서 시작
            moveTo(0f, 0f)
            // 상단 선 그리기
            lineTo(width, 0f)

            // 오른쪽 하단 코너
            arcTo(
                rect = Rect(
                    offset = Offset(
                        width - bottomCircleCenter.x - cornerRadius,
                        bottomCircleCenter.y - cornerRadius
                    ),
                    size = Size(cornerRadius * 2, cornerRadius * 2)
                ),
                startAngleDegrees = angleDegree - 90f,
                sweepAngleDegrees = 180f - angleDegree,
                forceMoveTo = false
            )

            // 왼쪽 하단 코너
            arcTo(
                rect = Rect(
                    offset = Offset(
                        bottomCircleCenter.x - cornerRadius,
                        bottomCircleCenter.y - cornerRadius
                    ),
                    size = Size(cornerRadius * 2, cornerRadius * 2)
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 180f - angleDegree,
                forceMoveTo = false
            )

            close()
        }
        return Outline.Generic(path)
    }
}

@Preview
@Composable
private fun CourseDetailDescriptionBottomButtonPreview() {
    SaiTheme {
        Surface {
            CourseDetailDescriptionBottomButton(expanded = false, onClick = {})
        }
    }
}
