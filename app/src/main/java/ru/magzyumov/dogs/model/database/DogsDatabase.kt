package ru.magzyumov.dogs.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.magzyumov.dogs.model.entity.DogEntity


@Database(entities = [DogEntity::class], version = 1, exportSchema = false)
abstract class DogsDatabase: RoomDatabase() {
    abstract fun iDogsDao(): IDogsDao
}