package com.tijiebo.woofwoof.questionnaire

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tijiebo.woofwoof.domain.GenerateFirstQuestionUseCase
import com.tijiebo.woofwoof.domain.GenerateQuestionUseCase
import com.tijiebo.woofwoof.domain.HighScoreUseCase
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.model.Question
import com.tijiebo.woofwoof.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val generateQuestionUseCase: GenerateQuestionUseCase,
    private val highScoreUseCase: HighScoreUseCase,
    generateFirstQuestionUseCase: GenerateFirstQuestionUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var uiState by mutableStateOf(
        UiState(streak = 0, question = generateFirstQuestionUseCase.invoke())
    )
        private set

    fun evaluateOption(option: DogBreed) {
        val question = uiState.question
        if (option == question.correctOption) {
            handleCorrectOptionSelected()
        } else {
            uiState = uiState.copy(showCorrectAnswer = question.correctOption)
        }
    }

    private fun handleCorrectOptionSelected() {
        val currentStreak = uiState.streak
        uiState = uiState.copy(
            streak = currentStreak + 1,
            showConfetti = true
        )
        highScoreUseCase.saveHighScore(currentStreak + 1)
        getNextQuestion()
    }

    private fun getNextQuestion() {
        viewModelScope.launch(ioDispatcher) {
            val nextQuestion = viewModelScope.async { generateQuestionUseCase.invoke() }.await()
            viewModelScope.async { delay(CONFETTI_DELAY) }.await()
            uiState = uiState.copy(
                question = nextQuestion,
                showConfetti = false
            )
        }
    }

    fun dismissDialog() {
        uiState = uiState.copy(showCorrectAnswer = null)
    }

    data class UiState(
        val streak: Int,
        val question: Question,
        val showCorrectAnswer: DogBreed? = null,
        val showConfetti: Boolean = false
    )

    companion object {
        private const val CONFETTI_DELAY = 750L
    }
}
