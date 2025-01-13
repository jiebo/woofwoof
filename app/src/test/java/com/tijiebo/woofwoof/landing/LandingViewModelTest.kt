package com.tijiebo.woofwoof.landing

import com.tijiebo.woofwoof.domain.HighScoreUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LandingViewModelTest {

    private val highScoreFlow = MutableStateFlow(0)
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: LandingViewModel

    @Before
    fun setUp() {
        val highScoreUseCase: HighScoreUseCase = mockk()
        every { highScoreUseCase.getHighScore() } returns highScoreFlow
        viewModel = LandingViewModel(
            highScoreUseCase = highScoreUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `Verify high score logic`() {
        // Given high score is initially zero
        // When view model is first initialised
        // Then high score should be zero
        assertEquals(0, viewModel.highScore)

        // When high score is now nine
        highScoreFlow.tryEmit(9)
        // Then view model should be reflected
        assertEquals(9, viewModel.highScore)
    }
}
