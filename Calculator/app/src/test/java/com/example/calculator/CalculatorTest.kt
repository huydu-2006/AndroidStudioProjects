package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorTest {
    private val calc = Calculator()

    private fun enter(text: String) = text.forEach { c ->
        when (c) {
            '+', '-', 'x', '/' -> calc.pressOperator(c)
            '=' -> calc.evaluate()
            else -> calc.digit(c)
        }
    }

    @Test
    fun `adds and chains operators`() {
        enter("12+30+5")
        assertEquals("12+30+5", calc.expression)
        enter("=")
        assertEquals("47", calc.display)
    }

    @Test
    fun `evaluates chained expression left to right`() {
        enter("3-5+1=")
        assertEquals("-1", calc.display)
    }

    @Test
    fun `subtracts and multiplies`() {
        enter("7x8-6=")
        assertEquals("50", calc.display)
    }

    @Test
    fun `multiplication and division go before addition and subtraction`() {
        enter("2+3x4=")
        assertEquals("14", calc.display)
        enter("10-6/2=")
        assertEquals("7", calc.display)
    }

    @Test
    fun `negative number can start the expression`() {
        enter("-5+3=")
        assertEquals("-2", calc.display)
        calc.clear() // '-' now continues from the result, so reset before a fresh negative entry
        enter("-5x3=")
        assertEquals("-15", calc.display)
    }

    @Test
    fun `operator after result continues from result`() {
        enter("5+3=")
        enter("-2=")
        assertEquals("6", calc.display)
    }

    @Test
    fun `leading zero is replaced while typing`() {
        enter("05")
        assertEquals("5", calc.display)
        assertEquals("5", calc.expression)
    }

    @Test
    fun `operator after backspacing a negative result is ignored`() {
        enter("3-5=")
        enter("+")
        calc.backspace()
        calc.backspace()
        enter("+5=")
        assertEquals("-5", calc.display)
    }

    @Test
    fun `integer division truncates`() {
        enter("7/2=")
        assertEquals("3", calc.display)
    }

    @Test
    fun `divide by zero shows error until next digit`() {
        enter("5/0=")
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
