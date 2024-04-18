package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track

class LikedSongsAdapter(context: Context, resource: Int, objects: List<Track>) :
    ArrayAdapter<Track>(context, resource, objects) {
    private val storage = FirebaseStorage.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false)

        val currentSong = getItem(position)
        val songTitleTextView = view!!.findViewById<TextView>(R.id.txtSongName)
        val singerTextView = view.findViewById<TextView>(R.id.txtSinger)
        val songImageView = view.findViewById<ImageView>(R.id.imvPhoto)

        currentSong?.let {
            songTitleTextView.text = it.trackTitle
            singerTextView.text = it.artist
            it.trackImage?.let { imageUrl ->
                val gsReference = storage.getReferenceFromUrl(imageUrl)
                Glide.with(context)
                    .load(gsReference)
                    .into(songImageView)
            }
        }

        return view
    }
}