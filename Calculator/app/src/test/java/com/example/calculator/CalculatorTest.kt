package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorTest {
    private val calc = Calculator()

    private fun enter(text: String) = text.forEach { c ->
        when (c) {
            '+', '−', '×', '÷' -> calc.pressOperator(c)
            '=' -> calc.evaluate()
            else -> calc.digit(c)
        }
    }

    @Test
    fun `adds and chains operators`() {
        enter("12+30+5=")
        assertEquals("47", calc.display)
    }

    @Test
    fun `subtracts and multiplies`() {
        enter("7×8−6=")
        assertEquals("50", calc.display)
    }

    @Test
    fun `integer division truncates`() {
        enter("7÷2=")
        assertEquals("3", calc.display)
    }

    @Test
    fun `divide by zero shows error until next digit`() {
        enter("5÷0=")
        assertTrue(calc.hasError)
        enter("7")
        assertFalse(calc.hasError)
        assertEquals("7", calc.display)
    }

    @Test
    fun `clear entry keeps pending operation`() {
        enter("12+5")
        calc.clearEntry()
        assertEquals("0", calc.display)
        enter("7=")
        assertEquals("19", calc.display)
    }

    @Test
    fun `clear resets everything`() {
        enter("12+5")
        calc.clear()
        assertEquals("0", calc.display)
        assertEquals("", calc.expression)
        enter("3=")
        assertEquals("3", calc.display)
    }

    @Test
    fun `backspace removes rightmost digit`() {
        enter("123")
        calc.backspace()
        assertEquals("12", calc.display)
        calc.backspace()
        calc.backspace()
        assertEquals("0", calc.display)
    }

    @Test
    fun `dot button is ignored`() {
        enter("1.5")
        assertEquals("15", calc.display)
    }
}
