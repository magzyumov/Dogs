package ru.magzyumov.dogs.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.magzyumov.dogs.model.database.IDogsDao
import ru.magzyumov.dogs.model.entity.DogEntity
import ru.magzyumov.dogs.model.response.BreedsResponse
import ru.magzyumov.dogs.model.response.BreedsResponse.*
import ru.magzyumov.dogs.util.IDogsRequest
import javax.inject.Inject

class DogsRepository @Inject constructor(val dogsDao: IDogsDao, val dogsRequest: IDogsRequest){

    fun insert(garage: DogEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dogsDao.insert(garage)
        }
    }

    fun delete(garage: DogEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dogsDao.delete(garage)
        }
    }

    fun update(garage: DogEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dogsDao.update(garage)
            Log.e("DEBUG", "update is called in repo")
        }
    }

    fun deleteById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dogsDao.deleteById(id)
        }
    }

    fun getAll(): LiveData<List<DogEntity>> {
        return dogsDao.getAll()
    }

    fun getById(id: Int): LiveData<DogEntity> {
        return dogsDao.getById(id)
    }


    fun getAllBreeds(): LiveData<List<Breed>> {
        val result: MutableLiveData<List<Breed>> = MutableLiveData()
        dogsRequest.getAllBreedsFromServer()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseAllBreeds)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<List<Breed>>() {
                override fun onSuccess(breeds: List<Breed>) {
                    result.value = breeds
                }

                override fun onError(e: Throwable) {
                    Log.e("GetAllBreeds", e.localizedMessage.orEmpty())
                }
            })

        return result
    }

    private val parseAllBreeds: Function1<BreedsResponse, List<Breed>> =
        { allBreeds: BreedsResponse ->

            val breedsResult: MutableList<Breed> = ArrayList()

            val listOfBreeds = allBreeds.message.toString()
                .replace("\\{|\\}".toRegex(),"")
                .split("], ".toRegex())

            for (breed: String in listOfBreeds){
                val ii = breed.plus("]").split("=")
                breedsResult.add(
                    Breed(ii[0].capitalize(),
                    ii[1].replace("\\[|\\]".toRegex(),"")
                        .trim()
                        .splitToSequence(", ")
                        .filter { it.isNotBlank() }
                        .toList()))
            }
            breedsResult
        }


    fun getSubBreeds(subBreed: String): LiveData<SubBreeds> {
        val result: MutableLiveData<SubBreeds> = MutableLiveData()
        dogsRequest.getSubBreedsFromServer(subBreed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseSubBreeds)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<SubBreeds>() {
                override fun onSuccess(subBreeds: SubBreeds) {
                    result.value = subBreeds
                }
                override fun onError(e: Throwable) {
                    Log.e("GetSubBreeds", e.localizedMessage.orEmpty())
                }
            })
        return result
    }

    private val parseSubBreeds: Function1<BreedsResponse, SubBreeds> =
        { allSubBreeds: BreedsResponse ->

            var list = allSubBreeds.message.toString()
                .replace("\\[|\\]".toRegex(),"")
                .trim()
                .splitToSequence(", ")
                .filter { it.isNotBlank() }
                .toList()

            SubBreeds(list)
        }


    fun getImagesForBreed(breed: String): LiveData<BreedImages> {
        val result: MutableLiveData<BreedImages> = MutableLiveData()
        dogsRequest.getImagesForBreed(breed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseImagesForBreed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<BreedImages>() {
                override fun onSuccess(breedImages: BreedImages) {
                    result.value = breedImages
                }
                override fun onError(e: Throwable) {
                    Log.e("GetImagesForBreed", e.localizedMessage.orEmpty())
                }
            })
        return result
    }

    fun getImagesForBreed(breed: String, subBreed: String): LiveData<BreedImages> {
        val result: MutableLiveData<BreedImages> = MutableLiveData()
        dogsRequest.getImagesForBreed(breed, subBreed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseImagesForBreed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<BreedImages>() {
                override fun onSuccess(breedImages: BreedImages) {
                    result.value = breedImages
                }
                override fun onError(e: Throwable) {
                    Log.e("GetImagesForBreed", e.localizedMessage.orEmpty())
                }
            })
        return result
    }

    private val parseImagesForBreed: Function1<BreedsResponse, BreedImages> =
        { allSubBreeds: BreedsResponse ->

            val list = allSubBreeds.message.toString()
                .replace("\\[|\\]".toRegex(),"")
                .trim()
                .splitToSequence(", ")
                .filter { it.isNotBlank() }
                .toList()

            BreedImages(list)
        }

}
