package com.yan.feracode.spotify.data.api.client

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

import com.yan.feracode.spotify.data.api.retrofit.SpotifyRetrofitClient
import com.yan.feracode.spotify.data.model.Artist
import com.yan.feracode.spotify.data.model.Track
import com.yan.feracode.spotify.view.activity.SpotifyLoginActivity

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.observers.EmptyCompletableObserver
import io.reactivex.schedulers.Schedulers

class SpotifyClient : SpotifyRetrofitClient, SpotifyService {


    constructor(activity: Activity) : super(activity)


    override fun search(query: String): Observable<List<Artist>> {
        return spotifyService!!.searchArtist(query)
                .map {
                    val listType = object : TypeToken<List<Artist>>() {}.type
                    Gson().fromJson<List<Artist>>(it.getAsJsonObject("artists").get("items"), listType)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTracks(artistId: String): Observable<List<Track>> {
        return spotifyService!!.getTracks(artistId).map {
                        val listType = object : TypeToken<List<Track>>() {}.type
                        Gson().fromJson<List<Track>>(it.get("tracks"), listType)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}
