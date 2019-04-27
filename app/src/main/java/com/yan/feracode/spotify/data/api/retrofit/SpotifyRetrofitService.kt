package com.yan.feracode.spotify.data.api.retrofit

import com.google.gson.JsonObject
import com.yan.feracode.spotify.data.api.Constants
import com.yan.feracode.spotify.data.model.Artist
import com.yan.feracode.spotify.data.model.Track
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyRetrofitService {

    @GET(Constants.Endpoint.ARTIST_SEARCH)
    fun searchArtist(@Query(Constants.Params.QUERY_SEARCH) artist: String): Observable<JsonObject>

    @GET(Constants.Endpoint.ARTIST_TRACKS)
    fun getTracks(@Path(Constants.Params.ARTIST_ID) artistId: String): Observable<JsonObject>
}
