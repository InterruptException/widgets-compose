package top.x86.widgets.compose.progressindicator

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
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
import kotlinx.coroutines.delay
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.roundToInt

@OptIn(ExperimentalTextApi::class)
@Composable
fun TextProgressBar(modifier: Modifier = Modifier,
                    percent: Float, //进度的百分比 0<= percent >=100
                    barHeightDp : Dp = 4.dp,  //进度条的厚度
                    ringOuterWidth: Dp = 2.dp, //圆环外圈的宽度
                    ringInnerRadius: Dp = 4.dp, //圆环内部半径
                    ringOuterColor: Color = MaterialTheme.colorScheme.primary, //圆环外圈颜色
                    ringInnerColor: Color = Color.White, //圆环内部颜色
                    barFinishedColor: Color = MaterialTheme.colorScheme.primary, //进度条已完成部分的颜色
                    barUnfinishedColor: Color = Color(0xFFE6E2FF), //进度条未完成部分的颜色
                    textColor: Color = Color.White, //气泡内文本颜色
                    textStyle: TextStyle = LocalTextStyle.current, //气泡内文本样式
                    bubbleColor: Color = MaterialTheme.colorScheme.primary, //气泡的颜色
                    bubbleRadiusDp: Dp = 2.dp, //气泡的圆角半径
                    bubblePadding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 2.dp), //气泡内部与文本之间的padding
                    isBubbleAtTop: Boolean = true, //气泡是否处于进度条上方
                    verticalSpace1: Dp = 4.dp,//圆环与三角形的间距
                    verticalSpace2: Dp = 0.dp,//三角形与气泡的间距
                    fixedBubbleWidth: Boolean = true, //固定气泡宽度不随文字字数改变 (”0%“ 到 ”100%“)，true表示不论是否为”100%“都按"100%"的宽度算
                    triangleHeightDp: Dp = 4.dp, //三角形的高
                    triangleWidthDp: Dp = 4.dp, //三角形的宽
                    triangleColor: Color = MaterialTheme.colorScheme.primary, //三角形的颜色
                    enabledHighlight: Boolean = false,
                    @FloatRange(0.0, 1.0) highlightWidthRange: Float = 0.8f,
                    moveInterval: Long = 30,
                    moveStep: Float = 0.03f,
                    barBrush: Brush? = null
) {
    val p = percent.coerceIn(0f, 100f)
    val textMeasurer = rememberTextMeasurer()
    val textStyle2 = remember(textStyle) {
        textStyle.copy(
            color = textColor
        )
    }
    val text = remember(p) {
        "${p.roundToInt()}%"
    }

    var barHighlightOffset by remember {
        mutableStateOf(0f)
    }

    if (enabledHighlight) {
        LaunchedEffect(Unit) {
            while (p < 100f) {
                var nextOffset = barHighlightOffset + moveStep
                if (nextOffset > 1f + highlightWidthRange/2f) {
                    nextOffset = -highlightWidthRange/2f
                }
                barHighlightOffset = nextOffset
                delay(moveInterval)
            }
        }
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
    val canvasMinHeightDp = bubbleHeightDp  + verticalSpace2 + triangleHeightDp + verticalSpace1 + max(barHeightDp, (ringOuterWidth + ringInnerRadius) * 2)
    Box(modifier = Modifier
        .defaultMinSize(minWidth = canvasMinWidthDp, minHeight = canvasMinHeightDp)
        .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            Modifier
                .fillMaxWidth()
                .matchParentSize()) {
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
            val ringOuterRadius = ringInnerRadius + ringOuterWidth
            val bubbleCenterY = if (isBubbleAtTop) {
                bubbleSize.height / 2f
            } else {
                max(barHeight, ringOuterRadius.toPx() * 2) + verticalSpace1.toPx() + triangleHeight + verticalSpace2.toPx() + bubbleHeightDp.toPx() / 2f
            }
            val triangleCenterY = if (isBubbleAtTop) {
                (bubbleSize.height + verticalSpace2.toPx() + triangleHeight/2f)
            }  else {
                max(barHeight, ringOuterRadius.toPx() * 2) + verticalSpace1.toPx() + triangleHeight /2f
            }
            val barCenterY = if (isBubbleAtTop) {
                bubbleSize.height + verticalSpace2.toPx() + triangleHeight + verticalSpace1.toPx() + max(barHeight / 2f, ringOuterRadius.toPx())
            } else {
                max(barHeight / 2f, ringOuterRadius.toPx())
            }
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
                bothSideSpace = bothSidesSpace,
                barBrush = barBrush,
                enabledHighlight = enabledHighlight,
                barHighlightOffset = barHighlightOffset,
                highlightWidthRange = highlightWidthRange)
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
                triangleColor = triangleColor,
                isBubbleAtTop = isBubbleAtTop)
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
                              bothSideSpace: Float,
                              barBrush: Brush? = null,
                              enabledHighlight: Boolean = false,
                              @FloatRange(0.0, 1.0) barHighlightOffset: Float,
                              @FloatRange(0.0, 1.0) highlightWidthRange: Float

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

    if (enabledHighlight) {
        drawBarHighlight(
            barFinishedLeft = barLeftX,
            barFinishedRight = barMiddleX,
            barY = barY,
            barColorFinished = barFinishedColor,
            highlightColor = Color.White,
            highlightWidthRange = highlightWidthRange,
            highlightOffsetCenterX = barHighlightOffset,
            barHeight = barHeight
        )
    }
}

