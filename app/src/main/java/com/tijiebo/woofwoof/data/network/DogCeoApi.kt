package com.tijiebo.woofwoof.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface DogCeoApi {
    @GET("breed/{breed}/images/random")
    suspend fun getBreedImage(
        @Path("breed", encoded = true) breed: String
    ): DogBreedImageResponse
}

@JsonClass(generateAdapter = true)
data class DogBreedImageResponse(
    @field:Json(name = "message") val imageUrl: String
)
