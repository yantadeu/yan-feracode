package com.yan.feracode.spotify.data.model

import com.google.gson.annotations.SerializedName

import com.yan.feracode.spotify.data.api.Constants.Serialized.IMAGES
import com.yan.feracode.spotify.data.api.Constants.Serialized.NAME

class Album {
    @SerializedName(NAME)
    var albumName: String? = null
    @SerializedName(IMAGES)
    var trackImages: List<ArtistImages>? = null
}
