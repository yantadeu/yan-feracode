package com.yan.feracode.spotify.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

import com.yan.feracode.spotify.data.api.Constants.Serialized.FOLLOWERS
import com.yan.feracode.spotify.data.api.Constants.Serialized.HREF
import com.yan.feracode.spotify.data.api.Constants.Serialized.ID
import com.yan.feracode.spotify.data.api.Constants.Serialized.IMAGES
import com.yan.feracode.spotify.data.api.Constants.Serialized.NAME
import com.yan.feracode.spotify.data.api.Constants.Serialized.POPULARITY

class Artist() : Parcelable {

    @SerializedName(FOLLOWERS)
    var followers: Followers? = null
    @SerializedName(HREF)
    private var href: String? = null
    @SerializedName(ID)
    var id: String? = null
    @SerializedName(IMAGES)
    var artistImages: List<ArtistImages>? = null
    @SerializedName(NAME)
    var name: String? = null
    @SerializedName(POPULARITY)
    private var popularity: Int = -1

    protected constructor(`in`: Parcel) : this() {
        this.href = `in`.readString()
        this.id = `in`.readString()
        this.name = `in`.readString()
        this.followers = `in`.readParcelable(Followers::class.java.classLoader)
        this.popularity = `in`.readInt()

        if (this.artistImages == null) {
            this.artistImages = ArrayList()
        }
        `in`.readTypedList(this.artistImages, ArtistImages.CREATOR)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(this.href)
        parcel.writeString(this.id)
        parcel.writeString(this.name)
        parcel.writeParcelable(this.followers, 0)
        parcel.writeInt(this.popularity)
        parcel.writeTypedList(this.artistImages)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Artist> = object : Parcelable.Creator<Artist> {

            override fun createFromParcel(source: Parcel): Artist {
                return Artist(source)
            }

            override fun newArray(size: Int): Array<Artist?> {
                return arrayOfNulls(size)
            }
        }
    }
}
