package com.tijiebo.woofwoof.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tijiebo.woofwoof.landing.landing
import com.tijiebo.woofwoof.questionnaire.questionnaire
import com.tijiebo.woofwoof.score.score
import kotlinx.serialization.Serializable

@Composable
fun ComposeApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ComposeRoute.Landing) {
        landing(
            onStartQuestionnaire = {
                navController.navigate(ComposeRoute.Questionnaire)
            }
        )
        questionnaire(
            navigateToScore = { streak ->
                navController.navigate(ComposeRoute.Score(streak)) {
                    popUpTo<ComposeRoute.Landing> {
                        inclusive = false
                    }
                }
            }
        )
        score(
            navigateToLanding = {
                navController.popBackStack(ComposeRoute.Landing, false)
            }
        )
    }
}

sealed interface ComposeRoute {
    @Serializable
    data object Landing : ComposeRoute

    @Serializable
    data object Questionnaire : ComposeRoute

    @Serializable
    data class Score(val streak: Int) : ComposeRoute
}
