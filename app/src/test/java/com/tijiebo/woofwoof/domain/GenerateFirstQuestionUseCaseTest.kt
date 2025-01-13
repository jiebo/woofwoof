package com.tijiebo.woofwoof.domain

import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.model.Question
import com.tijiebo.woofwoof.util.Shuffler
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class GenerateFirstQuestionUseCaseTest {
    private val shuffler: Shuffler = mockk()

    private lateinit var useCase: GenerateFirstQuestionUseCase

    @Before
    fun setUp() {
        every { shuffler.invoke(DogBreed.entries) } returns DOG_BREED_LIST
        every { shuffler.invoke(DOG_BREED_LIST.take(4)) } returns SHUFFLED_DOG_BREED_LIST
        useCase = GenerateFirstQuestionUseCaseImpl(
            shuffler = shuffler
        )
    }

    @Test
    fun `Verify generateFirstQuestion`() = runTest {
        // When generate first question
        val result = useCase.invoke()

        // Then verify Question does not contains IMAGE_URL
        assertEquals(
            Question(
                options = SHUFFLED_DOG_BREED_LIST,
                correctOption = DogBreed.PUG,
                imageUrl = null
            ),
            result
        )
    }

    companion object {
        private val DOG_BREED_LIST = listOf(
            DogBreed.PUG,
            DogBreed.DOBERMAN,
            DogBreed.SHIHTZU,
            DogBreed.CHOW,
            DogBreed.AFGHAN_HOUND
        )
        private val SHUFFLED_DOG_BREED_LIST = listOf(
            DogBreed.DOBERMAN,
            DogBreed.SHIHTZU,
            DogBreed.PUG,
            DogBreed.CHOW
        )
    }
}
