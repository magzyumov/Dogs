package ru.magzyumov.dogs.util

import io.reactivex.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import ru.magzyumov.dogs.model.response.BreedsResponse

interface IDogsRequest {
    @GET("breeds/list/all")
    fun getAllBreedsFromServer(): Single<BreedsResponse>

    @GET("breed/{subBreed}/list")
    fun getSubBreedsFromServer(@Path ("subBreed") breed: String): Single<BreedsResponse>

    @GET("breed/{breed}/images")
    fun getImagesForBreed(@Path ("breed") breed: String): Single<BreedsResponse>

    @GET("breed/{breed}/{subBreed}/images")
    fun getImagesForBreed(  @Path ("breed") breed: String,
                            @Path ("subBreed") subBreed: String): Single<BreedsResponse>

    companion object Factory {
        fun create(): IDogsRequest {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dog.ceo/api/")
                .build()

            return retrofit.create(IDogsRequest::class.java)
        }
    }
}