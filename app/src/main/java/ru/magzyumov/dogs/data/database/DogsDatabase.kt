package ru.magzyumov.dogs.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.magzyumov.dogs.data.entity.FavouritesEntity


@Database(entities = [FavouritesEntity::class], version = 1, exportSchema = false)
abstract class DogsDatabase: RoomDatabase() {
    abstract fun iDogsDao(): IDogsDao
}