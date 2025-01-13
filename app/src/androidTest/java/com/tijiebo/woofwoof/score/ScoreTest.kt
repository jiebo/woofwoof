package com.tijiebo.woofwoof.score

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.tijiebo.woofwoof.R
import com.tijiebo.woofwoof.ui.theme.WoofwoofScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ScoreTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val navigateToLanding: () -> Unit = mockk(relaxed = true)

    private fun setComposeContent(uiState: ScoreViewModel.UiState) {
        composeTestRule.setContent {
            WoofwoofScreen {
                ScoreScreen(
                    uiState = uiState,
                    navigateToLanding = navigateToLanding
                )
            }
        }
    }

    @Test
    fun verifyNewHighScoreUi() {
        val uiState = ScoreViewModel.UiState(
            streak = 9,
            highScore = 9
        )
        setComposeContent(uiState)
        composeTestRule
            .onNodeWithText(context.getString(R.string.score_new_high_score))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.new_high_score, "09"))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.score_new_high_score_cta))
            .assertIsDisplayed()
            .performClick()
        verify { navigateToLanding.invoke() }
    }

    @Test
    fun verifyNoNewHighScoreUi() {
        val uiState = ScoreViewModel.UiState(
            streak = 8,
            highScore = 9
        )
        setComposeContent(uiState)
        composeTestRule
            .onNodeWithText(context.getString(R.string.score_no_new_high_score))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.high_score, "09"))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.your_score, "08"))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.score_no_new_high_score_cta))
            .assertIsDisplayed()
            .performClick()
        verify { navigateToLanding.invoke() }
    }
}
