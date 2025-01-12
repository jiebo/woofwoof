package com.tijiebo.woofwoof.score

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tijiebo.woofwoof.domain.HighScoreUseCase
import com.tijiebo.woofwoof.ui.ComposeRoute
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class ScoreViewModelTest {

    private val highScoreUseCase: HighScoreUseCase = mockk()
    private val highScoreFlow = MutableStateFlow(0)
    private val savedStateHandle: SavedStateHandle = mockk()

    private val viewModel by lazy {
        ScoreViewModel(
            highScoreUseCase = highScoreUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Before
    fun setUp() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<ComposeRoute.Score>() } returns
            ComposeRoute.Score(streak = 9)
        every { highScoreUseCase.getHighScore() } returns highScoreFlow
    }

    @Test
    fun `Verify high score logic`() {
        // Given user had just entered Score screen and high score is 9
        highScoreFlow.tryEmit(9)
        // Then UI state should be updated that user has gotten a new high score
        assertEquals(
            ScoreViewModel.UiState(
                streak = 9,
                highScore = 9
            ),
            viewModel.uiState
        )
        assertTrue(viewModel.uiState.hasNewHighScore)
    }

    @Test
    fun `Verify no new high score logic`() {
        // Given user had just entered Score screen and high score is 10
        highScoreFlow.tryEmit(10)
        // Then UI state should be updated that user has not gotten a new high score
        assertEquals(
            ScoreViewModel.UiState(
                streak = 9,
                highScore = 10
            ),
            viewModel.uiState
        )
        assertFalse(viewModel.uiState.hasNewHighScore)
    }

    @Test
    fun `Verify no new high score logic - User's streak is zero`() {
        // Given user had just entered Score screen and high score is 0
        highScoreFlow.tryEmit(0)
        // And user's streak is 0
        every { savedStateHandle.toRoute<ComposeRoute.Score>() } returns
            ComposeRoute.Score(streak = 0)
        // Then UI state should be updated that user has not gotten a new high score
        assertEquals(
            ScoreViewModel.UiState(
                streak = 0,
                highScore = 0
            ),
            viewModel.uiState
        )
        assertFalse(viewModel.uiState.hasNewHighScore)
    }
}
