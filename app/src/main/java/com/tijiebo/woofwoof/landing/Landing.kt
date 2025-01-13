package com.tijiebo.woofwoof.landing

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tijiebo.woofwoof.R
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.ui.ComposeRoute
import com.tijiebo.woofwoof.ui.theme.WoofwoofScreen

fun NavGraphBuilder.landing(onStartQuestionnaire: () -> Unit) {
    composable<ComposeRoute.Landing> {
        val viewModel = hiltViewModel<LandingViewModel>()
        LandingScreen(
            breeds = viewModel.breeds,
            highScore = viewModel.highScore,
            onStartQuestionnaire = onStartQuestionnaire
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LandingScreen(
    breeds: List<DogBreed>,
    highScore: Int,
    onStartQuestionnaire: () -> Unit
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
                val carouselState = rememberCarouselState { breeds.size }
                HorizontalMultiBrowseCarousel(
                    state = carouselState,
                    preferredItemWidth = 186.dp,
                    itemSpacing = 8.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.padding(vertical = 32.dp),
                    flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(carouselState)
                ) { index ->
                    val item = breeds[index]
                    Image(
                        modifier = Modifier
                            .height(205.dp)
                            .maskClip(MaterialTheme.shapes.extraLarge),
                        painter = painterResource(id = item.drawableResId),
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                    )
                }
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    stringResource(R.string.landing_text),
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { onStartQuestionnaire.invoke() },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        stringResource(R.string.landing_cta),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    stringResource(R.string.high_score, highScore.toString().padStart(2, '0')),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
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
        LandingScreen(
            breeds = DogBreed.entries,
            highScore = 9,
            onStartQuestionnaire = {}
        )
    }
}
