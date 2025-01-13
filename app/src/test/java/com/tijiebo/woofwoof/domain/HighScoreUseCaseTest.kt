package com.tijiebo.woofwoof.domain

import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import com.tijiebo.woofwoof.domain.HighScoreUseCaseImpl.Companion.HIGH_SCORE_KEY
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class HighScoreUseCaseTest {

    private val preferences = SPMockBuilder().createSharedPreferences()
    private lateinit var useCase: HighScoreUseCase

    @Before
    fun setUp() {
        useCase = HighScoreUseCaseImpl(
            preferences = preferences
        )
    }

    @Test
    fun `Verify High Score logic`() {
        // Given user had not interacted with app
        // Then high score should be zero
        assertEquals(0, useCase.getHighScore().value)

        // When user obtained a new high score
        useCase.saveHighScore(2)
        // Then high score should be two
        assertEquals(2, useCase.getHighScore().value)
        assertEquals(2, preferences.getInt(HIGH_SCORE_KEY, 0))

        // When user did not obtain a new high score
        useCase.saveHighScore(1)
        // Then high score should remain unchanged
        assertEquals(2, useCase.getHighScore().value)
        assertEquals(2, preferences.getInt(HIGH_SCORE_KEY, 0))

        // When user obtained a new high score
        useCase.saveHighScore(3)
        // Then high score should be three
        assertEquals(3, useCase.getHighScore().value)
        assertEquals(3, preferences.getInt(HIGH_SCORE_KEY, 0))
    }
}
