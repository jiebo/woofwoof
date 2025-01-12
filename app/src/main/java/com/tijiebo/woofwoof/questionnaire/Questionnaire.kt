package com.tijiebo.woofwoof.questionnaire

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tijiebo.woofwoof.R
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.model.Question
import com.tijiebo.woofwoof.ui.ComposeRoute
import com.tijiebo.woofwoof.ui.theme.VibrantColor
import com.tijiebo.woofwoof.ui.theme.WoofwoofScreen
import com.tijiebo.woofwoof.ui.theme.fontTitillium
import com.tijiebo.woofwoof.ui.util.TextWithStyle
import com.tijiebo.woofwoof.ui.util.textMultiStyle

fun NavGraphBuilder.questionnaire(
    navigateToScore: (Int) -> Unit
) {
    composable<ComposeRoute.Questionnaire> {
        val viewModel = hiltViewModel<QuestionnaireViewModel>()
        val uiState = viewModel.uiState
        BackHandler {
            // No-op; Disable all back clicks
        }
        QuestionnaireScreen(
            streak = uiState.streak,
            question = uiState.question,
            evaluateOption = viewModel::evaluateOption
        )
        val celebrateLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.celebrate))
        if (uiState.showConfetti) {
            Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.35f))) {
                LottieAnimation(celebrateLottie, iterations = Int.MAX_VALUE, speed = 0.8f)
            }
        }
        uiState.showCorrectAnswer?.let { correctAnswer ->
            CorrectAnswerDialog(correctAnswer) {
                viewModel.dismissDialog()
                navigateToScore.invoke(uiState.streak)
            }
        }
    }
}

@Composable
internal fun QuestionnaireScreen(
    streak: Int,
    question: Question,
    evaluateOption: (DogBreed) -> Unit
) {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize().padding(it)) {
            Spacer(Modifier.height(48.dp))
            CurrentStreak(streak)
            Spacer(Modifier.weight(1f))
            AsyncImage(
                model = question.correctOption.drawableResId,
                contentDescription = question.correctOption.title,
                error = painterResource(question.correctOption.drawableResId),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .heightIn(max = 320.dp)
                    .clip(RoundedCornerShape(32.dp))
            )
            Text(question.correctOption.title)
            Spacer(Modifier.weight(1f))
            QuestionnaireOptions(question, evaluateOption)
        }
    }
}

@Composable
private fun CurrentStreak(streak: Int) {
    Text(
        stringResource(R.string.streak, streak.toString().padStart(2, '0')),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun QuestionnaireOptions(
    question: Question,
    evaluateOption: (DogBreed) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().height(168.dp)) {
        with(question.options[0]) {
            QuestionnaireOption(
                modifier = Modifier.padding(8.dp, 4.dp, 4.dp, 4.dp),
                option = this,
                color = VibrantColor.red,
                onItemSelected = evaluateOption
            )
        }
        with(question.options[1]) {
            QuestionnaireOption(
                modifier = Modifier.padding(4.dp, 4.dp, 8.dp, 4.dp),
                option = this,
                color = VibrantColor.blue,
                onItemSelected = evaluateOption
            )
        }
    }
    Row(modifier = Modifier.fillMaxWidth().height(168.dp)) {
        with(question.options[2]) {
            QuestionnaireOption(
                modifier = Modifier.padding(8.dp, 4.dp, 4.dp, 8.dp),
                option = this,
                color = VibrantColor.yellow,
                onItemSelected = evaluateOption
            )
        }
        with(question.options[3]) {
            QuestionnaireOption(
                modifier = Modifier.padding(4.dp, 4.dp, 8.dp, 8.dp),
                option = this,
                color = VibrantColor.green,
                onItemSelected = evaluateOption
            )
        }
    }
}

@Composable
private fun RowScope.QuestionnaireOption(
    modifier: Modifier = Modifier,
    option: DogBreed,
    color: Color,
    onItemSelected: (DogBreed) -> Unit
) {
    Surface(
        onClick = { onItemSelected.invoke(option) },
        modifier = modifier
            .fillMaxHeight()
            .weight(1f),
        color = color,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            option.title,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = fontTitillium,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
private fun CorrectAnswerDialog(
    correctAnswer: DogBreed,
    navigateToScore: () -> Unit
) {
    AlertDialog(
        onDismissRequest = navigateToScore,
        confirmButton = {
            Button(onClick = navigateToScore) {
                Text(stringResource(R.string.show_correct_answer_dialog_cta))
            }
        },
        text = {
            Text(
                textMultiStyle(
                    originalText = stringResource(
                        R.string.show_correct_answer_dialog_text,
                        correctAnswer.title
                    ),
                    customTextList =
                    listOf(
                        TextWithStyle(
                            customText = correctAnswer.title,
                            MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    )
                ),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}

@Preview(name = "Phone / Portrait")
@Preview(name = "Phone / Portrait - Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Small Phone / Portrait", widthDp = 480, heightDp = 640)
@Preview(name = "Tablet / Portrait", widthDp = 800, heightDp = 1280, device = Devices.PIXEL_C)
@Preview(name = "Tablet / Landscape", widthDp = 1280, heightDp = 800, device = Devices.PIXEL_C)
@Composable
private fun PreviewLandingScreen() {
    WoofwoofScreen {
        QuestionnaireScreen(
            streak = 9,
            question = Question(
                options = listOf(DogBreed.PUG, DogBreed.DOBERMAN, DogBreed.SHIHTZU, DogBreed.CHOW),
                correctOption = DogBreed.SHIHTZU,
                imageUrl = null
            ),
            evaluateOption = {}
        )
    }
}
