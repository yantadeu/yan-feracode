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
import com.yan.feracode.spotify.data.model.Artist
import kotlinx.android.synthetic.main.item_artist.*
import kotlinx.android.synthetic.main.item_artist.view.*

internal class ArtistsAdapter : RecyclerView.Adapter<ArtistsAdapter.ArtistsViewHolder>() {

    private var artists: List<Artist>? = null
    private var itemClickListener: ItemClickListener? = null

    init {
        artists = emptyList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtistsViewHolder, position: Int) {
        val artist = artists!![position]
        holder.artist = artist
        holder.textView.text = artist.name

        if (artist.artistImages!!.isNotEmpty()) {

            for (i in artist.artistImages!!.indices) {
                //artist.artistImages!!.size
                Picasso.with(holder.imageView.context)
                        .load(artist.artistImages!![0].url)
                        .into(holder.imageView)
            }
        } else {
            val imageHolder = "http://www.iphonemode.com/wp-content/uploads/2016/07/Spotify-new-logo.jpg"
            Picasso.with(holder.imageView.context).load(imageHolder).into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.onItemClick(artist, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return artists!!.size
    }

    fun setArtists(artists: List<Artist>) {
        this.artists = artists
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(artist: Artist, position: Int)
    }

    internal class ArtistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.img_view_artist_image

        val textView: TextView = itemView.txt_artist_name

        lateinit var artist: Artist

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
