package ru.magzyumov.dogs.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
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
import javax.inject.Singleton


class DogsRepository @Inject constructor(
    private val mDogsDao: IDogsDao,
    private val mDogsRequest: IDogsRequest
){
    private val mNetWorkStatusLiveData: MutableLiveData<String> = MutableLiveData()
    private val mListOfBreedsLiveData: MutableLiveData<List<Breed>> = MutableLiveData()
    private val mListOfSubBreedsLiveData: MutableLiveData<SubBreeds> = MutableLiveData()
    private val mListOfImagesLiveData: MutableLiveData<BreedImages> = MutableLiveData()

    fun getNetworkStatus(): LiveData<String> = mNetWorkStatusLiveData
    fun getListOfBreeds(): LiveData<List<Breed>> = mListOfBreedsLiveData
    fun getListOfSubBreeds(): LiveData<SubBreeds> = mListOfSubBreedsLiveData
    fun getListOfImages(): LiveData<BreedImages> = mListOfImagesLiveData

    fun insertFavourite(favourite: FavouritesEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            mDogsDao.insertFavourite(favourite)
        }
    }

    fun deleteFavouriteByPhoto(photo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            mDogsDao.deleteFavouriteByPhoto(photo)
        }
    }

    fun getAllFavourite(): LiveData<List<FavouritesCount>> {
        return mDogsDao.getAllFavourite()
    }

    fun getFavouriteImages(breed: String): LiveData<List<String>> {
        return mDogsDao.getFavouriteImages(breed)
    }

    fun getAllBreeds() {
        mDogsRequest.getAllBreedsFromServer()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseAllBreeds)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<List<Breed>>() {
                override fun onSuccess(breeds: List<Breed>) {
                    mListOfBreedsLiveData.postValue(breeds)
                }

                override fun onError(e: Throwable) {
                    mNetWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
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
        mDogsRequest.getSubBreedsFromServer(subBreed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseSubBreeds)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<SubBreeds>() {
                override fun onSuccess(subBreeds: SubBreeds) {
                    mListOfSubBreedsLiveData.postValue(subBreeds)
                }
                override fun onError(e: Throwable) {
                    mNetWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
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
        mDogsRequest.getImagesForBreed(breed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseImagesForBreed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<BreedImages>() {
                override fun onSuccess(breedImages: BreedImages) {
                    mListOfImagesLiveData.postValue(breedImages)
                }
                override fun onError(e: Throwable) {
                    mNetWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
                }
            })
    }

    fun getImagesForBreed(breed: String, subBreed: String){
        mDogsRequest.getImagesForBreed(breed, subBreed)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseImagesForBreed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<BreedImages>() {
                override fun onSuccess(breedImages: BreedImages) {
                    mListOfImagesLiveData.postValue(breedImages)
                }
                override fun onError(e: Throwable) {
                    mNetWorkStatusLiveData.postValue(e.localizedMessage.orEmpty())
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
