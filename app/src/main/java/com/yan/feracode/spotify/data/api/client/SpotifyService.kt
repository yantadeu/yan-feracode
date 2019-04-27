package com.yan.feracode.spotify.data.api.client

import com.yan.feracode.spotify.data.model.Artist
import com.yan.feracode.spotify.data.model.Track
import io.reactivex.Observable

interface SpotifyService {

    fun search(query: String): Observable<List<Artist>>

    fun getTracks(artistId: String): Observable<List<Track>>
}
