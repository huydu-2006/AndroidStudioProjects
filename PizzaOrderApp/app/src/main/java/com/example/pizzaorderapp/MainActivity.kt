package com.example.pizzaorderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzaorderapp.ui.theme.PizzaOrderAppTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzaOrderAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PizzaApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private val PIZAA_PRICE = mapOf("Small".to(90000), "Medium".to(120000), "Large".to(150000))
private val PIZAA_SIZE = listOf("Small", "Medium", "Large")
private val TOPPING = listOf("Cheese", "Mushroom", "Sausage", "Olives")
private val TOPPING_FEE = 15000
private val DELIVERY_FEE = 20000
private val defaultFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

@Composable
fun PizzaApp(modifier: Modifier = Modifier) {
    var pizzaSize by remember { mutableStateOf("") }

    var takeCheese by remember { mutableStateOf(false) }
    var takeMushroom by remember { mutableStateOf(false) }
    var takeSausage by remember { mutableStateOf(false) }
    var takeOlives by remember { mutableStateOf(false) }

    var homeDelivery by remember { mutableStateOf(false) }
    var customerName by remember { mutableStateOf("") }
    var alertMessage by remember { mutableStateOf<String?>(null) }
    var totalFee = (PIZAA_PRICE[pizzaSize] ?: 0) +
            (if (takeCheese) TOPPING_FEE else 0) +
            (if (takeMushroom) TOPPING_FEE else 0) +
            (if (takeSausage) TOPPING_FEE else 0) +
            (if (takeOlives) TOPPING_FEE else 0) +
            (if (homeDelivery) DELIVERY_FEE else 0)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "Pizza Order",
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider()
        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text(text = "Name") },
            placeholder = { Text(text = "Customer name") }
        )
        Text(
            text = "Choose pizza size",
            fontWeight = FontWeight.Bold
        )
        Column {
            PIZAA_SIZE.forEach { size ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = pizzaSize == size,
                        onClick = { pizzaSize = size }
                    )
                    Text (
                        text = size
                    )
                    PIZAA_PRICE[size]?.let {
                        Text(
                            text = defaultFormat.format(it),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
        HorizontalDivider()
        Text(
            text = "Extra toppings(+"+ defaultFormat.format(TOPPING_FEE) + " each)",
            fontWeight = FontWeight.Bold
        )

        TOPPING.forEachIndexed { index, topping ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox (
                    checked = when (index) {
                        0 -> takeCheese
                        1 -> takeMushroom
                        2 -> takeSausage
                        else -> takeOlives
                    },
                    onCheckedChange = { when (index) {
                        0 -> takeCheese = !takeCheese
                        1 -> takeMushroom = !takeMushroom
                        2 -> takeSausage = !takeSausage
                        else -> takeOlives = !takeOlives
                    } }
                )
                Text(text = topping)
            }
        }
        HorizontalDivider()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Home delivery",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Delivery fee: $DELIVERY_FEE"
                )
            }
            Switch (
                checked = homeDelivery,
                onCheckedChange = {homeDelivery  = !homeDelivery}
            )
        }
        HorizontalDivider()
        Row {
            Text (
                text = "Total price",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = defaultFormat.format(totalFee),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Button(
            onClick = {
                alertMessage = if (pizzaSize == "") "Please choose a pizza size"
                else "Order placed successfully!"
            },
            enabled = customerName.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
        ) {
            Text(text = "Place Order")
        }
    }

    alertMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { alertMessage = null },
            confirmButton = {
                TextButton(onClick = { alertMessage = null }) {
                    Text(text = "OK")
                }
            },
            text = { Text(text = message) }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PizzaAppPreview() {
    PizzaOrderAppTheme {
        PizzaApp()
    }
}