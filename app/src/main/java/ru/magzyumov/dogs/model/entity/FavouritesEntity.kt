package ru.magzyumov.dogs.model.entity

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

}