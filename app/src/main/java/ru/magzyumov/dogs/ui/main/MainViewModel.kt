package ru.magzyumov.dogs.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.magzyumov.dogs.model.entity.FavouritesCountEntity
import ru.magzyumov.dogs.model.entity.FavouritesEntity
import ru.magzyumov.dogs.model.response.BreedsResponse.*
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

    fun getAllFavourite(): LiveData<List<FavouritesCountEntity>> {
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
        dogsRepository.getSubBreeds(subBreed)
        return dogsRepository.getListOfSubBreeds()
    }

    fun getImagesForBreed(breed: String): LiveData<BreedImages>{
        dogsRepository.getImagesForBreed(breed)
        return dogsRepository.getListOfImages()
    }

    fun getImagesForBreed(breed: String, subBreed: String): LiveData<BreedImages>{
        dogsRepository.getImagesForBreed(breed, subBreed)
        return dogsRepository.getListOfImages()
    }
}