package com.yan.feracode.spotify.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import com.yan.feracode.spotify.data.api.Constants.Serialized.HREF
import com.yan.feracode.spotify.data.api.Constants.Serialized.TOTAL

class Followers private constructor(`in`: Parcel) : Parcelable {

    @SerializedName(HREF)
    private val href: String?
    @SerializedName(TOTAL)
    var totalFollowers: Int = 0

    init {
        this.href = `in`.readString()
        this.totalFollowers = `in`.readInt()
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(this.href)
        parcel.writeInt(this.totalFollowers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Followers> = object : Parcelable.Creator<Followers> {

            override fun createFromParcel(source: Parcel): Followers {
                return Followers(source)
            }

            override fun newArray(size: Int): Array<Followers?> {
                return arrayOfNulls(size)
            }
        }
    }
}
