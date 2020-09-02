package ru.magzyumov.dogs.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BreedsResponse {
    @SerializedName("message")
    @Expose
    val message: Any? = null

    @SerializedName("status")
    @Expose
    val status: String? = null

    data class Breed(val breed: String, val subBreeds: List<String>)

    data class SubBreeds(val subBreeds: List<String>)

    data class BreedImages(val images: List<String>)
}