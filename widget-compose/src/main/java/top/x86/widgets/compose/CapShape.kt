package top.x86.widgets.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CapShape(modifier: Modifier = Modifier,
             backgroundColor: Color = Color.White,
             topRadius: Dp = 0.dp,
             bottomRadius: Dp = 0.dp
) {
    CapShape(modifier, backgroundColor, topRadius, topRadius, bottomRadius, bottomRadius)
}

@Composable
fun CapShape(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    cornerRadiusTopLeft: Dp = 0.dp,
    cornerRadiusTopRight: Dp = 0.dp,
    cornerRadiusBottomLeft: Dp = 0.dp,
    cornerRadiusBottomRight: Dp = 0.dp,
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = cornerRadiusTopLeft,
                    topEnd = cornerRadiusTopRight,
                    bottomStart = cornerRadiusBottomLeft,
                    bottomEnd = cornerRadiusBottomRight
                )
            )
            .graphicsLayer(
                clip = true // 用于确保内容在圆角处被裁剪
            )
    ) {

    }
}

@Preview
@Composable
private fun PreviewCapShape() {
    Box(Modifier.background(Color.Gray).padding(30.dp)) {
        CapShape(modifier = Modifier
            .size(100.dp),
            bottomRadius = 10.dp)

    }
}