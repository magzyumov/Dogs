package ru.magzyumov.dogs.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.magzyumov.dogs.data.database.IDogsDao
import ru.magzyumov.dogs.data.entity.FavouritesEntity
import ru.magzyumov.dogs.data.entity.FavouritesEntity.*
import ru.magzyumov.dogs.data.response.BreedsResponse
import ru.magzyumov.dogs.data.response.BreedsResponse.*
import ru.magzyumov.dogs.util.IDogsRequest
import javax.inject.Inject

class DogsRepository @Inject constructor(
    private val dogsDao: IDogsDao,
    private val dogsRequest: IDogsRequest
){
    private val netWorkStatusLiveData: MutableLiveData<String> = MutableLiveData()
    private val listOfBreedsLiveData: MutableLiveData<List<Breed>> = MutableLiveData()
    private val listOfSubBreedsLiveData: MutableLiveData<SubBreeds> = MutableLiveData()
    private val listOfImagesLiveData: MutableLiveData<BreedImages> = MutableLiveData()
    private val dbWorkerLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var subscriptions: CompositeDisposable

    fun getNetworkStatus(): LiveData<String> = netWorkStatusLiveData
    fun getListOfBreeds(): LiveData<List<Breed>> = listOfBreedsLiveData
    fun getListOfSubBreeds(): LiveData<SubBreeds> = listOfSubBreedsLiveData
    fun getListOfImages(): LiveData<BreedImages> = listOfImagesLiveData

    fun insertFavourite(favourite: FavouritesEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dogsDao.insertFavourite(favourite)
        }
    }

    fun deleteFavouriteByPhoto(photo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dogsDao.deleteFavouriteByPhoto(photo)
        }
    }

    fun getAllFavourite(): LiveData<List<FavouritesCount>> {
        return dogsDao.getAllFavourite()
    }

    fun getFavouriteImages(breed: String): LiveData<List<String>> {
        return dogsDao.getFavouriteImages(breed)
    }

    fun getAllBreeds() {
        dogsRequest.getAllBreedsFromServer()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseAllBreeds)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<List<Breed>>() {
                override fun onSuccess(breeds: List<Breed>) {
                    listOfBreedsLiveData.postValue(breeds)
                }

                override fun onError(e: Throwable) {
                    netWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
                }
            })
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


    fun getSubBreeds(subBreed: String) {
        dogsRequest.getSubBreedsFromServer(subBreed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseSubBreeds)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<SubBreeds>() {
                override fun onSuccess(subBreeds: SubBreeds) {
                    listOfSubBreedsLiveData.postValue(subBreeds)
                }
                override fun onError(e: Throwable) {
                    netWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
                }
            })
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


    fun getImagesForBreed(breed: String) {
        dogsRequest.getImagesForBreed(breed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseImagesForBreed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<BreedImages>() {
                override fun onSuccess(breedImages: BreedImages) {
                    listOfImagesLiveData.postValue(breedImages)
                }
                override fun onError(e: Throwable) {
                    netWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
                }
            })
    }

    fun getImagesForBreed(breed: String, subBreed: String){
        dogsRequest.getImagesForBreed(breed, subBreed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseImagesForBreed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<BreedImages>() {
                override fun onSuccess(breedImages: BreedImages) {
                    listOfImagesLiveData.postValue(breedImages)
                }
                override fun onError(e: Throwable) {
                    netWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
                }
            })
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
