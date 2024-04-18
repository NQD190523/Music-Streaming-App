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
    var onItemClickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistForYouViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_playlistforyou, parent, false)
        return PlaylistForYouViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaylistForYouViewHolder, position: Int) {
        val currentPlaylist = playlists[position]
        holder.bind(currentPlaylist)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentPlaylist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updateData(playlists: List<Playlist>) {
        this.playlists = playlists
        notifyDataSetChanged()
    }

    inner class PlaylistForYouViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistForYouName: TextView = itemView.findViewById(R.id.playlistForUName)
        val artistPlaylist: TextView = itemView.findViewById(R.id.artistPlaylist)
        val playlistForYouPhoto: ImageView = itemView.findViewById(R.id.playlistForUPhoto)

        fun bind(playlist: Playlist) {
            playlistForYouName.text = playlist.playlistName
            artistPlaylist.text = playlist.playlistArtists

            playlist.playlistThumb?.let { imageUrl ->
                val gsReference = storage.getReferenceFromUrl(imageUrl)
                Glide.with(context)
                    .load(gsReference)
                    .into(playlistForYouPhoto)
            }
        }
    }
}