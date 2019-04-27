package com.yan.feracode.spotify.presenter

import android.content.ServiceConnection
import com.yan.feracode.spotify.interactor.AudioFinishedListener
import com.yan.feracode.spotify.interactor.PlayerInteractor
import com.yan.feracode.spotify.view.service.AudioPlayerService

internal class AudioPlayerPresenter(private val playerInteractor: PlayerInteractor) : Presenter<AudioPlayerPresenter.View>(), AudioFinishedListener {
    private var serviceConnection: ServiceConnection? = null

    init {
        this.playerInteractor.setAudioFinishedListener(this)
    }

    fun onPreviewTrack() {
        playerInteractor.onPreview()
    }

    fun onNextTrack() {
        playerInteractor.onNext()
    }

    fun onPlayPauseTrack() {
        playerInteractor.onPlayStop()
    }

    fun onStartAudioService(trackUrl: String) {
        view!!.onStartAudioService(trackUrl, serviceConnection)
    }

    fun setInfoMediaPlayer(trackPosition: Int) {
        view!!.setInfoTrackPlayer(trackPosition)
    }

    override fun terminate() {
        super.terminate()
        playerInteractor.destroyAudioService()
        view = null
    }

    override fun onPlay() {
        view!!.isPlay()
    }

    override fun onPause() {
        view!!.isPause()
    }

    override fun onSetTimeStart(trackCurrentPosition: Int) {
        view!!.setTimeStart(trackCurrentPosition)
    }

    override fun onSetTimeFinished(audioPlayerService: AudioPlayerService) {
        view!!.setTimeFinished(audioPlayerService)
    }

    override fun onResetTrackDuration() {
        view!!.onResetTrackDuration()
    }

    override fun onSetInfoTrackPlayer(trackPosition: Int) {
        view!!.setInfoTrackPlayer(trackPosition)
    }

    override fun onServiceConnection(serviceConnection: ServiceConnection) {
        this.serviceConnection = serviceConnection
    }

    internal interface View : Presenter.View {

        fun onStartAudioService(trackUrl: String, serviceConnection: ServiceConnection?)

        fun setInfoTrackPlayer(trackPosition: Int)

        fun isPlay()

        fun isPause()

        fun setTimeStart(trackCurrentPosition: Int)

        fun setTimeFinished(audioPlayerService: AudioPlayerService)

        fun onResetTrackDuration()
    }
}
