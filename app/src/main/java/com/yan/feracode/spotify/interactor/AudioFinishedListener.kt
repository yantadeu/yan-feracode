package com.yan.feracode.spotify.interactor

import android.content.ServiceConnection
import com.yan.feracode.spotify.view.service.AudioPlayerService

interface AudioFinishedListener {

    fun onPlay()

    fun onPause()

    fun onSetTimeStart(trackCurrentPosition: Int)

    fun onSetTimeFinished(audioPlayerService: AudioPlayerService)

    fun onResetTrackDuration()

    fun onSetInfoTrackPlayer(trackPosition: Int)

    fun onServiceConnection(serviceConnection: ServiceConnection)
}
