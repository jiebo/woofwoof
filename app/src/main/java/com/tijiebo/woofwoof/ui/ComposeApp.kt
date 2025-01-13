package com.tijiebo.woofwoof.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tijiebo.woofwoof.landing.landing
import kotlinx.serialization.Serializable

@Composable
fun ComposeApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ComposeRoute.Landing) {
        landing(
            onStartQuestionnaire = {
                // TODO: Navigate to questionnaire
            }
        )
    }
}

sealed interface ComposeRoute {
    @Serializable
    data object Landing : ComposeRoute
}
