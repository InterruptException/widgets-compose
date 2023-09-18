package top.x86.demo.androidlibprojcet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import top.x86.demo.androidlibprojcet.app.ui.theme.ThisProjectAppTheme
import top.x86.widgets.compose.HorizontalDashDivider
import top.x86.widgets.compose.progressindicator.TextProgressBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThisProjectAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppBody()
                }
            }
        }
    }
}

@Composable
fun AppBody() {
    Surface {
        Column {
            Text(text = "app")
            HorizontalDashDivider()
            TextProgressBar(modifier = Modifier.fillMaxWidth(), percent = 50f, enabledHighlight = true)
        }
    }
}

@Preview
@Composable
fun PreviewAppBody() {
    AppBody()
}