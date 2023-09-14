package top.x86.widgets.compose.switcher

import android.opengl.ETC1.getWidth
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.x86.compose.more.modifiers.clickableWithoutRipple
import java.lang.Float.max


@Composable
fun TabSwitcher(
    modifier: Modifier,
    isSelectedLeft: Boolean = true,
    textLeft: String = "Left",
    textRight: String = "Right",
    bgColorSelected: Color = MaterialTheme.colorScheme.primary,
    bgColorUnselected: Color = Color(238, 248, 255, 1),
    textColorSelected: Color = Color.White,
    textColorUnselected: Color = Color.Gray,//Color(189, 200, 210, 1),
    angle: Float = 76.0f,//倾斜角度
    shadowOpacity: Float = 0.8f,
    shadowElevation: Dp = 5.dp,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    textStyleSelected: TextStyle = LocalTextStyle.current,
    textStyleUnselected: TextStyle = LocalTextStyle.current
) {
    val textStyleLeft = if (isSelectedLeft) {
        textStyleSelected.copy(color = textColorSelected)
    } else {
        textStyleUnselected.copy(color = textColorUnselected)
    }
    val textStyleRight = if (!isSelectedLeft) {
        textStyleSelected.copy(color = textColorSelected)
    } else {
        textStyleUnselected.copy(color = textColorUnselected)
    }

    val textLayoutLeft = textMeasurer.measure(textLeft, textStyleLeft)
    val textLayoutRight = textMeasurer.measure(textRight, textStyleRight)



    Canvas(modifier = modifier) {
        //计算参数
        val middle = size.width / 2.0f
        val middleWidth = getWidth(angle, size.height / 2.0f)
        val topMiddle = middle + middleWidth
        val bottomMiddle = middle - middleWidth
        val maxHalfWidth: Float = max(topMiddle, bottomMiddle)

        val textOffsetLeft = Offset(
            x = (middle - textLayoutLeft.size.width) / 2f,
            y = (size.height - textLayoutLeft.size.height) /2f
        )

        val textOffsetRight = Offset(
            x = middle + (middle - textLayoutLeft.size.width) / 2f,
            y = (size.height - textLayoutLeft.size.height) /2f
        )

//        val selectedPaint = Paint()
//        selectedPaint.color = selectedBgColor
//        val unselectedPaint = Paint()
//        unselectedPaint.color = unselectedBgColor
//        val leftPaint: Paint = if (isSelectedLeft) selectedPaint else unselectedPaint
//        val rightPaint: Paint = if (isSelectedLeft) unselectedPaint else selectedPaint

        val leftPath = Path().apply {
            reset()
            moveTo(0f, 0f)
            lineTo(topMiddle, 0f)
            lineTo(bottomMiddle, size.height)
            lineTo(0f, size.height)
            lineTo(0f, 0f)
            close()
        }

        val rightPath = Path().apply {
            reset()
            moveTo(topMiddle, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(bottomMiddle, size.height)
            lineTo(topMiddle, 0f)
            close();
        }

        if (isSelectedLeft) {
            drawPath(rightPath, bgColorUnselected)
            drawText(textLayoutRight, textColorUnselected, textOffsetRight)
            drawPath(leftPath, bgColorSelected)
            drawText(textLayoutLeft, textColorSelected, textOffsetLeft)
        } else {
            drawPath(leftPath, bgColorUnselected)
            drawText(textLayoutLeft, textColorUnselected, textOffsetLeft)
            drawPath(rightPath, bgColorSelected)
            drawText(textLayoutRight, textColorSelected, textOffsetRight)
        }
    }
}

private fun getWidth(angle: Float, height: Float): Float {
    return if (angle / 90.0f % 2 == 1.0f) {
        0.0f
    } else {
        height / Math.tan(angle * Math.PI / 180.0f).toFloat()
    }
}

@Composable
fun TabSwitcher2(
    modifier: Modifier,
    isSelectedLeft: Boolean = true,
    textLeft: String = "Left",
    textRight: String = "Right",
    bgColorSelected: Color = MaterialTheme.colorScheme.primary,
    bgColorUnselected: Color = Color(238, 248, 255, 1),
    textColorSelected: Color = Color.White,
    textColorUnselected: Color = Color.Gray,//Color(189, 200, 210, 1),
    angle: Float = 76.0f,//倾斜角度
    shadowOpacity: Float = 0.8f,
    shadowElevation: Dp = 5.dp,
    textStyleSelected: TextStyle = LocalTextStyle.current,
    textStyleUnselected: TextStyle = LocalTextStyle.current,
    onValueChange: (Boolean)->Unit = {}
) {
    val textStyleLeft = if (isSelectedLeft) {
        textStyleSelected.copy(color = textColorSelected)
    } else {
        textStyleUnselected.copy(color = textColorUnselected)
    }
    val textStyleRight = if (!isSelectedLeft) {
        textStyleSelected.copy(color = textColorSelected)
    } else {
        textStyleUnselected.copy(color = textColorUnselected)
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Box(modifier = Modifier
            .clickableWithoutRipple {
                onValueChange(true)
            }
            .fillMaxHeight()
            .weight(1f)
            .drawBehind {
                //计算参数
                val middle = size.width
                val middleWidth = getWidth(angle, size.height / 2.0f)
                val topMiddle = middle + middleWidth
                val bottomMiddle = middle - middleWidth

                val leftPath = Path().apply {
                    reset()
                    moveTo(0f, 0f)
                    lineTo(topMiddle, 0f)
                    lineTo(bottomMiddle, size.height)
                    lineTo(0f, size.height)
                    lineTo(0f, 0f)
                    close()
                }

                drawPath(leftPath, if (isSelectedLeft) bgColorSelected else bgColorUnselected)
            },
            contentAlignment = Alignment.Center
        ) {
            Text(text = textLeft, style = textStyleLeft)
        }
        Box(modifier = Modifier
            .clickableWithoutRipple {
                onValueChange(false)
            }
            .fillMaxHeight()
            .weight(1f)
            .drawBehind {
                //计算参数
                val middle = 0
                val middleWidth = getWidth(angle, size.height / 2.0f)
                val topMiddle = middle + middleWidth
                val bottomMiddle = middle - middleWidth

                val rightPath = Path().apply {
                    reset()
                    moveTo(topMiddle, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(bottomMiddle, size.height)
                    lineTo(topMiddle, 0f)
                    close();
                }
                drawPath(rightPath,  if (!isSelectedLeft) bgColorSelected else bgColorUnselected)
            }
            , contentAlignment = Alignment.Center) {
            Text(text = textRight, style = textStyleRight)
        }
    }
}

@Preview
@Composable
fun PreviewTabSwitcher() {
    Surface(color = Color.LightGray) {
        Box(modifier = Modifier.size(400.dp, 100.dp), contentAlignment = Alignment.Center) {

            Box(modifier = Modifier.shadow(2.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), clip = true)) {
                TabSwitcher2(modifier = Modifier.size(300.dp, 60.dp).background(Color.White), isSelectedLeft = false)
            }
        }
    }
}