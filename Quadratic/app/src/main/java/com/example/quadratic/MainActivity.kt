package com.example.quadratic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quadratic.ui.theme.QuadraticTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuadraticTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuadraticScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun QuadraticScreen(modifier: Modifier = Modifier) {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = a,
            onValueChange = { a = it },
            label = { Text("a coefficient") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = b,
            onValueChange = { b = it },
            label = { Text("b coefficient") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = c,
            onValueChange = { c = it },
            label = { Text("c coefficient") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                val x = a.toDoubleOrNull()
                val y = b.toDoubleOrNull()
                val z = c.toDoubleOrNull()
                result = if (x == null || y == null || z == null) {
                    "Please enter valid numbers for a, b and c"
                } else {
                    solveQuadratic(x, y, z)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
        ) {
            Text("Solve")
        }

        Text(result, style = MaterialTheme.typography.headlineSmall)
    }
}

fun solveQuadratic(a: Double, b: Double, c: Double): String {
    if (a == 0.0) return "The equation is not quadratic (a = 0)"
    val d = b * b - 4 * a * c
    return when {
        d > 0 -> {
            val r1 = (-b - sqrt(d)) / (2 * a)
            val r2 = (-b + sqrt(d)) / (2 * a)
            "The equation has 2 distinct solutions: " +
                "x1 = ${minOf(r1, r2).pretty()}, x2 = ${maxOf(r1, r2).pretty()}"
        }
        d == 0.0 -> "The equation has 1 double solution: x = ${(-b / (2 * a)).pretty()}"
        else -> "The equation has no real solutions"
    }
}

private fun Double.pretty() = "%.2f".format(this)

@Preview(showBackground = true)
@Composable
fun QuadraticScreenPreview() {
    QuadraticTheme {
        QuadraticScreen()
    }
}
