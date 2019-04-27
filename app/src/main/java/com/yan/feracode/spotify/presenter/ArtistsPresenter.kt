package com.yan.feracode.spotify.presenter

import com.yan.feracode.spotify.data.model.Artist
import com.yan.feracode.spotify.interactor.ArtistsInteractor
import io.reactivex.disposables.Disposable

internal class ArtistsPresenter(private val artistsInteractor: ArtistsInteractor) : Presenter<ArtistsPresenter.View>() {

    fun onSearchArtist(name: String) {
        view!!.showLoading()
        val list = artistsInteractor.searchArtists(name)
        val disposable = list.subscribe({ artists ->
            if (artists!!.isNotEmpty()) {
                view!!.hideLoading()
                artists?.let { view!!.renderArtists(it) }
            } else {
                view!!.showArtistNotFoundMessage()
            }
        }, {
            view!!.showArtistNotFoundMessage()
            it.printStackTrace()
        })


        addDisposableObserver(disposable)
    }

    fun launchArtistDetail(artist: Artist) {
        view!!.launchArtistDetail(artist)
    }

    override fun terminate() {
        super.terminate()
        view = null
    }

    interface View : Presenter.View {

        fun showLoading()

        fun hideLoading()

        fun showArtistNotFoundMessage()

        fun showConnectionErrorMessage()

        fun showServerError()

        fun renderArtists(artists: List<Artist>)

        fun launchArtistDetail(artist: Artist)
    }
}
