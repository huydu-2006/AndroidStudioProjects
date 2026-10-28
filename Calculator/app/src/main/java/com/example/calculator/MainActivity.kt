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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    listOf("C", "CE", "BS", "/"),
    listOf("7", "8", "9", "x"),
    listOf("4", "5", "6", "-"),
    listOf("1", "2", "3", "+"),
    listOf("0", ".", "="),
)

@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    val calc = remember { Calculator() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Text(
                text = calc.expression.ifEmpty { calc.display },
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.displayLarge
            )
        }
        buttonRows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { label ->
                    Button(
                        onClick = { press(calc, label) },
                        modifier = Modifier
                            .weight(if (label == "0") 2f else 1f)
                            .fillMaxHeight(),
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
        "BS" -> calc.backspace()
        "=" -> calc.evaluate()
        "+", "-", "x", "/" -> calc.pressOperator(label.single())
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

class Calculator {
    var display by mutableStateOf("0") // current entry, or result
        private set
    var expression by mutableStateOf("") // whole expression, e.g. "12+30+5"
        private set
    var hasError by mutableStateOf(false)
        private set

    // next digit starts a new entry
    private val fresh get() = expression.isEmpty() || expression.last() in OPERATORS

    fun digit(d: Char) {
        if (!d.isDigit()) return // dot button is not used in this assignment
        if (hasError) reset()
        when {
            fresh -> {
                display = d.toString()
                expression += d
            }
            display == "0" -> {
                display = d.toString()
                expression = expression.dropLast(1) + d
            }
            display.length < MAX_DIGITS -> {
                display += d
                expression += d
            }
        }
    }

    fun pressOperator(op: Char) {
        if (hasError) return
        expression = when {
            expression.isEmpty() -> if (op == '-' && display == "0") "-" else display + op // "-" starts a negative number only from a fresh zero; otherwise continue from the result
            expression == "-" -> expression // ignore operators until a digit follows
            expression.last() in OPERATORS -> expression.dropLast(1) + op
            else -> expression + op
        }
    }

    fun evaluate() {
        if (hasError) return
        val expr = expression.trimEnd { it in OPERATORS }
        if (expr.isEmpty()) return
        val negative = expr.startsWith('-')
        val body = if (negative) expr.drop(1) else expr
        val nums = body.split('+', '-', 'x', '/').map { it.toLongOrNull() ?: return }.toMutableList()
        val ops = body.filter { it in OPERATORS }.toMutableList()
        if (ops.size != nums.size - 1) return
        if (negative) nums[0] = -nums[0]

        // x and / bind tighter than + and -
        var i = 0
        while (i < ops.size) {
            if (ops[i] == 'x' || ops[i] == '/') {
                if (ops[i] == '/' && nums[i + 1] == 0L) return divideByZero()
                nums[i] = if (ops[i] == 'x') nums[i] * nums[i + 1] else nums[i] / nums[i + 1]
                nums.removeAt(i + 1)
                ops.removeAt(i)
            } else i++
        }
        var acc = nums[0]
        ops.forEachIndexed { j, op -> acc = if (op == '+') acc + nums[j + 1] else acc - nums[j + 1] }

        display = acc.toString()
        expression = ""
    }

    fun clearEntry() {
        expression = expression.trimEnd { it.isDigit() }
        display = "0"
        hasError = false
    }

    fun clear() {
        reset()
    }

    fun backspace() {
        if (hasError || expression.isEmpty()) return
        expression = expression.dropLast(1)
        display = expression.takeLastWhile { it.isDigit() }.ifEmpty { "0" }
    }

    private fun divideByZero() {
        display = "Cannot divide by zero"
        expression = ""
        hasError = true
    }

    private fun reset() {
        display = "0"
        expression = ""
        hasError = false
    }

    private companion object {
        const val MAX_DIGITS = 5
        const val OPERATORS = "+-x/"
    }
}