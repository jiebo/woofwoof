package com.tijiebo.woofwoof.questionnaire

import com.tijiebo.woofwoof.domain.GenerateFirstQuestionUseCase
import com.tijiebo.woofwoof.domain.GenerateQuestionUseCase
import com.tijiebo.woofwoof.domain.HighScoreUseCase
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.model.Question
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class QuestionnaireViewModelTest {

    private val generateQuestionUseCase: GenerateQuestionUseCase = mockk()
    private val generateFirstQuestionUseCase: GenerateFirstQuestionUseCase = mockk()
    private val highScoreUseCase: HighScoreUseCase = mockk(relaxUnitFun = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: QuestionnaireViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { generateFirstQuestionUseCase.invoke() } returns firstQuestion
        viewModel = QuestionnaireViewModel(
            generateQuestionUseCase = generateQuestionUseCase,
            highScoreUseCase = highScoreUseCase,
            generateFirstQuestionUseCase = generateFirstQuestionUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Verify Evaluate Option logic - Correct option selected`() {
        // Given user had not interacted with UI
        // Then UI state should be default
        assertEquals(
            QuestionnaireViewModel.UiState(streak = 0, question = firstQuestion),
            viewModel.uiState
        )

        // When user selects the correct option
        viewModel.evaluateOption(
            option = DogBreed.PUG
        )

        // Then update the UI to show confetti while question has not been updated
        assertEquals(
            QuestionnaireViewModel.UiState(
                streak = 1,
                question = firstQuestion,
                showConfetti = true
            ),
            viewModel.uiState
        )
        // And attempt to save high score
        verify { highScoreUseCase.saveHighScore(1) }

        // Given next question is generated
        coEvery { generateQuestionUseCase.invoke() } returns question
        // When scheduler advances but confetti delay has not returned
        testDispatcher.scheduler.runCurrent()
        // Then next question is generated
        coVerify { generateQuestionUseCase.invoke() }
        // And UI should not proceed to next question
        assertEquals(firstQuestion, viewModel.uiState.question)

        // When confetti delay has elapsed
        testDispatcher.scheduler.advanceTimeBy(1_000L)
        // Then UI should proceed to next question
        assertEquals(question, viewModel.uiState.question)
    }

    @Test
    fun `Verify Evaluate Option logic - Incorrect option selected`() {
        // Given user had not interacted with UI
        // Then UI state should be default
        assertEquals(QuestionnaireViewModel.UiState(streak = 0, question = firstQuestion), viewModel.uiState)

        // When user selects an incorrect option
        viewModel.evaluateOption(option = DogBreed.CHOW)

        // Then show correct answer
        assertEquals(DogBreed.PUG, viewModel.uiState.showCorrectAnswer)
        // And verify that use cases were not triggered
        verify {
            highScoreUseCase wasNot Called
            generateQuestionUseCase wasNot Called
        }
    }

    companion object {
        private val firstQuestion = Question(
            options = listOf(DogBreed.PUG, DogBreed.DOBERMAN, DogBreed.SHIHTZU, DogBreed.CHOW),
            correctOption = DogBreed.PUG,
            imageUrl = null
        )
        private val question = Question(
            options = listOf(DogBreed.PUG, DogBreed.DOBERMAN, DogBreed.SHIHTZU, DogBreed.CHOW),
            correctOption = DogBreed.SHIHTZU,
            imageUrl = null
        )
    }
}
