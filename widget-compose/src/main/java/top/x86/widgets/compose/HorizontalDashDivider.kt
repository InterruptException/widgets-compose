package top.x86.widgets.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.x86.compose.more.modifiers.dashLine

@Composable
fun HorizontalDashDivider(modifier: Modifier = Modifier,
                          dashColor: Color = Color(0x33000000),
                          backgroundColor: Color = Color.White,
                          height: Dp = 1.dp
) {
    Box(modifier = modifier.wrapContentHeight()) {
        Box(
            modifier = Modifier.background(backgroundColor).height(height).fillMaxWidth()
                .dashLine(lineWidth = 1.dp, dashColor = dashColor, cap = StrokeCap.Butt)
        )
    }
}