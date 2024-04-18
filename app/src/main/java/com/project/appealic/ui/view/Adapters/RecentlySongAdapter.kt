package com.project.appealic.ui.view.Adapters

import android.annotation.SuppressLint
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
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track

class RecentlySongAdapter(private val context: Context, private var songs: List<SongEntity>) :
    RecyclerView.Adapter<RecentlySongAdapter.RecentlySongViewHolder>() {
    var onItemClick: ((SongEntity) -> Unit)? = null

    private val storage = FirebaseStorage.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlySongViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.song_card_item, parent, false)
        return RecentlySongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: RecentlySongViewHolder, position: Int) {
        val currentSong = songs[position]
        holder.songNameTextView.text = currentSong.songName
        holder.singerTextView.text = currentSong.singer
        currentSong.thumbUrl?.let { imageUrl ->
            val gsReference = storage.getReferenceFromUrl(imageUrl)
            Glide.with(context)
                .load(gsReference)
                .into(holder.songImageView)
        }

        // Thêm setOnClickListener cho itemView
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentSong)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newSongs: List<SongEntity>) {
        this.songs = newSongs
        notifyDataSetChanged()
    }

    class RecentlySongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songNameTextView: TextView = view.findViewById(R.id.txtSongName)
        val singerTextView: TextView = view.findViewById(R.id.txtSinger)
        val songImageView: ImageView = view.findViewById(R.id.imvPhoto)
    }
}
