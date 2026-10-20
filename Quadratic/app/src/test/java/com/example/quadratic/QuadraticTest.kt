package com.example.quadratic

import org.junit.Assert.assertEquals
import org.junit.Test

class QuadraticTest {
    @Test
    fun twoDistinctSolutions() {
        assertEquals(
            "The equation has 2 distinct solutions: x1 = 1, x2 = 2",
            solveQuadratic(1.0, -3.0, 2.0),
        )
    }

    @Test
    fun doubleSolution() {
        assertEquals("The equation has 1 double solution: x = 1", solveQuadratic(1.0, -2.0, 1.0))
    }

    @Test
    fun noRealSolutions() {
        assertEquals("The equation has no real solutions", solveQuadratic(1.0, 0.0, 1.0))
    }

    @Test
    fun notQuadratic() {
        assertEquals("The equation is not quadratic (a = 0)", solveQuadratic(0.0, 1.0, 1.0))
    }
}
