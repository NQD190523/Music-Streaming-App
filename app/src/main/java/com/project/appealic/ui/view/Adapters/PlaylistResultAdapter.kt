package com.project.appealic.ui.view.Adapters

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Playlist

class PlaylistResultAdapter(context: Context, private var playlists: List<Playlist>) : ArrayAdapter<Playlist>(context, 0, playlists) {
    private val storage = FirebaseStorage.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_search_result_playlist, parent, false)
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

        // Gọi hàm để lấy tổng số bài hát từ Firebase và cập nhật UI
        getTotalSongsFromFirebase(currentPlaylist.playlistId) { totalSongs ->
            txtPlaylistTotalSongs.text = (totalSongs.toString()+" Songs")
        }

        return view
    }

    private fun getTotalSongsFromFirebase(playlistId: String, onResult: (Int) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val playlistsRef = db.collection("playlists")
        playlistsRef.document(playlistId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val playlistData = task.result?.toObject(Playlist::class.java)
                val trackIds = playlistData?.trackIds ?: emptyList()
                val totalSongs = trackIds.size
                Log.d(TAG, "Total songs: $totalSongs")
                onResult(totalSongs)
            } else {
                Log.d(TAG, "Failed to get playlist data", task.exception)
                onResult(0)
            }
        }
    }
    fun updateData(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}