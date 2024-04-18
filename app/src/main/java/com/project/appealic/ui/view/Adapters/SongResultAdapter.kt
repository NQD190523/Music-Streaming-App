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
import com.project.appealic.data.model.Track

class SongResultAdapter(context: Context, private val tracks: List<Track>) :
    ArrayAdapter<Track>(context, 0, tracks) {

    lateinit var onAddPlaylistClick: (Track) -> Unit

    fun setOnAddPlaylistClickListener(listener: (Track) -> Unit) {
        onAddPlaylistClick = listener
    }

    lateinit var moreActionClickListener: (Track) -> Unit

    fun setOnMoreActionClickListener(listener: (Track) -> Unit) {
        this.moreActionClickListener = listener
    }

    private val storage = FirebaseStorage.getInstance()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_search_result_song, parent, false)

        val currentTrack = getItem(position)
        val songNameTextView = view.findViewById<TextView>(R.id.txtSongName)
        val singerTextView = view.findViewById<TextView>(R.id.txtSinger)
        val songImageView = view.findViewById<ImageView>(R.id.imvPhoto)
        val btnMoreAction: ImageView = view.findViewById(R.id.btnMoreAction)

        currentTrack?.let { track ->
            songNameTextView.text = track.trackTitle
            singerTextView.text = track.artist
            track.trackImage?.let { imageUrl ->
                val gsReference = storage.getReferenceFromUrl(imageUrl)
                Glide.with(context)
                    .load(gsReference)
                    .into(songImageView)
            }

            btnMoreAction.setOnClickListener {
                moreActionClickListener.invoke(track)
            }
        }

        return view
    }

    interface OnAddPlaylistClickListener {
        fun onAddPlaylistClick(track: Track)
    }

    interface OnMoreActionClickListener {
        fun onMoreActionClick(track: Track)
    }

}