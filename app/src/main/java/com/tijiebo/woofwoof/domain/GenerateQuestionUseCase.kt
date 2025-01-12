package com.tijiebo.woofwoof.domain

import com.tijiebo.woofwoof.data.network.DogCeoApi
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
internal abstract class GenerateQuestionModule {
    @Binds
    internal abstract fun bindGenerateQuestionUseCase(impl: GenerateQuestionUseCaseImpl): GenerateQuestionUseCase
}

interface GenerateQuestionUseCase {
    suspend fun invoke(): Question
}

internal class GenerateQuestionUseCaseImpl @Inject constructor(
    private val api: DogCeoApi,
    private val shuffler: Shuffler
) : GenerateQuestionUseCase {
    override suspend fun invoke(): Question {
        val options = shuffler.invoke(DogBreed.entries).take(4)
        val correctOption = options.first()
        val shuffledOptions = shuffler.invoke(options)
        return try {
            val imageUrl = api.getBreedImage(correctOption.code).imageUrl
            Question(
                options = shuffledOptions,
                correctOption = correctOption,
                imageUrl = imageUrl
            )
        } catch (ex: Exception) {
            Question(
                options = shuffledOptions,
                correctOption = correctOption,
                imageUrl = null
            )
        }
    }
}
