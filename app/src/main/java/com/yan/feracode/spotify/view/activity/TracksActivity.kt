package com.yan.feracode.spotify.view.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

import java.lang.reflect.Type

import butterknife.BindView
import butterknife.ButterKnife
import de.hdodenhof.circleimageview.CircleImageView
import com.yan.feracode.R
import com.yan.feracode.spotify.data.api.client.SpotifyClient
import com.yan.feracode.spotify.data.model.Artist
import com.yan.feracode.spotify.data.model.Track
import com.yan.feracode.spotify.interactor.TracksInteractor
import com.yan.feracode.spotify.presenter.TracksPresenter
import com.yan.feracode.spotify.view.adapter.TracksAdapter
import com.yan.feracode.spotify.view.fragment.PlayerFragment
import com.yan.feracode.spotify.view.utils.BlurEffectUtils
import kotlinx.android.synthetic.main.activity_tracks.*

class TracksActivity : AppCompatActivity(), TracksPresenter.View, AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.toolbar)
    internal var tlbar: Toolbar? = null

    private var tracksPresenter: TracksPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracks)

        ButterKnife.bind(this)
        setuptlbar()
        setupRecyclerView()

        tracksPresenter = TracksPresenter(TracksInteractor(SpotifyClient(this@TracksActivity)))
        tracksPresenter!!.view = this
        rv_tracks
        tlbar = toolbar
        val artist = intent.getParcelableExtra<Artist>(EXTRA_REPOSITORY)
        initializeViews(artist)

        tracksPresenter!!.onSearchTracks(artist.id!!)
    }

    override fun showLoading() {
        pv_tracks!!.visibility = View.VISIBLE

        txt_line_tracks!!.visibility = View.GONE
        rv_tracks!!.visibility = View.GONE
    }

    override fun hideLoading() {
        pv_tracks!!.visibility = View.GONE
        rv_tracks!!.visibility = View.VISIBLE
    }

    override fun showTracksNotFoundMessage() {
        pv_tracks!!.visibility = View.GONE
        txt_line_tracks!!.visibility = View.VISIBLE

        txt_line_tracks!!.text = getString(R.string.error_tracks_not_found)
    }

    override fun showConnectionErrorMessage() {
        pv_tracks!!.visibility = View.GONE
        txt_line_tracks!!.visibility = View.VISIBLE
        txt_line_tracks!!.text = getString(R.string.error_internet_connection)
    }

    override fun renderTracks(tracks: List<Track>) {
        val adapter = rv_tracks!!.adapter as TracksAdapter?
        adapter!!.setTracks(tracks)
        adapter.notifyDataSetChanged()
    }

    override fun launchTrackDetail(tracks: List<Track>, track: Track, position: Int) {
        PlayerFragment.newInstance(setTracks(tracks), position)
                .show(supportFragmentManager, "")
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        onOffsetChangedState(appBarLayout, verticalOffset)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onOffsetChangedState(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (verticalOffset == 0) {
            hideAndShowTitletlbar(View.GONE)
        } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            hideAndShowTitletlbar(View.VISIBLE)
        } else {
            hideAndShowTitletlbar(View.GONE)
        }
    }

    private fun hideAndShowTitletlbar(visibility: Int) {
        txt_title_tracks!!.visibility = visibility
        txt_subtitle_artist!!.visibility = visibility
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_tracks!!.layoutManager = linearLayoutManager
        val adapter = TracksAdapter()
        adapter.setItemClickListener(object : TracksAdapter.ItemClickListener {
            override fun onItemClick(tracks: List<Track>, track: Track, position: Int) {
                tracksPresenter!!.launchArtistDetail(tracks, track, position)
            }
        })
        rv_tracks!!.adapter = adapter

        appbar_artist!!.addOnOffsetChangedListener(this)
    }

    private fun setuptlbar() {
        setSupportActionBar(tlbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
    }

    private fun initializeViews(artist: Artist) {

        if (artist.artistImages!!.isNotEmpty()) {
            Picasso.with(this)
                    .load(artist.artistImages!![0].url)
                    .transform(BlurEffectUtils(this, 20))
                    .into(iv_collapsing_artist)
            Picasso.with(this).load(artist.artistImages!![0].url).into(civ_artist)
        } else {
            val imageHolder = "http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png"
            civ_artist!!.visibility = View.GONE
            Picasso.with(this)
                    .load(imageHolder)
                    .transform(BlurEffectUtils(this, 20))
                    .into(iv_collapsing_artist)
        }

        txt_title_artist!!.text = artist.name
        txt_subtitle_artist!!.text = artist.name
        val totalFollowers = resources.getQuantityString(R.plurals.numberOfFollowers,
                artist.followers!!.totalFollowers, artist.followers!!.totalFollowers)
        txt_followers_artist!!.text = totalFollowers
    }

    private fun setTracks(tracks: List<Track>): String {
        val gson = GsonBuilder().create()
        val trackType = object : TypeToken<List<Track>>() {

        }.type
        return gson.toJson(tracks, trackType)
    }

    override fun context(): Context {
        return this@TracksActivity
    }

    companion object {

        val EXTRA_REPOSITORY = "EXTRA_ARTIST"
        val EXTRA_TRACK_POSITION = "EXTRA_TRACK_POSITION"
        val EXTRA_TRACKS = "EXTRA_TRACKS"
    }
}