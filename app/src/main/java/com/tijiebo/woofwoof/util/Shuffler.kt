package com.tijiebo.woofwoof.util

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
internal abstract class ShufflerModule {
    @Binds
    internal abstract fun bindShuffler(impl: ShufflerImpl): Shuffler
}
interface Shuffler {
    fun <T> invoke(input: List<T>): List<T>
}

internal class ShufflerImpl @Inject constructor() : Shuffler {
    override fun <T> invoke(input: List<T>): List<T> {
        return input.shuffled()
    }
}
