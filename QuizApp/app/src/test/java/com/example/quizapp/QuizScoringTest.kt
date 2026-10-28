package com.example.quizapp

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizScoringTest {
    @Test
    fun singleChoice() {
        val question = QuizQuestion.Single("q", listOf("a", "b"), correct = 0)
        assertFalse(question.isCorrect())
        question.selected = 1
        assertFalse(question.isCorrect())
        question.selected = 0
        assertTrue(question.isCorrect())
    }

    @Test
    fun multipleChoiceNeedsExactSet() {
        val question = QuizQuestion.Multiple("q", listOf("a", "b", "c"), correct = setOf(0, 2))
        question.selected.add(0)
        assertFalse(question.isCorrect())
        question.selected.add(2)
        assertTrue(question.isCorrect())
        question.selected.add(1)
        assertFalse(question.isCorrect())
    }

    @Test
    fun shortAnswerIgnoresCaseAndSpaces() {
        val question = QuizQuestion.Short("q", "Composable")
        question.input = "  composable "
        assertTrue(question.isCorrect())
    }

    @Test
    fun resetClearsAnswer() {
        val question = QuizQuestion.Short("q", "Composable")
        question.input = "Composable"
        question.reset()
        assertFalse(question.isCorrect())
    }
}
