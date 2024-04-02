package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Track

class RecentlySongAdapter(private val context: Context, private val tracks: List<Track>) :
    RecyclerView.Adapter<RecentlySongAdapter.RecentlySongViewHolder>() {

    private val storage = FirebaseStorage.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlySongViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        return RecentlySongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: RecentlySongViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.songNameTextView.text = currentTrack.trackTitle
        holder.singerTextView.text = currentTrack.artist
        currentTrack.trackImage?.let { imageUrl ->
            val gsReference = storage.getReferenceFromUrl(imageUrl)
            Glide.with(context)
                .load(gsReference)
                .into(holder.songImageView)
        }
    }

    class RecentlySongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songNameTextView: TextView = view.findViewById(R.id.txtSongName)
        val singerTextView: TextView = view.findViewById(R.id.txtSinger)
        val songImageView: ImageView = view.findViewById(R.id.imvPhoto)
    }
}