package top.x86.widgets.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.core.math.MathUtils
import java.lang.Float.max
import kotlin.math.roundToInt

@OptIn(ExperimentalTextApi::class)
@Composable
fun TextProgressBar(modifier: Modifier = Modifier,
                    percent: Float, //0<= percent >=100
                    barHeightDp : Dp = 4.dp,
                    ringOuterRadius: Dp = 8.dp,
                    ringInnerRadius: Dp = 4.dp,
                    ringOuterColor: Color = MaterialTheme.colorScheme.primary,
                    ringInnerColor: Color = Color.White,
                    barFinishedColor: Color = MaterialTheme.colorScheme.primary,
                    barUnfinishedColor: Color = Color(0xFFE6E2FF),
                    textColor: Color = Color.White,
                    textStyle: TextStyle = LocalTextStyle.current,
                    bubbleColor: Color = MaterialTheme.colorScheme.primary,
                    bubbleRadiusDp: Dp = 2.dp,
                    bubblePadding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                    bubbleVerticalSpace: Dp = 8.dp,
                    fixedBubbleWidth: Boolean = true,
                    triangleHeightDp: Dp = 4.dp,
                    triangleWidthDp: Dp = 4.dp,
                    triangleColor: Color = MaterialTheme.colorScheme.primary
) {
    val p = MathUtils.clamp(percent, 0f, 100f)
    val textMeasurer = rememberTextMeasurer()
    val textStyle2 = remember(textStyle) {
        textStyle.copy(
            color = textColor
        )
    }
    val text = remember(p) {
        "${p.roundToInt()}%"
    }
    val layoutDirection = LocalLayoutDirection.current
    val textLayout = textMeasurer.measure("100%", style = textStyle)//用于测量最大宽度，避免宽度变化
    val textLayoutSize = textLayout.size
    val bubblePaddingTopDp = bubblePadding.calculateTopPadding()
    val bubblePaddingBottomDp = bubblePadding.calculateBottomPadding()
    val bubblePaddingLeftDp = bubblePadding.calculateLeftPadding(layoutDirection)
    val bubblePaddingRightDp = bubblePadding.calculateRightPadding(layoutDirection)
    val textLayoutHeightDp = with(LocalDensity.current){
        textLayoutSize.height.toDp()
    }
    val textLayoutWidthDp = with(LocalDensity.current){
        textLayoutSize.width.toDp()
    }

    val bubbleHeightDp = textLayoutHeightDp + bubblePaddingTopDp + bubblePaddingBottomDp
    val bubbleWidthDp = textLayoutWidthDp + bubblePaddingLeftDp + bubblePaddingRightDp
    val canvasMinWidthDp = bubbleWidthDp
    val canvasMinHeightDp = bubbleHeightDp + triangleHeightDp + bubbleVerticalSpace + (max(barHeightDp, ringOuterRadius * 2))
    Box(modifier = Modifier.defaultMinSize(minWidth = canvasMinWidthDp, minHeight = canvasMinHeightDp).then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxWidth().matchParentSize()) {
            val bubblePaddingLeft = bubblePaddingLeftDp.toPx()
            val bubblePaddingRight = bubblePaddingRightDp.toPx()
            val bubblePaddingTop = bubblePaddingTopDp.toPx()
            val bubblePaddingBottom = bubblePaddingBottomDp.toPx()
            val bubbleSize = Size(
                width = textLayoutSize.width + bubblePaddingLeft + bubblePaddingRight,
                height = textLayoutSize.height + bubblePaddingTop + bubblePaddingBottom
            )
            val bothSidesSpace = bubbleSize.width / 2f
            val barWidth = size.width - bubbleSize.width
            val bubbleCenterX = barWidth * (p/100f) + bothSidesSpace
            val triangleCenterX = bubbleCenterX
            val barCenterX = size.width / 2f
            val barHeight = barHeightDp.toPx()
            val triangleHeight = triangleHeightDp.toPx()
            val triangleWidth = triangleWidthDp.toPx()
            val bubbleCenterY = bubbleSize.height / 2f
            val triangleCenterY = bubbleSize.height + triangleHeight/2f
            val barCenterY = triangleCenterY + triangleHeight / 2f + bubbleVerticalSpace.toPx() + max(barHeight / 2f, ringOuterRadius.toPx())
            val ringCenterX = triangleCenterX
            val ringCenterY = barCenterY

            drawBubble(
                fixedBubbleWidth = fixedBubbleWidth,
                percentText = text,
                textMeasurer = textMeasurer,
                maxTextLayoutSize = textLayoutSize,
                textStyle = textStyle2,
                bubbleColor = bubbleColor,
                textColor = textColor,
                bubbleRadius = CornerRadius(x = bubbleRadiusDp.toPx(), y = bubbleRadiusDp.toPx()),
                bubbleCenterX = bubbleCenterX,
                bubbleCenterY = bubbleCenterY,
                bubblePadding = bubblePadding,
                layoutDirection = layoutDirection,
            )

            drawBar(
                barCenterX = barCenterX,
                barCenterY = barCenterY,
                barMiddleX = triangleCenterX,
                barWidth = barWidth,
                barHeight = barHeight,
                barFinishedColor = barFinishedColor,
                barUnfinishedColor = barUnfinishedColor,
                bothSideSpace = bothSidesSpace)
            drawRing(
                ringCenterX = ringCenterX,
                ringCenterY = ringCenterY,
                ringOuterRadius = ringOuterRadius,
                ringInnerRadius = ringInnerRadius,
                ringOuterColor = ringOuterColor,
                ringInnerColor = ringInnerColor)

            drawTriangleBlock(
                triangleCenterX = triangleCenterX,
                triangleCenterY = triangleCenterY,
                triangleWidth = triangleWidth,
                triangleHeight = triangleHeight,
                triangleColor = triangleColor)
        }
    }
}

