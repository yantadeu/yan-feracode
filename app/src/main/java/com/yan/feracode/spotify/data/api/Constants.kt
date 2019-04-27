package com.yan.feracode.spotify.data.api

import com.yan.feracode.spotify.data.api.Constants.Params.ARTIST_ID

object Constants {

    val SPOTIFY_API = "https://api.spotify.com"
    val API_KEY = "Authorization"


    val ACCESS_TOKEN = "Bearer "

    object Endpoint {

        const val ARTIST_SEARCH = "/v1/search?type=artist"
        const val ARTIST_TRACKS = "v1/artists/{$ARTIST_ID}/top-tracks?country=BR"
    }

    object Params {
        const val QUERY_SEARCH = "q"
        const val ARTIST_ID = "artistId"
    }

    object Serialized {

        const val NAME = "name"
        const val IMAGES = "images"
        const val FOLLOWERS = "followers"
        const val HREF = "href"
        const val ITEMS = "items"
        const val ID = "id"
        const val POPULARITY = "popularity"
        const val HEIGHT = "height"
        const val URL = "url"
        const val WIDTH = "width"
        const val TOTAL = "total"
        const val PREVIEW_URL = "preview_url"
        const val TRACK_NUMBER = "track_number"
        const val ALBUM = "album"

    }

    object Deserializer {
        val HREF = "href"
        val ARTISTS = "artists"
        val ITEMS = "items"
        val TRACKS = "tracks"
    }
}
