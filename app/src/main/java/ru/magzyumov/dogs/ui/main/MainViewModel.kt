package ru.magzyumov.dogs.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.magzyumov.dogs.data.entity.FavouritesEntity
import ru.magzyumov.dogs.data.entity.FavouritesEntity.*
import ru.magzyumov.dogs.data.response.BreedsResponse.*
import ru.magzyumov.dogs.repository.DogsRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dogsRepository: DogsRepository
): ViewModel() {

    fun insertFavourite(favourite: FavouritesEntity) {
        dogsRepository.insertFavourite(favourite)
    }

    fun deleteFavouriteByPhoto(photo: String) {
        dogsRepository.deleteFavouriteByPhoto(photo)
    }

    fun getAllFavourite(): LiveData<List<FavouritesCount>> {
        return dogsRepository.getAllFavourite()
    }

    fun getFavouriteImages(breed: String): LiveData<List<String>> {
        return dogsRepository.getFavouriteImages(breed)
    }

    fun getNetworkStatus(): LiveData<String>{
        return dogsRepository.getNetworkStatus()
    }

    fun getAllBreeds(): LiveData<List<Breed>>{
        dogsRepository.getAllBreeds()
        return dogsRepository.getListOfBreeds()
    }

    fun getSubBreeds(subBreed: String): LiveData<SubBreeds>{
        dogsRepository.getSubBreeds(subBreed.toLowerCase())
        return dogsRepository.getListOfSubBreeds()
    }

    fun getImagesForBreed(breed: String): LiveData<BreedImages>{
        dogsRepository.getImagesForBreed(breed.toLowerCase())
        return dogsRepository.getListOfImages()
    }

    fun getImagesForBreed(breed: String, subBreed: String): LiveData<BreedImages>{
        dogsRepository.getImagesForBreed(breed.toLowerCase(), subBreed.toLowerCase())
        return dogsRepository.getListOfImages()
    }
}