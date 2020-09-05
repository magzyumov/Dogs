package ru.magzyumov.dogs.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.magzyumov.dogs.data.entity.FavouritesEntity
import ru.magzyumov.dogs.data.entity.FavouritesEntity.*
import ru.magzyumov.dogs.data.response.BreedsResponse.*
import ru.magzyumov.dogs.repository.IDogsRepository
import javax.inject.Inject


class MainViewModel
@Inject constructor(
    private val mDogsRepository: IDogsRepository
): ViewModel() {

    fun insertFavourite(favourite: FavouritesEntity) {
        mDogsRepository.insertFavourite(favourite)
    }

    fun deleteFavouriteByPhoto(photo: String) {
        mDogsRepository.deleteFavouriteByPhoto(photo)
    }

    fun getAllFavourite(): LiveData<List<FavouritesCount>> {
        return mDogsRepository.getAllFavourite()
    }

    fun getFavouriteImages(breed: String): LiveData<List<String>> {
        return mDogsRepository.getFavouriteImages(breed)
    }

    fun getNetworkStatus(): LiveData<String>{
        return mDogsRepository.getNetworkStatus()
    }

    fun getAllBreeds(): LiveData<List<Breed>>{
        mDogsRepository.getAllBreeds()
        return mDogsRepository.getListOfBreeds()
    }

    fun getSubBreeds(subBreed: String): LiveData<SubBreeds>{
        mDogsRepository.getSubBreeds(subBreed.toLowerCase())
        return mDogsRepository.getListOfSubBreeds()
    }

    fun getImagesForBreed(breed: String): LiveData<BreedImages>{
        mDogsRepository.getImagesForBreed(breed.toLowerCase())
        return mDogsRepository.getListOfImages()
    }

    fun getImagesForBreed(breed: String, subBreed: String): LiveData<BreedImages>{
        mDogsRepository.getImagesForBreed(breed.toLowerCase(), subBreed.toLowerCase())
        return mDogsRepository.getListOfImages()
    }
}