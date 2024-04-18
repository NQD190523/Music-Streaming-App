package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Playlist

class PlaylistResultAdapter(context: Context, private var playlists: List<Playlist>) : ArrayAdapter<Playlist>(context, 0, playlists) {
    private val storage = FirebaseStorage.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_playlistforyou, parent, false)
        val currentPlaylist = playlists[position]
        val playlistForYouName = view.findViewById<TextView>(R.id.txtPlaylistName)
        val playlistForYouPhoto = view.findViewById<ImageView>(R.id.imvPhotoPlaylist)
        val txtPlaylistTotalSongs = view.findViewById<TextView>(R.id.txtPlaylistTotalSongs)

        playlistForYouName.text = currentPlaylist.playlistName
        currentPlaylist.playlistThumb?.let { imageUrl ->
            val gsReference = storage.getReferenceFromUrl(imageUrl)
            Glide.with(context)
                .load(gsReference)
                .into(playlistForYouPhoto)
        }

        // Giả sử bạn có một hàm để lấy số lượng track từ Firebase
        val totalSongs = getTotalSongsFromFirebase(currentPlaylist.playlistId)
        txtPlaylistTotalSongs.text = totalSongs.toString()

        return view
    }

    private fun getTotalSongsFromFirebase(playlistId: String): Int {
         val songsRef = FirebaseDatabase.getInstance().getReference("playlists/$playlistId/trackIds")
         val totalSongs = songsRef.get().addOnSuccessListener { snapshot ->
             snapshot.childrenCount
         }
        return 0
    }

    fun updateData(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}
