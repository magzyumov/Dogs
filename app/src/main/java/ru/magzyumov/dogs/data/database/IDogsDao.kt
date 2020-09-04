package ru.magzyumov.dogs.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.magzyumov.dogs.data.entity.FavouritesEntity
import ru.magzyumov.dogs.data.entity.FavouritesEntity.*


@Dao
interface IDogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(favourite: FavouritesEntity): Long

    @Query("SELECT breed, COUNT(picture) as count FROM favourites GROUP BY breed")
    fun getAllFavourite(): LiveData<List<FavouritesCount>>

    @Query("SELECT picture FROM favourites WHERE breed = :breed")
    fun getFavouriteImages(breed: String): LiveData<List<String>>

    @Query("DELETE FROM favourites WHERE picture = :photo")
    fun deleteFavouriteByPhoto(photo: String): Int
}
