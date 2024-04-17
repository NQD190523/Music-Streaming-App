package com.project.appealic.ui.view.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.appealic.R
import com.project.appealic.data.model.PlayListEntity

class UserPlaylistAdapter (
    private val context: Context,
    private var playlists: List<PlayListEntity>
) :
    BaseAdapter() {

    private val storage = FirebaseStorage.getInstance()

    override fun getCount(): Int {
        return playlists.size
    }

    override fun getItem(position: Int): Any {
        return playlists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_playlist, parent, false)
            holder = ViewHolder(view)
            view?.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val currentPlaylist = playlists[position]
        holder.playlistNameTextView.text = currentPlaylist.playListName
        // Load the image from Firebase Storage
        val imageRef = storage.getReferenceFromUrl(currentPlaylist.playListThumb.toString())
        Glide.with(context).load(imageRef).into(holder.playlistImageView)

        return view
    }

    private class ViewHolder(view: View) {
        val playlistNameTextView: TextView = view.findViewById(R.id.txtUserPlaylistName)
        val playlistImageView: ImageView = view.findViewById(R.id.playlistImage)
    }
}