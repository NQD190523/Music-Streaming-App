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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Album

class AlbumsResultAdapter(context: Context, private var albums: List<Album>) : ArrayAdapter<Album>(context, 0, albums) {
    private val storage = FirebaseStorage.getInstance()

    class AlbumsResultAdapter(context: Context, private var albums: List<Album>) :
        ArrayAdapter<Album>(context, 0, albums) {
        private val storage = FirebaseStorage.getInstance()

        // Interface for on item click listener
        interface OnItemClickListener {
            fun onItemClick(album: Album)
        }

        // Field to hold the on item click listener
        private var onItemClickListener: OnItemClickListener? = null

        // Method to set the on item click listener
        fun setOnItemClickListener(listener: OnItemClickListener) {
            onItemClickListener = listener
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_search_result_album, parent, false)
            val currentAlbum = albums[position]
            val albumName = view.findViewById<TextView>(R.id.txtAlbumName)
            val albumPhoto = view.findViewById<ImageView>(R.id.imvPhotoAlbum)
            val txtAlbumTotalSongs = view.findViewById<TextView>(R.id.txtAlbumTotalSongs)

            albumName.text = currentAlbum.title
            currentAlbum.thumbUrl?.let { imageUrl ->
                val gsReference = storage.getReferenceFromUrl(imageUrl)
                Glide.with(context)
                    .load(gsReference)
                    .into(albumPhoto)
            }

            // Gọi hàm để lấy tổng số bài hát từ Firebase và cập nhật UI
            getTotalSongsFromFirebase(currentAlbum.albumId) { totalSongs ->
                txtAlbumTotalSongs.text = (totalSongs.toString() + " Songs")
            }

            // Set the on click listener to the view
            view.setOnClickListener { onItemClickListener?.onItemClick(currentAlbum) }

            return view
        }

        private fun getTotalSongsFromFirebase(albumId: String, onResult: (Int) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val albumsRef = db.collection("albums")
            albumsRef.document(albumId).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val albumData = task.result?.toObject(Album::class.java)
                    val trackIds = albumData?.trackIds ?: emptyList()
                    val totalSongs = trackIds.size
                    onResult(totalSongs)
                } else {
                    onResult(0)
                }
            }
        }

        fun updateData(newAlbums: List<Album>) {
            albums = newAlbums
            notifyDataSetChanged()
        }
    }
}