package top.x86.widgets.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun RingShape(
    radius: Dp,
    strokeWidth: Dp,
    ringColor: Color = MaterialTheme.colorScheme.primary,
    centerColor: Color = Color.White
) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        Canvas(modifier = Modifier.size(radius* 2)) {
            val canvasRadius = (radius-strokeWidth/2).toPx()
            val canvasStrokeWidth = strokeWidth.toPx()
            val centerX = size.width / 2
            val centerY = size.height / 2

            drawCircle(
                color = centerColor,
                center = Offset(centerX, centerY),
                radius = canvasRadius,
                style = Fill
            )

            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(centerX - canvasRadius, centerY - canvasRadius),
                size = Size(2 * canvasRadius, 2 * canvasRadius),
                style = Stroke(canvasStrokeWidth)
            )
        }
    }
}

@Preview
@Composable
fun Preview_RingShape() {
    Box(modifier = Modifier.background(Color.Yellow)) {
        RingShape(radius = 30.dp, strokeWidth = 5.dp)
    }
}