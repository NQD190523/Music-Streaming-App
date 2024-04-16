package com.project.appealic.ui.view.Adapters

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.appealic.R
import com.project.appealic.data.model.UserPlaylist
import com.project.appealic.data.model.UserWithPlayLists

class UserPlaylistAdapter(private val context: Context, private val userWithPlayLists: UserWithPlayLists) : BaseAdapter() {

    override fun getCount(): Int {
        return userWithPlayLists.playLists.size
    }

    override fun getItem(position: Int): Any {
        return userWithPlayLists.playLists[position]
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

        val currentItem = userWithPlayLists.user
        val playList = userWithPlayLists.playLists[position]
        holder.userPlaylistName.text = playList.playListName
        holder.userName.text = currentItem.name
         holder.playlistImage.setImageResource(playList.playListThumb)

        return view
    }

    private class ViewHolder {
        lateinit var userPlaylistName: TextView
        lateinit var userName: TextView
        lateinit var playlistImage: ImageView
    }
}