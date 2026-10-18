package com.example.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Integer calculator state; Compose observes the display properties. */
class Calculator {
    var display by mutableStateOf("0")
        private set
    var expression by mutableStateOf("")
        private set
    var hasError by mutableStateOf(false)
        private set

    private var accumulator = 0L
    private var pending: Char? = null
    private var fresh = true // next digit starts a new entry

    fun digit(d: Char) {
        if (!d.isDigit()) return // dot button is not used in this assignment
        if (hasError) reset()
        when {
            fresh -> display = d.toString()
            display == "0" -> display = d.toString()
            display.length < MAX_DIGITS -> display += d
            else -> return
        }
        fresh = false
    }

    fun pressOperator(op: Char) {
        if (hasError) return
        val current = display.toLong()
        if (pending != null && !fresh) {
            val result = apply(current) ?: return divideByZero()
            display = result.toString()
        } else {
            accumulator = current
        }
        pending = op
        expression = "$display $op"
        fresh = true
    }

    fun evaluate() {
        if (hasError || pending == null) return
        val result = apply(display.toLong()) ?: return divideByZero()
        display = result.toString()
        expression = ""
        pending = null
        fresh = true
    }

    fun clearEntry() {
        // keeps the pending operation and accumulator
        display = "0"
        hasError = false
        fresh = false
    }

    fun clear() {
        reset()
        fresh = true
    }

    fun backspace() {
        if (hasError) return
        display = display.dropLast(1).let { if (it.isEmpty() || it == "-") "0" else it }
        fresh = false
    }

    private fun apply(b: Long): Long? {
        val a = accumulator
        // integer division truncates toward zero
        val result = when (pending) {
            '+' -> a + b
            '−' -> a - b
            '×' -> a * b
            '÷' -> if (b == 0L) return null else a / b
            else -> b
        }
        // shortcut: Long arithmetic, overflow wraps silently; upgrade to BigInteger if needed
        accumulator = result
        return result
    }

    private fun divideByZero() {
        display = "Cannot divide by zero"
        expression = ""
        accumulator = 0
        pending = null
        fresh = true
        hasError = true
    }

    private fun reset() {
        display = "0"
        expression = ""
        accumulator = 0
        pending = null
        hasError = false
    }

    private companion object {
        const val MAX_DIGITS = 15
    }
}
