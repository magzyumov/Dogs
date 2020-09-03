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
    val dogsRepository: DogsRepository
) : ViewModel() {

    fun insert(dog: DogEntity) {
        dogsRepository.insert(dog)
    }

    fun delete(dog: DogEntity) {
        dogsRepository.delete(dog)
    }

    fun update(dog: DogEntity) {
        Log.e("DEBUG", "update is called in viewmodel")
        dogsRepository.update(dog)
    }

    fun deleteById(id: Int) {
        dogsRepository.deleteById(id)
    }

    fun getAll(): LiveData<List<DogEntity>> {
        Log.e("DEBUG", "View model getAllGarages")
        return dogsRepository.getAll()
    }

    fun getById(id: Int): LiveData<DogEntity> {
        Log.e("DEBUG", "View model getOneGarage")
        return dogsRepository.getById(id)
    }

    fun getNetworkStatus(): LiveData<String>{
        return dogsRepository.getNetworkStatus()
    }

    fun getAllBreeds(): LiveData<List<Breed>>{
        return dogsRepository.getAllBreeds()
    }


    fun getSubBreeds(subBreed: String): LiveData<SubBreeds>{
        return dogsRepository.getSubBreeds(subBreed)
    }

    fun getImagesForBreed(breed: String): LiveData<BreedImages>{
        return dogsRepository.getImagesForBreed(breed)
    }

    fun getImagesForBreed(breed: String, subBreed: String): LiveData<BreedImages>{
        return dogsRepository.getImagesForBreed(breed, subBreed)
    }

}