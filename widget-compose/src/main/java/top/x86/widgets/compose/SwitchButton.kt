package top.x86.widgets.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SwitchButton(leftSideText : String, rightSideText: String,
                 selectedBgColor: Color, unselectedBgColor: Color,
                 selectedTextColor: Color, unselectedTextColor: Color,
                 selectedLeftSide: Boolean = true,
                 middleSpace: Dp = 10.dp
) {
    Box(modifier = Modifier
        .padding(5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(unselectedTextColor)
        .padding(5.dp)
        .clip(RoundedCornerShape(10.dp))
    ) {
        val isLeft = rememberSaveable {
            mutableStateOf(selectedLeftSide)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(middleSpace)) {
            val leftSideTextColor = if (isLeft.value) selectedTextColor else unselectedTextColor
            val rightSideTextColor = if (isLeft.value) unselectedTextColor else selectedTextColor
            val bgColorLeft = if (isLeft.value) selectedBgColor else unselectedBgColor
            val bgColorRight = if (isLeft.value) unselectedBgColor else selectedBgColor

            Text(text = leftSideText, color = leftSideTextColor, modifier = Modifier
                .drawBehind {
                    drawRoundRect(
                        bgColorLeft,
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )
                }
                .padding(vertical = 13.dp, horizontal = 23.dp))
            Text(text = rightSideText, color = rightSideTextColor, modifier = Modifier
                .drawBehind {
                    drawRoundRect(
                        bgColorRight,
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )
                }
                .padding(vertical = 13.dp, horizontal = 23.dp))
        }
    }
}

@Preview
@Composable
fun PreviewSwitchButton() {
    Box(modifier = Modifier.padding(20.dp)) {
        SwitchButton(
            leftSideText = "aaa",
            rightSideText = "bbb",
            selectedBgColor = Color.White,
            unselectedBgColor = Color.Gray,
            selectedTextColor = Color.Blue,
            unselectedTextColor = Color.DarkGray
        )
    }
}