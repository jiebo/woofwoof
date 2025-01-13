package com.tijiebo.woofwoof.questionnaire

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.tijiebo.woofwoof.R
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.model.Question
import com.tijiebo.woofwoof.ui.theme.WoofwoofScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class QuestionnaireTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val evaluateOption: (DogBreed) -> Unit = mockk(relaxed = true)

    private fun setComposeContent() {
        composeTestRule.setContent {
            WoofwoofScreen {
                QuestionnaireScreen(
                    streak = STREAK,
                    question = question,
                    evaluateOption = evaluateOption
                )
            }
        }
    }

    @Test
    fun verifyQuestionnaireScreenUi() {
        setComposeContent()
        composeTestRule
            .onNodeWithText(context.getString(R.string.streak, "09"))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(DogBreed.PUG.title)
            .assertIsDisplayed()
            .performClick()
        verify { evaluateOption.invoke(DogBreed.PUG) }
        composeTestRule
            .onNodeWithText(DogBreed.DOBERMAN.title)
            .assertIsDisplayed()
            .performClick()
        verify { evaluateOption.invoke(DogBreed.DOBERMAN) }
        composeTestRule
            .onAllNodesWithText(DogBreed.SHIHTZU.title)[1] // Correct answer is shown on screen as well
            .assertIsDisplayed()
            .performClick()
        verify { evaluateOption.invoke(DogBreed.SHIHTZU) }
        composeTestRule
            .onNodeWithText(DogBreed.CHOW.title)
            .assertIsDisplayed()
            .performClick()
        verify { evaluateOption.invoke(DogBreed.CHOW) }
    }

    companion object {
        private const val STREAK = 9
        private val question = Question(
            options = listOf(DogBreed.PUG, DogBreed.DOBERMAN, DogBreed.SHIHTZU, DogBreed.CHOW),
            correctOption = DogBreed.SHIHTZU,
            imageUrl = null
        )
    }
}
