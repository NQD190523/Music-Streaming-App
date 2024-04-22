package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.Fragment.MoreActionFragment
import java.lang.ref.WeakReference

class LikedSongsAdapter(context: Context, tracks: List<Track>) :
    ArrayAdapter<Track>(context, 0, tracks) {

    private val storage = FirebaseStorage.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutId = context.resources.getResourceEntryName(R.layout.item_playlist)
        println("Layout ID: $layoutId")
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false)
        val btnMoreAction: ImageView  = view.findViewById(R.id.btnMoreAction)

        val currentSong = getItem(position)
        val songTitleTextView = view.findViewById<TextView>(R.id.txtSongName)
        val singerTextView = view.findViewById<TextView>(R.id.txtSinger)
        val songImageView = view.findViewById<ImageView>(R.id.imageView)

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

        btnMoreAction.setOnClickListener {
            val track = getItem(position) // Lấy bài hát ứng với vị trí hiện tại trong danh sách
            track?.let {
                val bundle = Bundle().apply {
                    putString("SONG_TITLE", it.trackTitle)
                    putString("SINGER_NAME", it.artist)
                    putString("TRACK_IMAGE", it.trackImage)
                    putString("ARTIST_ID", it.artistId)
                    putString("TRACK_ID", it.trackId)
                }
                val moreActionFragment = MoreActionFragment()
                moreActionFragment.arguments = bundle
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                moreActionFragment.show(fragmentManager, "MoreActionFragment") // Hiển thị fragment
            }
        }

        return view
    }

}