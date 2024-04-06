package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.project.appealic.R
import com.project.appealic.data.model.SongEntity

class LikedSongsAdapter(context: Context, resource: Int, objects: List<SongEntity>) :
    ArrayAdapter<SongEntity>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView =
                LayoutInflater.from(context).inflate(R.layout.activity_liked_song, parent, false)
        }

        val currentSong = getItem(position)

        val songTitleTextView = listItemView!!.findViewById<TextView>(R.id.txtSongName)
        songTitleTextView.text = currentSong?.songName

        val singerTextView = listItemView.findViewById<TextView>(R.id.txtSinger)
        singerTextView.text = currentSong?.singer

        val songImageView = listItemView.findViewById<ImageView>(R.id.imvPhoto)
        currentSong?.let { song ->
            song.thumbUrl?.let { imageUrl ->
                Glide.with(context)
                    .load(imageUrl)
                    .into(songImageView)
            }
        }



        return listItemView
    }
}