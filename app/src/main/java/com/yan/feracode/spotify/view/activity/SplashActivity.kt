package com.yan.feracode.spotify.view.activity

import android.content.Intent
import android.graphics.PixelFormat
import android.media.Image
import android.os.Bundle
import com.yan.feracode.R
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout


class SplashActivity : AppCompatActivity() {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        StartAnimations()
    }

    private fun StartAnimations() {
        var anim = AnimationUtils.loadAnimation(this, R.anim.alpha)
        anim.reset()
        val l = findViewById<LinearLayout>(R.id.lin_lay)
        l.clearAnimation()
        l.startAnimation(anim)

        anim = AnimationUtils.loadAnimation(this, R.anim.translate)
        anim.reset()
        val iv = findViewById<ImageView>(R.id.splash)
        iv.clearAnimation()
        iv.startAnimation(anim)

        val splashTread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    // Splash screen pause time
                    while (waited < 2500) {
                        Thread.sleep(100)
                        waited += 100
                    }

                    val intent = Intent(this@SplashActivity,
                            SpotifyLoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION

                    startActivity(intent)
                    overridePendingTransition(R.anim.pull_up_from_bottom, R.anim.hold)

                    this@SplashActivity.finish()
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {
                    this@SplashActivity.finish()
                }

            }
        }
        splashTread.start()
    }

}
