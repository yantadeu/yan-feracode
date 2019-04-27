package com.yan.feracode.spotify.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yan.feracode.spotify.data.api.Constants.Serialized.HREF
import com.yan.feracode.spotify.data.api.Constants.Serialized.ITEMS

class ListArtist protected constructor(`in`: Parcel) : Parcelable {

    @SerializedName(HREF)
    var href: String? = null
    @SerializedName(ITEMS)
    var items:  List<Artist>? = null

    init {
        this.href = `in`.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(items)
        parcel.writeString(href)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListArtist> {
        override fun createFromParcel(parcel: Parcel): ListArtist {
            return ListArtist(parcel)
        }

        override fun newArray(size: Int): Array<ListArtist?> {
            return arrayOfNulls(size)
        }
    }

}
