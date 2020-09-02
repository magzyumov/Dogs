package ru.magzyumov.dogs.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.magzyumov.dogs.model.entity.DogEntity


@Dao
interface IDogsDao {
    @Query("SELECT * FROM dogs")
    fun getAll(): LiveData<List<DogEntity>>

    @Query("SELECT * FROM dogs WHERE id = :id")
    fun getById(id: Int): LiveData<DogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(garage: DogEntity): Long

    @Delete
    fun delete(garage: DogEntity): Int

    @Update
    fun update(garage: DogEntity)

    @Query("DELETE FROM dogs WHERE id = :id")
    fun deleteById(id: Int)
}