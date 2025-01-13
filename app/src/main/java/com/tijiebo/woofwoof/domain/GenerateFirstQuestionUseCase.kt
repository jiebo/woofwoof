package com.tijiebo.woofwoof.domain

import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.model.Question
import com.tijiebo.woofwoof.util.Shuffler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
internal abstract class GenerateFirstQuestionModule {
    @Binds
    internal abstract fun bindGenerateFirstQuestionUseCase(impl: GenerateFirstQuestionUseCaseImpl): GenerateFirstQuestionUseCase
}

interface GenerateFirstQuestionUseCase {
    fun invoke(): Question
}

internal class GenerateFirstQuestionUseCaseImpl @Inject constructor(
    private val shuffler: Shuffler
) : GenerateFirstQuestionUseCase {
    override fun invoke(): Question {
        val options = shuffler.invoke(DogBreed.entries).take(4)
        val correctOption = options.first()
        val shuffledOptions = shuffler.invoke(options)
        return Question(
            options = shuffledOptions,
            correctOption = correctOption,
            imageUrl = null
        )
    }
}
