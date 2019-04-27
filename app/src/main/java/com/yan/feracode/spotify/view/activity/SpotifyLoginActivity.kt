package com.yan.feracode.spotify.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.yan.feracode.R


import androidx.appcompat.app.AppCompatActivity

class SpotifyLoginActivity : AppCompatActivity() {

    internal var mListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.login_button -> openLoginWindow()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_login)

        val mLoginButton = findViewById<View>(R.id.login_button) as Button
        mLoginButton.setOnClickListener(mListener)

    }


    private fun openLoginWindow() {

        val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("user-read-private", "streaming", "user-top-read", "user-read-recently-played", "app-remote-control", "user-follow-read", "user-follow-modify", "user-library-read", "user-top-read", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "playlist-read-collaborative", "playlist-modify-public", "playlist-modify-private", "playlist-read-private"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {

                AuthenticationResponse.Type.TOKEN -> {

                    val intent = Intent(this@SpotifyLoginActivity,
                            ArtistsActivity::class.java)

                    intent.putExtra(AUTH_TOKEN, response.accessToken)

                    startActivity(intent)

                    destroy()
                }

                AuthenticationResponse.Type.ERROR -> Log.e(TAG, "Auth error: " + response.error)

                else -> Log.d(TAG, "Auth result: " + response.type)
            }
        }
    }

    fun destroy() {
        this@SpotifyLoginActivity.finish()
    }

    companion object {


        val CLIENT_ID = "5de6930c8a744270851a5064c7ff6333"
        private val REDIRECT_URI = "http://localhost:8888/callback"

        private val TAG = "Yan Spotify " + SpotifyLoginActivity::class.java.simpleName

        /**
         * Request code that will be passed together with authentication result to the onAuthenticationResult
         */
        private val REQUEST_CODE = 1337

        val AUTH_TOKEN = "AUTH_TOKEN"
    }
}
