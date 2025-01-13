package com.tijiebo.woofwoof.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val options: List<DogBreed>,
    val correctOption: DogBreed,
    val imageUrl: String?
)
