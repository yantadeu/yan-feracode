package com.yan.feracode.spotify.interactor

import com.yan.feracode.spotify.data.api.client.SpotifyService
import com.yan.feracode.spotify.data.model.Artist
import io.reactivex.Observable

class ArtistsInteractor(private val spotifyService: SpotifyService) {

    fun searchArtists(query: String): Observable<List<Artist>> {
        return spotifyService.search(query)
    }

}
