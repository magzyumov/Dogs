package ru.magzyumov.dogs.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.magzyumov.dogs.model.entity.DogEntity
import ru.magzyumov.dogs.model.response.BreedsResponse.*
import ru.magzyumov.dogs.repository.DogsRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dogsRepository: DogsRepository
): ViewModel() {

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