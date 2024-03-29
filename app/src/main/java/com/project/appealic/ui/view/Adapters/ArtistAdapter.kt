package com.project.appealic.ui.view.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.appealic.R
import com.project.appealic.data.model.Artist

class ArtistAdapter(private val artists: List<Artist>) :
    RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistImageView: ImageView = itemView.findViewById(R.id.artistImageView)
        val artistNameTextView: TextView = itemView.findViewById(R.id.artistNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.artist_card_item,
            parent,
            false
        )
        return ArtistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val currentArtist = artists[position]
        holder.artistImageView.setImageResource(currentArtist.imageResource)
        holder.artistNameTextView.text = currentArtist.name
    }

    override fun getItemCount() = artists.size
}