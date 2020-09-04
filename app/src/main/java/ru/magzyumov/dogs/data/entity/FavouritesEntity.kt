package ru.magzyumov.dogs.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
class FavouritesEntity(

    @ColumnInfo(name = "breed")
    var breed: String,

    @PrimaryKey
    @ColumnInfo(name = "picture")
    var picture: String,
    ) {

    class FavouritesCount(var breed: String, var count: Int)
}
