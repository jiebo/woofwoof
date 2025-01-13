package com.tijiebo.woofwoof.domain

import com.tijiebo.woofwoof.data.network.DogBreedImageResponse
import com.tijiebo.woofwoof.data.network.DogCeoApi
import com.tijiebo.woofwoof.model.DogBreed
import com.tijiebo.woofwoof.model.Question
import com.tijiebo.woofwoof.util.Shuffler
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class GenerateQuestionUseCaseTest {
    private val api: DogCeoApi = mockk()
    private val shuffler: Shuffler = mockk()

    private lateinit var useCase: GenerateQuestionUseCase

    @Before
    fun setUp() {
        every { shuffler.invoke(DogBreed.entries) } returns DOG_BREED_LIST
        every { shuffler.invoke(DOG_BREED_LIST.take(4)) } returns SHUFFLED_DOG_BREED_LIST
        useCase = GenerateQuestionUseCaseImpl(
            api = api,
            shuffler = shuffler
        )
    }

    @Test
    fun `Verify API successful`() = runTest {
        // Given API is always successful
        coEvery { api.getBreedImage(any()) } returns DogBreedImageResponse(IMAGE_URL)

        // When use case is triggered
        val result = useCase.invoke()

        // Then verify Question contains IMAGE_URL
        assertEquals(
            Question(
                options = SHUFFLED_DOG_BREED_LIST,
                correctOption = DogBreed.PUG,
                imageUrl = IMAGE_URL
            ),
            result
        )
    }

    @Test
    fun `Verify API unsuccessful`() = runTest {
        // Given API always fails
        coEvery { api.getBreedImage(any()) } throws RuntimeException()

        // When use case is triggered
        val result = useCase.invoke()

        // Then verify Question does not contain IMAGE_URL
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
        private const val IMAGE_URL = "image_url"
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
