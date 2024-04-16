package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.project.appealic.R
import com.project.appealic.data.model.PlayListEntity

class UserPlaylistAdapter(private val context: Context, private val playLists: List<PlayListEntity>) : BaseAdapter() {

    override fun getCount(): Int {
        return playLists.size
    }

    override fun getItem(position: Int): Any {
        return playLists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_playlist, parent, false)
            holder = ViewHolder()
            holder.userPlaylistName = view.findViewById(R.id.txtUserPlaylistName)
            holder.userName = view.findViewById(R.id.txtUserName)
            holder.playlistImage = view.findViewById(R.id.playlistImage)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val currentItem = playLists[position]
        holder.userPlaylistName.text = currentItem.playListName
//        holder.userName.text = currentItem.name
         holder.playlistImage.setImageResource(currentItem.playListThumb)

        return view
    }

    private class ViewHolder {
        lateinit var userPlaylistName: TextView
        lateinit var userName: TextView
        lateinit var playlistImage: ImageView
    }
}