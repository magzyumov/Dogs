package ru.magzyumov.dogs.repository

import androidx.lifecycle.LiveData
import ru.magzyumov.dogs.data.entity.FavouritesEntity
import ru.magzyumov.dogs.data.response.BreedsResponse


interface IDogsRepository {

    fun getNetworkStatus(): LiveData<String>
    fun getListOfBreeds(): LiveData<List<BreedsResponse.Breed>>
    fun getListOfSubBreeds(): LiveData<BreedsResponse.SubBreeds>
    fun getListOfImages(): LiveData<BreedsResponse.BreedImages>

    fun insertFavourite(favourite: FavouritesEntity)

    fun deleteFavouriteByPhoto(photo: String)

    fun getAllFavourite(): LiveData<List<FavouritesEntity.FavouritesCount>>

    fun getFavouriteImages(breed: String): LiveData<List<String>>

    fun getAllBreeds()

    fun getSubBreeds(subBreed: String)

    fun getImagesForBreed(breed: String)

    fun getImagesForBreed(breed: String, subBreed: String)
}