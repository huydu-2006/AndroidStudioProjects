package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

private val buttonRows = listOf(
    listOf("C", "CE", "⌫", "÷"),
    listOf("7", "8", "9", "×"),
    listOf("4", "5", "6", "−"),
    listOf("1", "2", "3", "+"),
    listOf("0", ".", "="),
)

@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    val calc = remember { Calculator() }
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = calc.expression,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleLarge,
        )
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Text(
                text = calc.display,
                textAlign = TextAlign.End,
                style = if (calc.hasError) MaterialTheme.typography.titleLarge
                else MaterialTheme.typography.displayMedium,
            )
        }
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.7f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { label ->
                    Button(
                        onClick = { press(calc, label) },
                        modifier = Modifier.weight(if (label == "0") 2f else 1f).fillMaxHeight(),
                    ) {
                        Text(label, style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }
    }
}

private fun press(calc: Calculator, label: String) {
    when (label) {
        "C" -> calc.clear()
        "CE" -> calc.clearEntry()
        "⌫" -> calc.backspace()
        "=" -> calc.evaluate()
        "+", "−", "×", "÷" -> calc.pressOperator(label.single())
        else -> calc.digit(label.single())
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}
