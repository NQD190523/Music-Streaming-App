package com.project.appealic.ui.view.Adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.data.model.Artist

class FavouriteArtistAdapter(context: Context, private val artists: List<Artist>) :
    ArrayAdapter<Artist>(context, R.layout.item_favourite_artist, artists) {

    private val storage = Firebase.storage

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_favourite_artist, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentArtist = getItem(position)
        val gsReference = currentArtist?.ImageResource?.let { storage.getReferenceFromUrl(it) }
        Glide.with(context)
            .load(gsReference)
            .into(viewHolder.artistImageView)
        viewHolder.artistNameTextView.text = currentArtist?.Name

        return view
    }

    private class ViewHolder(view: View) {
        val artistImageView: ImageView = view.findViewById(R.id.imvPhotoArtist)
        val artistNameTextView: TextView = view.findViewById(R.id.txtArtistName)
    }
}