private fun DrawScope.drawBar(barCenterX: Float,
                              barCenterY: Float,
                              barMiddleX: Float,
                              barWidth: Float,
                              barHeight : Float,
                              barFinishedColor: Color,
                              barUnfinishedColor: Color,
                              bothSideSpace: Float
) {
    val barY = barCenterY
    val barLeftX = barCenterX - barWidth / 2f
    val barRightX = barCenterX + barWidth / 2f

    //底层
    drawLine(
        color = barUnfinishedColor,
        start = Offset(
            x = barLeftX,
            y = barY
        ),
        end = Offset(
            x = barRightX,
            y = barY
        ),
        strokeWidth = barHeight,
        cap = StrokeCap.Round
    )

    //上层
    drawLine(
        color = barFinishedColor,
        start = Offset(
            x = barLeftX,
            y = barY
        ),
        end = Offset(
            x = barMiddleX,
            y = barY
        ),
        strokeWidth = barHeight,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawTriangleBlock(triangleCenterX: Float,
                                        triangleCenterY: Float,
                                        triangleWidth: Float,
                                        triangleHeight: Float,
                                        triangleColor: Color,
) {
    val halfWidth = triangleWidth/2f
    val halfHeight = triangleHeight/2f
    val triangleTopLeft = Offset(
        x = triangleCenterX - halfWidth,
        y = triangleCenterY - halfHeight
    )
    val triangleTopRight = Offset(
        x = triangleCenterX + halfWidth,
        y = triangleCenterY - halfHeight
    )
    val triangleBottomCenter = Offset(
        x = triangleCenterX,
        y = triangleCenterY + halfHeight
    )
    val path = Path().apply {
        reset()
        moveTo(
            x = triangleTopLeft.x,
            y = triangleTopLeft.y,
        )
        lineTo(
            x = triangleTopRight.x,
            y = triangleTopRight.y,
        )
        lineTo(
            x = triangleBottomCenter.x,
            y = triangleBottomCenter.y,
        )
        close()
    }
    drawPath(
        path = path,
        color = triangleColor
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawBubble(fixedBubbleWidth: Boolean,
                                 percentText: String,
                                 textMeasurer: TextMeasurer,
                                 maxTextLayoutSize: IntSize,
                                 textStyle: TextStyle,
                                 bubbleColor: Color,
                                 textColor: Color,
                                 bubbleRadius: CornerRadius,
                                 bubbleCenterX: Float,
                                 bubbleCenterY: Float,
                                 bubblePadding: PaddingValues,
                                 layoutDirection: LayoutDirection
) {
    val textLayout = textMeasurer.measure(percentText, style = textStyle)
    val textLayoutSize = textLayout.size
    val paddingLeft = bubblePadding.calculateLeftPadding(layoutDirection).toPx()
    val paddingRight = bubblePadding.calculateRightPadding(layoutDirection).toPx()
    val paddingTop = bubblePadding.calculateTopPadding().toPx()
    val paddingBottom = bubblePadding.calculateBottomPadding().toPx()
    val actualMaxTextLayoutSize = if (fixedBubbleWidth) maxTextLayoutSize else textLayoutSize
    val bubbleSize = Size(
        width = actualMaxTextLayoutSize.width + paddingLeft + paddingRight,
        height = actualMaxTextLayoutSize.height + paddingTop + paddingBottom
    )
    drawRoundRect(
        color = bubbleColor,
        topLeft = Offset(
            x = bubbleCenterX - (actualMaxTextLayoutSize.width/2f) - paddingLeft,
            y = bubbleCenterY - (actualMaxTextLayoutSize.height/2f) - paddingTop
        ),
        cornerRadius = bubbleRadius,
        size = bubbleSize
    )

    drawText(textLayout,
        color = textColor,
        topLeft = Offset(
            x = bubbleCenterX - (textLayoutSize.width/2f),
            y = bubbleCenterY - (textLayoutSize.height/2f)
        )
    )
}

private fun DrawScope.drawRing(
    ringCenterX: Float,
    ringCenterY: Float,
    ringOuterRadius: Dp,
    ringInnerRadius: Dp,
    ringOuterColor: Color,
    ringInnerColor: Color,
) {
    //外层
    drawCircle(
        color = ringOuterColor,
        radius = ringOuterRadius.toPx(),
        center = Offset(ringCenterX, ringCenterY)
    )

    //内层
    drawCircle(
        color = ringInnerColor,
        radius = ringInnerRadius.toPx(),
        center = Offset(ringCenterX, ringCenterY)
    )
}


@Preview
@Composable
fun Preview_TextProgressBar() {
    Column(modifier = Modifier.background(Color.Black).padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TextProgressBar(percent = 0f, modifier = Modifier.width(200.dp))
        TextProgressBar(percent = 25f, modifier = Modifier.size(200.dp, 50.dp))
        TextProgressBar(percent = 50f, modifier = Modifier.size(200.dp, 50.dp))
        TextProgressBar(percent = 75f, modifier = Modifier.size(200.dp, 50.dp))
        TextProgressBar(percent = 100f, modifier = Modifier.size(200.dp, 50.dp))
    }
}