package com.yan.feracode.spotify.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import butterknife.OnClick
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.yan.feracode.R
import com.yan.feracode.spotify.data.model.Track
import com.yan.feracode.spotify.interactor.PlayerInteractor
import com.yan.feracode.spotify.presenter.AudioPlayerPresenter
import com.yan.feracode.spotify.view.activity.TracksActivity
import com.yan.feracode.spotify.view.service.AudioPlayerService
import com.yan.feracode.spotify.view.utils.ServiceUtils
import kotlinx.android.synthetic.main.fragment_audio_player.*

class PlayerFragment : DialogFragment(), AudioPlayerPresenter.View, SeekBar.OnSeekBarChangeListener {


    private val audioPlayerService: AudioPlayerService? = null
    private val isPlayerPlaying = false
    private val isPlayerPaused = false
    private var trackCurrentPosition: Int = 0

    private var trackList: List<Track>? = null
    private var trackPosition: Int = 0
    private lateinit var audioPlayerPresenter: AudioPlayerPresenter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_audio_player, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        trackList = getTrackList(arguments!!.getString(TracksActivity.EXTRA_TRACKS))
        trackPosition = arguments!!.getInt(TracksActivity.EXTRA_TRACK_POSITION)
        audioPlayerPresenter = AudioPlayerPresenter(PlayerInteractor(trackList!!, requireContext()))
        audioPlayerPresenter.view = this

        audioPlayerPresenter.setInfoMediaPlayer(trackPosition)
        audioPlayerPresenter.onStartAudioService(trackList!![trackPosition].preview_url!!)
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        audioPlayerPresenter.terminate()
        super.onDestroyView()
    }

    @OnClick(R.id.ib_preview_player)
    internal fun previewTrack() {
        audioPlayerPresenter.onPreviewTrack()
    }

    @OnClick(R.id.ib_next_player)
    internal fun nextTrack() {
        audioPlayerPresenter.onNextTrack()
    }

    @OnClick(R.id.ib_play_player)
    internal fun playTrack() {
        audioPlayerPresenter.onPlayPauseTrack()
    }

    override fun setInfoTrackPlayer(trackPosition: Int) {

        txt_track_title_player.text = trackList!![trackPosition].name
        txt_album_title_player.text = trackList!![trackPosition].album!!.albumName

        if (trackList!![trackPosition].album!!.trackImages!!.size > 0) {
            for (i in trackList!![trackPosition].album!!.trackImages!!.indices) {
                if (trackList!![trackPosition].album!!.trackImages!!.isNotEmpty()) {
                    Picasso.with(activity)
                            .load(trackList!![trackPosition].album!!.trackImages!![0].url)
                            .into(iv_album_player)
                }
            }
        } else {
            Picasso.with(activity)
                    .load("http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png")
                    .into(iv_album_player)
        }
    }

    override fun onDestroy() {
        audioPlayerPresenter.terminate()
        super.onDestroy()
    }

    private fun getTrackList(tracks: String?): List<Track>? {
        val gson = GsonBuilder().create()
        val trackType = object : TypeToken<List<Track>>() {

        }.type
        return gson.fromJson<List<Track>>(tracks, trackType)
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        txt_time_start!!.text = "00:" + String.format("%02d", i)
        trackCurrentPosition = i
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        if (isPlayerPlaying) {
            audioPlayerService!!.noUpdateUI()
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        trackCurrentPosition = seekBar.progress
        audioPlayerService?.toSeekTrack(trackCurrentPosition, isPlayerPaused)
    }

    override fun onStartAudioService(trackUrl: String, serviceConnection: ServiceConnection?) {

        val serviceIntent = Intent(activity, AudioPlayerService::class.java)
        serviceIntent.putExtra(AudioPlayerService.EXTRA_TRACK_PREVIEW_URL, trackUrl)

        if (ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService::class.java, activity!!) && !isPlayerPlaying) {
            trackCurrentPosition = 0
            activity!!.applicationContext.stopService(serviceIntent)
            activity!!.applicationContext.startService(serviceIntent)
        } else if (!ServiceUtils.isAudioPlayerServiceRunning(AudioPlayerService::class.java, activity!!)) {
            trackCurrentPosition = 0
            activity!!.applicationContext.startService(serviceIntent)
        }
        if (audioPlayerService == null) {
            activity!!.applicationContext
                    .bindService(serviceIntent, serviceConnection!!, Context.BIND_AUTO_CREATE)
        }
    }

    override fun isPlay() {
        ib_play_player!!.setImageResource(android.R.drawable.ic_media_play)
    }

    override fun isPause() {
        ib_play_player!!.setImageResource(android.R.drawable.ic_media_pause)
    }

    override fun setTimeStart(trackCurrentPosition: Int) {
        sb_time_progress_player!!.progress = trackCurrentPosition
        txt_time_start!!.text = "00:" + String.format("%02d", trackCurrentPosition)
    }

    override fun setTimeFinished(audioPlayerService: AudioPlayerService) {
        sb_time_progress_player!!.max = audioPlayerService.trackDuration
        txt_time_end!!.text = audioPlayerService.trackDurationString
    }

    override fun onResetTrackDuration() {
        sb_time_progress_player!!.progress = 0
        txt_time_start!!.text = ""
        txt_time_end!!.text = ""
    }

    override fun context(): Context = requireContext()


    companion object {

        fun newInstance(tracks: String, position: Int): PlayerFragment {
            val playerFragment = PlayerFragment()
            val bundle = Bundle()
            bundle.putString(TracksActivity.EXTRA_TRACKS, tracks)
            bundle.putInt(TracksActivity.EXTRA_TRACK_POSITION, position)
            playerFragment.arguments = bundle
            return playerFragment
        }
    }
}
