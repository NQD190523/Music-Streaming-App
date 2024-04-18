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
import com.project.appealic.data.model.Playlist

class PlaylistForYouAdapter(private val context: Context, private var playlists: List<Playlist>)
    : RecyclerView.Adapter<PlaylistForYouAdapter.PlaylistForYouViewHolder>() {
    private val storage = FirebaseStorage.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistForYouViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_playlistforyou, parent, false)
        return PlaylistForYouViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: PlaylistForYouViewHolder,
        position: Int
    ) {
        var currentPlaylist = playlists[position]
        holder.playlistForYouName.text = currentPlaylist.playlistName
        holder.artistPlaylist.text = currentPlaylist.playlistArtists
        currentPlaylist.playlistThumb?.let { imageUrl ->
            val gsReference = storage.getReferenceFromUrl(imageUrl)
            Glide.with(context)
                .load(gsReference)
                .into(holder.playlistForYouPhoto)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updateData(playlists: List<Playlist>) {
        this.playlists = playlists
        notifyDataSetChanged()
    }

    class PlaylistForYouViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistForYouName: TextView = view.findViewById(R.id.playlistForUName)
        val artistPlaylist: TextView = view.findViewById(R.id.artistPlaylist)
        val playlistForYouPhoto: ImageView = view.findViewById(R.id.playlistForUPhoto)
    }
}
