package com.yan.feracode.spotify.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import com.yan.feracode.spotify.data.api.Constants.Serialized.ALBUM
import com.yan.feracode.spotify.data.api.Constants.Serialized.NAME
import com.yan.feracode.spotify.data.api.Constants.Serialized.PREVIEW_URL
import com.yan.feracode.spotify.data.api.Constants.Serialized.TRACK_NUMBER

class Track protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName(NAME)
    var name: String? = null
    @SerializedName(PREVIEW_URL)
    var preview_url: String? = null
    @SerializedName(TRACK_NUMBER)
    private val track_number: Int
    @SerializedName(ALBUM)
    var album: Album? = null

    init {
        this.name = `in`.readString()
        this.preview_url = `in`.readString()
        this.track_number = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(this.name)
        parcel.writeString(this.preview_url)
        parcel.writeInt(this.track_number)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Track> = object : Parcelable.Creator<Track> {

            override fun createFromParcel(source: Parcel): Track {
                return Track(source)
            }

            override fun newArray(size: Int): Array<Track?> {
                return arrayOfNulls(size)
            }
        }
    }
}
