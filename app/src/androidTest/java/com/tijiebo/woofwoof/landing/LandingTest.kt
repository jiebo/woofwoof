package com.tijiebo.woofwoof.landing

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.tijiebo.woofwoof.R
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.ui.theme.WoofwoofScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class LandingTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val onStartQuestionnaire: () -> Unit = mockk(relaxed = true)

    private fun setComposeContent() {
        composeTestRule.setContent {
            WoofwoofScreen {
                LandingScreen(
                    breeds = DogBreed.entries,
                    highScore = HIGH_SCORE,
                    onStartQuestionnaire = onStartQuestionnaire
                )
            }
        }
    }

    @Test
    fun verifyLandingUi() {
        setComposeContent()
        composeTestRule
            .onNodeWithText(context.getString(R.string.app_name))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.landing_text))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.high_score, "09"))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(context.getString(R.string.landing_cta))
            .assertIsDisplayed()
            .performClick()
        verify { onStartQuestionnaire.invoke() }
    }

    companion object {
        private const val HIGH_SCORE = 9
    }
}
