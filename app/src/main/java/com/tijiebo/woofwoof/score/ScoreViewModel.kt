package com.tijiebo.woofwoof.score

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.tijiebo.woofwoof.domain.HighScoreUseCase
import com.tijiebo.woofwoof.ui.ComposeRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    highScoreUseCase: HighScoreUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState: UiState

    init {
        val streak = savedStateHandle.toRoute<ComposeRoute.Score>().streak
        val currentHighScore = highScoreUseCase.getHighScore().value
        uiState = UiState(
            streak = streak,
            highScore = currentHighScore
        )
    }

    data class UiState(
        val streak: Int = 0,
        val highScore: Int = 0
    ) {
        val hasNewHighScore: Boolean
            get() = streak >= highScore && streak > 0
    }
}
