package com.yan.feracode.spotify.view.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import butterknife.BindView
import butterknife.ButterKnife
import com.yan.feracode.R
import com.yan.feracode.spotify.data.api.client.SpotifyClient
import com.yan.feracode.spotify.data.model.Artist
import com.yan.feracode.spotify.interactor.ArtistsInteractor
import com.yan.feracode.spotify.presenter.ArtistsPresenter
import com.yan.feracode.spotify.view.activity.ArtistsActivity
import com.yan.feracode.spotify.view.activity.SpotifyLoginActivity
import com.yan.feracode.spotify.view.activity.TracksActivity
import com.yan.feracode.spotify.view.adapter.ArtistsAdapter
import kotlinx.android.synthetic.main.fragment_artists.*
import kotlinx.android.synthetic.main.toolbar.*

class ArtistsFragment : Fragment(), ArtistsPresenter.View, SearchView.OnQueryTextListener {



    @BindView(R.id.toolbar)
    internal var tlbar: Toolbar? = null
    @BindView(R.id.rv_artists)
    internal var rv_artist: RecyclerView? = null
    @BindView(R.id.pv_artists)
    internal var pv_artist: ProgressBar? = null
    @BindView(R.id.txt_line_artists)
    internal var txt_line_artist: TextView? = null

    private var artistsPresenter: ArtistsPresenter? = null

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        artistsPresenter = ArtistsPresenter(ArtistsInteractor(SpotifyClient(requireActivity())))
        artistsPresenter!!.view = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_artist = rv_artists
        tlbar = toolbar
        pv_artist = pv_artists
        txt_line_artist = txt_line_artists
        ButterKnife.bind(this, view)
        setupToolbar()
        setupRecyclerView()
    }

    override fun onDestroy() {
        artistsPresenter!!.terminate()
        super.onDestroy()
    }

    override fun getContext(): Context? {
        return activity
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_music, menu)
        setupSearchView(menu!!)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        artistsPresenter!!.onSearchArtist(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return true
    }

    override fun showLoading() {
        pv_artist!!.visibility = View.VISIBLE
        txt_line_artist!!.visibility = View.GONE
        rv_artist!!.visibility = View.GONE
    }

    override fun hideLoading() {
        pv_artist!!.visibility = View.GONE
        rv_artist!!.visibility = View.VISIBLE
    }

    override fun showArtistNotFoundMessage() {
        pv_artist!!.visibility = View.GONE
        txt_line_artist!!.visibility = View.VISIBLE
        txt_line_artist!!.text = getString(R.string.error_artist_not_found)
    }

    override fun showConnectionErrorMessage() {
        pv_artist!!.visibility = View.GONE
        txt_line_artist!!.visibility = View.VISIBLE
        txt_line_artist!!.text = getString(R.string.error_internet_connection)
    }

    override fun showServerError() {
        pv_artist!!.visibility = View.GONE
        txt_line_artist!!.visibility = View.VISIBLE
        txt_line_artist!!.text = getString(R.string.error_server_internal)
    }

    override fun renderArtists(artists: List<Artist>) {
        val adapter = rv_artist!!.adapter as ArtistsAdapter?
        adapter!!.setArtists(artists)
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView(menu: Menu) {
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView.queryHint = getString(R.string.search_hint)
        searchView.maxWidth = tlbar!!.width
        searchView.setOnQueryTextListener(this)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(tlbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_navigation_menu)
        }
    }

    private fun setupRecyclerView() {

        val adapter = ArtistsAdapter()
        adapter.setItemClickListener(object : ArtistsAdapter.ItemClickListener {
            override fun onItemClick(artist: Artist, position: Int) {
                artistsPresenter!!.launchArtistDetail(artist)
            }
        })

        rv_artist!!.adapter = adapter
    }

    override fun launchArtistDetail(artist: Artist) {
        val intent = Intent(context, TracksActivity::class.java)
        intent.putExtra(TracksActivity.EXTRA_REPOSITORY, artist)
        val AUTH_TOKEN = activity!!.intent.getStringExtra(SpotifyLoginActivity.AUTH_TOKEN)
        intent.putExtra("AUTH_TOKEN", AUTH_TOKEN)

        startActivity(intent)
    }

    override fun context(): Context = requireContext()
}
