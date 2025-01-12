package com.tijiebo.woofwoof.domain

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.tijiebo.woofwoof.domain.HighScoreModule.Companion.HIGH_SCORE_PREFS
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class HighScoreModule {
    @Binds
    @Singleton
    internal abstract fun bindHighScoreUseCase(impl: HighScoreUseCaseImpl): HighScoreUseCase

    companion object {
        @Provides
        @Singleton
        @Named(HIGH_SCORE_PREFS)
        fun provideHighScorePreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(KEY, MODE_PRIVATE)
        }

        private const val KEY = "high_score_key"
        const val HIGH_SCORE_PREFS = "high_score_prefs"
    }
}

interface HighScoreUseCase {
    fun getHighScore(): StateFlow<Int>

    fun saveHighScore(highScore: Int)
}

internal class HighScoreUseCaseImpl @Inject constructor(
    @Named(HIGH_SCORE_PREFS) private val preferences: SharedPreferences
) : HighScoreUseCase {
    private val _highScoreFlow = MutableStateFlow(currentHighScore())

    override fun getHighScore(): StateFlow<Int> = _highScoreFlow.asStateFlow()

    override fun saveHighScore(highScore: Int) {
        if (highScore > currentHighScore()) {
            _highScoreFlow.tryEmit(highScore)
            preferences.edit {
                putInt(HIGH_SCORE_KEY, highScore)
            }
        }
    }

    private fun currentHighScore(): Int {
        return preferences.getInt(HIGH_SCORE_KEY, 0)
    }

    companion object {
        internal const val HIGH_SCORE_KEY = "high_score_key"
    }
}
