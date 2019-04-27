package com.yan.feracode.spotify.view.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message

import java.io.IOException
import java.util.Timer
import java.util.TimerTask

@Suppress("DEPRECATION")
class AudioPlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private var mediaPlayerBinder: PlayerBinder? = null
    private var mediaPlayerHandler: Handler? = null
    private var timer: Timer? = null
    private var currentTrackPosition: Int = 0
    private var isPlayerPaused: Boolean = false
    private var mediaPlayer: MediaPlayer? = null
    private var trackPreviewUrl: String? = null

    val trackDurationString: String
        get() = "00:" + String.format("%02d", trackDuration)

    val trackDuration: Int
        get() = if (mediaPlayer != null && (isPlayerPaused || mediaPlayer!!.isPlaying)) {
            mediaPlayer!!.duration / 1000
        } else {
            0
        }

    /**
     * This method is executed first (1er)
     * initialize the Binder.
     */

    override fun onCreate() {
        super.onCreate()
        mediaPlayerBinder = PlayerBinder()
    }

    /**
     * This method is executed second (2nd)
     * Receives a string (EXTRA_TRACK_PREVIEW_URL) of intent from where he was released
     */

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null
                && intent.hasExtra(EXTRA_TRACK_PREVIEW_URL)
                && intent.getStringExtra(EXTRA_TRACK_PREVIEW_URL) != null) {
            setTrackPreviewUrl(intent.getStringExtra(EXTRA_TRACK_PREVIEW_URL))
            onPlayAudio(0)
        }
        return Service.START_STICKY
    }

    /**
     * This method is executed third (3th)
     * returns an IBinder object that defines the programming
     * interface that clients can use to interact with the service
     */

    override fun onBind(intent: Intent): IBinder? {
        return mediaPlayerBinder
    }


    /*
     * This method is executed fourth (4th)
     * This method is called when the media file is ready for playback.
     */

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
        if (currentTrackPosition != 0) {
            mediaPlayer.seekTo(currentTrackPosition * 1000)
        }

        updateUI()
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {

        val completionMessage = Message()
        val completionBundle = Bundle()
        completionBundle.putBoolean(EXTRA_IS_PLAYER, false)
        completionMessage.data = completionBundle
        if (mediaPlayerHandler != null) {
            mediaPlayerHandler!!.sendMessage(completionMessage)
        }

        noUpdateUI()
    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        return false
    }

    /**
     * @param trackPreviewUrl
     */
    fun setTrackPreviewUrl(trackPreviewUrl: String) {
        this.trackPreviewUrl = trackPreviewUrl
    }


    fun onPlayAudio(trackPosition: Int) {
        currentTrackPosition = trackPosition
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }

            mediaPlayer!!.reset()
        }
        setupAudioPlayer()
        isPlayerPaused = false
    }

    fun onPauseAudio(): Int {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            isPlayerPaused = true
            noUpdateUI()
            return mediaPlayer!!.duration / 1000
        } else {
            return 0
        }
    }

    fun setupAudioPlayer() {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }

        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer!!.setDataSource(trackPreviewUrl)
            mediaPlayer!!.prepareAsync()
            mediaPlayer!!.setOnCompletionListener(this@AudioPlayerService)
            mediaPlayer!!.setOnPreparedListener(this@AudioPlayerService)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer!!.setOnErrorListener(this@AudioPlayerService)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            noUpdateUI()
        }
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        if (mediaPlayerHandler != null) {
            mediaPlayerHandler = null
        }
    }

    fun noUpdateUI() {
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
        }
    }

    fun updateUI() {
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                sendCurrentTrackPosition()
            }
        }, 0, 1000)
    }

    private fun sendCurrentTrackPosition() {
        val message = Message()
        message.data = getCurrentTrackPosition()
        if (mediaPlayerHandler != null) {
            mediaPlayerHandler!!.sendMessage(message)
        }
    }

    private fun getCurrentTrackPosition(): Bundle {
        val uiBundle = Bundle()
        if (mediaPlayer != null && (isPlayerPaused || mediaPlayer!!.isPlaying)) {
            uiBundle.putBoolean(EXTRA_IS_PLAYER, true)
            val trackPosition = Math.ceil(mediaPlayer!!.currentPosition.toDouble() / 1000).toInt()
            uiBundle.putInt(EXTRA_CURRENT_TRACK_POSITION, trackPosition)
        }
        return uiBundle
    }

    fun toSeekTrack(trackProgress: Int, isTrackPaused: Boolean) {
        if (mediaPlayer != null && isTrackPaused && !mediaPlayer!!.isPlaying || mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.seekTo(trackProgress * 1000)
            if (mediaPlayer!!.isPlaying) {
                updateUI()
            }
        }
    }

    fun setAudioPlayerHandler(spotifyPlayerHandler: Handler) {

        this.mediaPlayerHandler = spotifyPlayerHandler
        val playerMessage = Message()
        val playerBundle: Bundle
        if (this.mediaPlayerHandler != null && (isPlayerPaused || mediaPlayer!!.isPlaying)) {
            playerBundle = getCurrentTrackPosition()

            if (!isPlayerPaused) {
                updateUI()
            } else {
                playerBundle.putBoolean(EXTRA_IS_PLAYER, false)
            }
            playerMessage.data = playerBundle
            if (this.mediaPlayerHandler != null) {
                this.mediaPlayerHandler!!.sendMessage(playerMessage)
            }
        }
    }

    inner class PlayerBinder : Binder() {
        val service: AudioPlayerService
            get() = this@AudioPlayerService
    }

    companion object {

        val EXTRA_IS_PLAYER = "is_player"
        val EXTRA_CURRENT_TRACK_POSITION = "current_track_position"
        val EXTRA_TRACK_PREVIEW_URL = "track_preview_url"
    }
}
