package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.utils.ImageUtils

class ArtistAdapter(private val context: Context, private val artists: List<Artist>) :
    RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

        private val storage =Firebase.storage
    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistImageView: ImageView = itemView.findViewById(R.id.imvPhotoArtist)
        val artistNameTextView: TextView = itemView.findViewById(R.id.txtArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_artist,
            parent,
            false
        )
        return ArtistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val currentArtist = artists[position]
        val gsReference = currentArtist.ImageResource?.let { storage.getReferenceFromUrl(it) }
        Glide.with(context)
            .load(gsReference)
            .into(holder.artistImageView)
        holder.artistNameTextView.text = currentArtist.Name
    }

    override fun getItemCount() = artists.size
}