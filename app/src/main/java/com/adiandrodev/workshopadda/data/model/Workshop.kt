package com.adiandrodev.workshopadda.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workshopTable")
data class Workshop (
    @PrimaryKey
    val id: Int = -1,
    val name: String = "",
    val description: String = "",
    val fromDate: String = "",
    val toDate: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(fromDate)
        parcel.writeString(toDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Workshop> {
        override fun createFromParcel(parcel: Parcel): Workshop {
            return Workshop(parcel)
        }

        override fun newArray(size: Int): Array<Workshop?> {
            return arrayOfNulls(size)
        }
    }
}