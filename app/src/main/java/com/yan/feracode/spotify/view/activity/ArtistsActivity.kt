package com.yan.feracode.spotify.view.activity

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.yan.feracode.R

class ArtistsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artists)
    }

    companion object {
        private val TAG = "Spotify ArtistsActivity"
    }

}
