package com.yan.feracode.spotify.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.yan.feracode.R
import com.yan.feracode.spotify.data.model.Track
import kotlinx.android.synthetic.main.item_track.view.*

internal class TracksAdapter : RecyclerView.Adapter<TracksAdapter.TracksViewHolder>() {

    private var tracks: List<Track>? = null
    private var itemClickListener: ItemClickListener? = null

    init {
        tracks = emptyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TracksViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val track = tracks!![position]

        holder.txt_title_tracks.text = (position + 1).toString() + "." + track.name
        holder.txt_track_album.text = track.album!!.albumName

        if (track.album!!.trackImages!!.isNotEmpty()) {
            holder.imageView.scaleType = ImageView.ScaleType.FIT_XY
            for (i in track.album!!.trackImages!!.indices) {
                if (track.album!!.trackImages!!.isNotEmpty()) {
                    Picasso.with(holder.imageView.context)
                            .load(track.album!!.trackImages!![0].url)
                            .into(holder.imageView)
                }
            }
        } else {

            Picasso.with(holder.imageView!!.context)
                    .load("http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png")
                    .into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.onItemClick(tracks!!, track, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks!!.size
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(tracks: List<Track>, track: Track, position: Int)
    }

    internal class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var imageView: ImageView = itemView.iv_track

        var txt_title_tracks: TextView = itemView.txt_track_title

        var txt_track_album: TextView = itemView.txt_track_album

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
