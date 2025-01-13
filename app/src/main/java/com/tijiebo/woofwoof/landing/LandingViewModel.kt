package com.tijiebo.woofwoof.landing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tijiebo.woofwoof.domain.HighScoreUseCase
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.util.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val highScoreUseCase: HighScoreUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var highScore by mutableIntStateOf(highScoreUseCase.getHighScore().value)
        private set

    val breeds: List<DogBreed> = DogBreed.entries.shuffled()

    init {
        viewModelScope.launch(ioDispatcher) {
            highScoreUseCase.getHighScore().collectLatest {
                highScore = it
            }
        }
    }
}
