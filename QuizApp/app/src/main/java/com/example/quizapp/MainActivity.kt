package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

private val CorrectGreen = Color(0xFF00FF00)
private val WrongRed = Color(0xFFFF0000)

abstract class QuizQuestion(val text: String) {
    abstract val typeLabel: String
    abstract fun isCorrect(): Boolean
    abstract fun reset()
}


class Single(
    text: String,
    val options: List<String>,
    val correct: Int,
) : QuizQuestion(text) {
    override val typeLabel = "Single Choice"
    var selected by mutableStateOf<Int?>(null)
    override fun isCorrect() = selected == correct // kotlin '==' is .equal() calling, not like java
    override fun reset() {
        selected = null
    }
}

class Multiple(
    text: String,
    val options: List<String>,
    val correct: Set<Int>,
) : QuizQuestion(text) {
    override val typeLabel = "Multiple Choice"
    val selected = mutableStateListOf<Int>()
    override fun isCorrect() = selected.toSet() == correct
    override fun reset() {
        selected.clear()
    }
}

class Short(
    text: String,
    val answer: String,
) : QuizQuestion(text) {
    override val typeLabel = "Short Answer"
    var input by mutableStateOf("")
    override fun isCorrect() = input.trim().equals(answer, ignoreCase = true)
    override fun reset() {
        input = ""
    }
}

private fun createQuestions() = listOf(
    Single(
        text = "Which language is officially supported for Android development?",
        options = listOf("Kotlin", "Python", "Java", "Swift"),
        correct = 0,
    ),
    Multiple(
        text = "Which of the following are Jetpack Compose layout components?",
        options = listOf("Column", "TextField", "Row", "Button"),
        correct = setOf(0, 2),
    ),
    Short(
        text = "Which annotation is used to declare a composable function in Jetpack Compose?",
        answer = "Composable",
    ),
)

@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    val questions = remember { createQuestions() }
    var submitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Android Quiz", style = MaterialTheme.typography.headlineSmall)
            HorizontalDivider()
        }
        itemsIndexed(questions) { index, question ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Question ${index + 1}: ${question.typeLabel}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(question.text, style = MaterialTheme.typography.bodyMedium)
                when (question) {
                    is Single -> SingleChoice(question, submitted)
                    is Multiple -> MultipleChoice(question, submitted)
                    is Short -> ShortAnswer(question, submitted)
                }
                if (index < questions.lastIndex) HorizontalDivider()
            }
        }
        item {
            if (!submitted) {
                Button(onClick = { submitted = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Submit Answers")
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Your Score: ${questions.count { it.isCorrect() }} / ${questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    questions.forEachIndexed { index, question ->
                        Text(
                            text = "Question ${index + 1}: ${if (question.isCorrect()) "Correct" else "Incorrect"}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            questions.forEach { it.reset() }
                            submitted = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Reset Quiz")
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleChoice(question: Single, submitted: Boolean) {
    question.options.forEachIndexed { index, option ->
        val mark = markColor(submitted, index == question.correct, question.selected == index)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = question.selected == index,
                    enabled = !submitted,
                    role = Role.RadioButton,
                    onClick = { question.selected = index },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = question.selected == index,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = mark ?: MaterialTheme.colorScheme.primary,
                    unselectedColor = mark ?: MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Text(option, color = mark ?: MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun MultipleChoice(question: Multiple, submitted: Boolean) {
    question.options.forEachIndexed { index, option ->
        val checked = index in question.selected
        val mark = markColor(submitted, index in question.correct, checked)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = checked,
                    enabled = !submitted,
                    role = Role.Checkbox,
                    onValueChange = { if (it) question.selected.add(index) else question.selected.remove(index) },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = mark ?: MaterialTheme.colorScheme.primary,
                    uncheckedColor = mark ?: MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Text(option, color = mark ?: MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun ShortAnswer(question: Short, submitted: Boolean) {
    val mark = when {
        !submitted -> null
        question.isCorrect() -> CorrectGreen
        else -> WrongRed
    }
    OutlinedTextField(
        value = question.input,
        onValueChange = { question.input = it },
        label = { Text("Your answer") },
        readOnly = submitted,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = mark ?: MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = mark ?: MaterialTheme.colorScheme.onSurface,
        ),
    )
    if (submitted && !question.isCorrect()) {
        Text("Correct answer: ${question.answer}", color = CorrectGreen)
    }
}

private fun markColor(submitted: Boolean, correctOption: Boolean, selected: Boolean): Color? = when {
    !submitted -> null
    correctOption -> CorrectGreen
    selected -> WrongRed
    else -> null
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizAppTheme {
        QuizScreen()
    }
}
