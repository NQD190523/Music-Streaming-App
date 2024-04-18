package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.project.appealic.R
import com.project.appealic.data.model.Playlist

class SleepingPlaylistAdapter(private val context: Context) : BaseAdapter() {

    private val playlists = mutableListOf<Playlist>()

    override fun getCount(): Int = playlists.size

    override fun getItem(position: Int): Any = playlists[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_playlist, parent, false)

        val playlist = playlists[position]

//        view.findViewById<TextView>(R.id.tvPlaylistName).text = playlist.playlistName
//        view.findViewById<TextView>(R.id.tvSongCount).text = "${playlist.songs.size} songs"

        return view
    }

    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}