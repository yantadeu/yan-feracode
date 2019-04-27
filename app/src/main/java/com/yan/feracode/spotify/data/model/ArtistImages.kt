package com.yan.feracode.spotify.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import com.yan.feracode.spotify.data.api.Constants.Serialized.HEIGHT
import com.yan.feracode.spotify.data.api.Constants.Serialized.URL
import com.yan.feracode.spotify.data.api.Constants.Serialized.WIDTH

class ArtistImages private constructor(`in`: Parcel) : Parcelable {

    @SerializedName(HEIGHT)
    private val heigth: Int
    @SerializedName(URL)
    var url: String? = null
    @SerializedName(WIDTH)
    private val width: Int

    init {
        this.heigth = `in`.readInt()
        this.url = `in`.readString()
        this.width = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(this.heigth)
        parcel.writeString(this.url)
        parcel.writeInt(this.width)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ArtistImages> = object : Parcelable.Creator<ArtistImages> {

            override fun createFromParcel(source: Parcel): ArtistImages {
                return ArtistImages(source)
            }

            override fun newArray(size: Int): Array<ArtistImages?> {
                return arrayOfNulls(size)
            }
        }
    }
}
