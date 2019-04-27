package com.yan.feracode.spotify.interactor


import com.yan.feracode.spotify.data.api.client.SpotifyService
import com.yan.feracode.spotify.data.model.Track

import io.reactivex.Observable

class TracksInteractor(private val spotifyService: SpotifyService) {

    fun loadData(artistId: String): Observable<List<Track>> {
        return spotifyService.getTracks(artistId)
    }
}
