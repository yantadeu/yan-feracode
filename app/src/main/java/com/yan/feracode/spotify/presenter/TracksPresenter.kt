package com.yan.feracode.spotify.presenter

import com.yan.feracode.spotify.data.model.Track
import com.yan.feracode.spotify.interactor.TracksInteractor
import io.reactivex.disposables.Disposable

internal class TracksPresenter(private val interactor: TracksInteractor) : Presenter<TracksPresenter.View>() {

    override fun terminate() {
        super.terminate()
        view = null
    }

    fun onSearchTracks(string: String) {
        view!!.showLoading()
        val disposable = interactor.loadData(string).subscribe({ tracks ->
            if (tracks.isNotEmpty()) {
                view!!.hideLoading()
                view!!.renderTracks(tracks)
            } else {
                view!!.showTracksNotFoundMessage()
            }
        }, { it.printStackTrace() })
        addDisposableObserver(disposable)
    }

    fun launchArtistDetail(tracks: List<Track>, track: Track, position: Int) {
        view!!.launchTrackDetail(tracks, track, position)
    }

    internal interface View : Presenter.View {

        fun showLoading()

        fun hideLoading()

        fun showTracksNotFoundMessage()

        fun showConnectionErrorMessage()

        fun renderTracks(tracks: List<Track>)

        fun launchTrackDetail(tracks: List<Track>, track: Track, position: Int)
    }
}
