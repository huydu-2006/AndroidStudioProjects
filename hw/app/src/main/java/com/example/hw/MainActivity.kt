package com.example.hw
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LemonApp()
            }
        }
    }
}

@Composable
fun LemonApp() {
    var currentStep by remember { mutableIntStateOf(1) }
    var squeezesLeft by remember { mutableIntStateOf(0) }

    val imageResource: Int
    val textResource: Int
    val descriptionResource: Int

    when (currentStep) {
        1 -> {
            imageResource = R.drawable.lemon_tree
            textResource = R.string.lemon_select
            descriptionResource = R.string.lemon_tree_description
        }
        2 -> {
            imageResource = R.drawable.lemon_squeeze
            textResource = R.string.lemon_squeeze
            descriptionResource = R.string.lemon_description
        }
        3 -> {
            imageResource = R.drawable.lemon_drink
            textResource = R.string.lemon_drink
            descriptionResource = R.string.lemonade_description
        }
        else -> {
            imageResource = R.drawable.lemon_restart
            textResource = R.string.lemon_restart
            descriptionResource = R.string.empty_glass_description
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(textResource),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(imageResource),
                contentDescription = stringResource(descriptionResource),
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        BorderStroke(2.dp, Color(105, 205, 216)),
                        RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        when (currentStep) {
                            1 -> {
                                squeezesLeft = (2..4).random()
                                currentStep = 2
                            }
                            2 -> {
                                squeezesLeft--
                                if (squeezesLeft <= 0) {
                                    currentStep = 3
                                }
                            }
                            3 -> currentStep = 4
                            4 -> currentStep = 1
                        }
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LemonAppPreview() {
    MaterialTheme {
        LemonApp()
    }
}
