package ru.magzyumov.dogs.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
class DogEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "breed")
    var breed: String,

    @ColumnInfo(name = "subBreed")
    var subBreed: String,

    @ColumnInfo(name = "picture")
    var picture: String,

    @ColumnInfo(name = "isFavourite")
    var isFavourite: Byte
): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readInt(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(breed)
        parcel.writeString(subBreed)
        parcel.writeString(picture)
        parcel.writeByte(isFavourite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DogEntity> {
        override fun createFromParcel(parcel: Parcel): DogEntity {
            return DogEntity(parcel)
        }

        override fun newArray(size: Int): Array<DogEntity?> {
            return arrayOfNulls(size)
        }
    }
}