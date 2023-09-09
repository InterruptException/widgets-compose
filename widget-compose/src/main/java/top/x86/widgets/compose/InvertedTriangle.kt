package top.x86.widgets.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InvertedTriangle(
    width: Dp,
    height: Dp,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(width.toPx(), 0f)
                lineTo(width.toPx() / 2f, height.toPx())
                close()
            }
            drawPath(
                path = path,
                style = Fill,
                color = color
            )
        }
    }
}

@Preview
@Composable
fun PreviewInvertedTriangle() {
    InvertedTriangle(width = 100.dp, height = 150.dp, color = Color.Red)
}