private fun DrawScope.drawBarHighlight(barFinishedLeft: Float, barFinishedRight: Float, barY: Float, barHeight: Float,
                                       barColorFinished: Color, highlightColor: Color,
                                       @FloatRange(0.0, 1.0) highlightWidthRange: Float,
                                       @FloatRange(0.0, 1.0) highlightOffsetCenterX: Float){
    val barWidth = barFinishedRight - barFinishedLeft
    val highlightWidthMax = barWidth * highlightWidthRange
    val highlightCenterX = barFinishedLeft + barWidth * highlightOffsetCenterX
    drawBarHighlightLeft(
        barColorFinished = barColorFinished,
        highlightColor = highlightColor,
        highlightWidthMax = highlightWidthMax,
        highlightCenterX = highlightCenterX,
        barFinishedLeft = barFinishedLeft,
        barFinishedRight = barFinishedRight,
        barY = barY,
        barHeight = barHeight
    )
    drawBarHighlightRight(
        barColorFinished = barColorFinished,
        highlightColor = highlightColor,
        highlightWidthMax = highlightWidthMax,
        highlightCenterX = highlightCenterX,
        barFinishedLeft = barFinishedLeft,
        barFinishedRight = barFinishedRight,
        barY = barY,
        barHeight = barHeight
    )
}

private fun DrawScope.drawBarHighlightLeft(barColorFinished: Color, highlightColor: Color,
                                           highlightWidthMax: Float,
                                           highlightCenterX: Float,
                                           barFinishedLeft: Float,
                                           barFinishedRight: Float,
                                           barY: Float,
                                           barHeight: Float,
){
    val halfWidth = highlightWidthMax / 2f
    val highlightStartX = (highlightCenterX - halfWidth).coerceIn(barFinishedLeft, barFinishedRight)
    val highlightEndX = highlightCenterX.coerceIn(barFinishedLeft, barFinishedRight)
    val brush = Brush.horizontalGradient(listOf(barColorFinished, highlightColor), startX = highlightStartX, endX = highlightCenterX)

    drawLine(brush = brush,
        start = Offset(x = highlightStartX, y = barY),
        end = Offset(x = highlightEndX, y = barY),
        strokeWidth = barHeight,
        cap = StrokeCap.Round)
}

private fun DrawScope.drawBarHighlightRight(barColorFinished: Color, highlightColor: Color,
                                            highlightWidthMax: Float,
                                            highlightCenterX: Float,
                                            barFinishedLeft: Float,
                                            barFinishedRight: Float,
                                            barY: Float,
                                            barHeight: Float
){
    val halfWidth = highlightWidthMax / 2f
    val highlightStartX = highlightCenterX.coerceIn(barFinishedLeft, barFinishedRight)
    val highlightEndX = (highlightCenterX+halfWidth).coerceIn(barFinishedLeft, barFinishedRight)
    val brush = Brush.horizontalGradient(listOf(highlightColor, barColorFinished), startX = highlightCenterX, endX = highlightEndX)
    drawLine(brush = brush,
        start = Offset(x = highlightStartX, y = barY),
        end = Offset(x = highlightEndX, y = barY),
        strokeWidth = barHeight,
        cap = StrokeCap.Round)
}

private fun DrawScope.drawTriangleBlock(triangleCenterX: Float,
                                        triangleCenterY: Float,
                                        triangleWidth: Float,
                                        triangleHeight: Float,
                                        triangleColor: Color,
                                        isBubbleAtTop: Boolean = true,
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
    if (isBubbleAtTop) {
        drawPath(
            path = path,
            color = triangleColor
        )

    } else {
        rotate(180f, pivot = Offset (triangleCenterX, triangleCenterY)) {
            drawPath(
                path = path,
                color = triangleColor
            )
        }
    }
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
                                 layoutDirection: LayoutDirection,
                                 isBubbleAtTop: Boolean = true,
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


@Preview(
    heightDp = 1000,
    widthDp = 400
)
@Composable
fun Preview_TextProgressBar() {
    Surface {
        Column(modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            TextProgressBar(percent = 0f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth())
            TextProgressBar(percent = 0f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), isBubbleAtTop = false)
            TextProgressBar(percent = 25f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), verticalSpace2 = 2.dp)
            TextProgressBar(percent = 25f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), verticalSpace2 = 2.dp, isBubbleAtTop = false)
            TextProgressBar(percent = 50f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), verticalSpace1 = 0.dp , verticalSpace2 = 2.dp)
            TextProgressBar(percent = 50f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), verticalSpace1 = 0.dp , verticalSpace2 = 2.dp, isBubbleAtTop = false)
            TextProgressBar(percent = 75f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), ringOuterWidth = 5.dp)
            TextProgressBar(percent = 75f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), ringOuterWidth = 5.dp, isBubbleAtTop = false)
            TextProgressBar(percent = 100f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), ringInnerRadius = 6.dp)
            TextProgressBar(percent = 100f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), ringInnerRadius = 6.dp, isBubbleAtTop = false)

            TextProgressBar(percent = 100f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), triangleHeightDp = 10.dp)
            TextProgressBar(percent = 100f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), triangleHeightDp = 10.dp, isBubbleAtTop = false)

            TextProgressBar(percent = 100f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(),
                triangleHeightDp = 6.dp, triangleWidthDp = 12.dp, ringInnerRadius = 0.dp, ringOuterWidth = 0.dp)
            TextProgressBar(percent = 100f, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(), isBubbleAtTop = false,
                triangleHeightDp = 6.dp, triangleWidthDp = 12.dp, ringInnerRadius = 0.dp, ringOuterWidth = 0.dp)
        }
    }
}