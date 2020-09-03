package ru.magzyumov.dogs.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single
import ru.magzyumov.dogs.model.entity.FavouritesCountEntity
import ru.magzyumov.dogs.model.entity.FavouritesEntity


@Dao
interface IDogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(favourite: FavouritesEntity): Long

    @Query("SELECT breed, COUNT(picture) as count FROM favourites GROUP BY breed")
    fun getAllFavourite(): LiveData<List<FavouritesCountEntity>>

    @Query("SELECT picture FROM favourites WHERE breed = :breed")
    fun getFavouriteImages(breed: String): LiveData<List<String>>

    @Query("DELETE FROM favourites WHERE picture = :photo")
    fun deleteFavouriteByPhoto(photo: String): Int
}
