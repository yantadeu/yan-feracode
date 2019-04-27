package com.yan.feracode.spotify.data.api.retrofit

import android.app.Activity
import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yan.feracode.spotify.data.api.Constants
import com.yan.feracode.spotify.data.api.retrofit.deserializer.ArtistsDeserializer
import com.yan.feracode.spotify.data.api.retrofit.deserializer.TracksDeserializer
import com.yan.feracode.spotify.data.model.Artist
import com.yan.feracode.spotify.data.model.Track
import com.yan.feracode.spotify.view.activity.SpotifyLoginActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class SpotifyRetrofitClient {


    protected var spotifyService: SpotifyRetrofitService? = null
        private set

    var AUTH_TOKEN: String? = null

    private val okHttpClient: OkHttpClient
        get() {
            val client = OkHttpClient.Builder()
            val apiInterceptor = ApiInterceptor()
            apiInterceptor.AUTH_TOKEN = AUTH_TOKEN
            client.addInterceptor(apiInterceptor)
            return client.build()
        }

    private val spotifyServiceClass: Class<SpotifyRetrofitService>
        get() = SpotifyRetrofitService::class.java

    private val spotifyDeserializer: Gson
        get() = GsonBuilder().registerTypeAdapter(object : TypeToken<List<Artist>>() {

        }.type, ArtistsDeserializer<Artist>())
                .registerTypeAdapter(object : TypeToken<List<Track>>() {

                }.type, TracksDeserializer<Track>())
                .create()

    constructor() {
        initRetrofit()
    }

    constructor(activity: Activity) {
        val AUTH_TOKEN = activity.intent.getStringExtra(SpotifyLoginActivity.AUTH_TOKEN)
        this.AUTH_TOKEN = AUTH_TOKEN
        initRetrofit()
    }


    private fun initRetrofit() {


        val retrofit = retrofitBuilder()
        spotifyService = retrofit.create(spotifyServiceClass)
    }

    private fun retrofitBuilder(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.SPOTIFY_API)
                .addConverterFactory(GsonConverterFactory.create(spotifyDeserializer))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }
}

