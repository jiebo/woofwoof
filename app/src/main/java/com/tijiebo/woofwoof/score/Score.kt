package com.tijiebo.woofwoof.score

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tijiebo.woofwoof.R
import com.tijiebo.woofwoof.ui.ComposeRoute
import com.tijiebo.woofwoof.ui.theme.WoofwoofScreen

fun NavGraphBuilder.score(navigateToLanding: () -> Unit) {
    composable<ComposeRoute.Score> {
        val viewModel = hiltViewModel<ScoreViewModel>()
        ScoreScreen(
            uiState = viewModel.uiState,
            navigateToLanding = navigateToLanding
        )
    }
}

@Composable
internal fun ScoreScreen(
    uiState: ScoreViewModel.UiState,
    navigateToLanding: () -> Unit
) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.widthIn(max = 560.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.hasNewHighScore) {
                    NewHighScore(uiState.highScore, navigateToLanding)
                } else {
                    NoNewHighScore(uiState.highScore, uiState.streak, navigateToLanding)
                }
            }
        }
    }
}

@Composable
private fun NewHighScore(
    highScore: Int,
    onCtaClicked: () -> Unit
) {
    val highScoreLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.high_score))
    LottieAnimation(
        highScoreLottie,
        iterations = Int.MAX_VALUE,
        modifier = Modifier.padding(horizontal = 36.dp).height(240.dp)
    )
    Text(
        stringResource(R.string.score_new_high_score),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        stringResource(R.string.new_high_score, highScore.toString().padStart(2, '0')),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Medium
    )
    Button(
        onClick = onCtaClicked,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            stringResource(R.string.score_new_high_score_cta),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun NoNewHighScore(
    highScore: Int,
    streak: Int,
    onCtaClicked: () -> Unit
) {
    val noHighScoreLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_high_score))
    LottieAnimation(
        noHighScoreLottie,
        iterations = Int.MAX_VALUE,
        modifier = Modifier.padding(horizontal = 36.dp).height(240.dp)
    )
    Text(
        stringResource(R.string.score_no_new_high_score),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        stringResource(R.string.high_score, highScore.toString().padStart(2, '0')),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Medium
    )
    Text(
        stringResource(R.string.your_score, streak.toString().padStart(2, '0')),
        style = MaterialTheme.typography.bodyLarge
    )
    Button(
        onClick = onCtaClicked,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            stringResource(R.string.score_no_new_high_score_cta),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(name = "Phone / Portrait")
@Composable
private fun PreviewLandingScreen_HighScore() {
    WoofwoofScreen {
        ScoreScreen(
            uiState = ScoreViewModel.UiState(
                streak = 10,
                highScore = 9
            ),
            navigateToLanding = {}
        )
    }
}

@Preview(name = "Phone / Portrait")
@Preview(name = "Phone / Portrait - Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Small Phone / Portrait", widthDp = 480, heightDp = 640)
@Preview(name = "Tablet / Portrait", widthDp = 800, heightDp = 1280, device = Devices.PIXEL_C)
@Preview(name = "Tablet / Landscape", widthDp = 1280, heightDp = 800, device = Devices.PIXEL_C)
@Composable
private fun PreviewLandingScreen() {
    WoofwoofScreen {
        ScoreScreen(
            uiState = ScoreViewModel.UiState(
                streak = 9,
                highScore = 10
            ),
            navigateToLanding = {}
        )
    }
}
