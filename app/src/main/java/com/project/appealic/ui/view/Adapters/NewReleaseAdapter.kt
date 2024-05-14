package com.project.appealic.ui.view.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.Fragment.MoreActionFragment

class NewReleaseAdapter(context: Context, private val tracks: List<Track>) :
    ArrayAdapter<Track>(context, 0, tracks) {

    var onItemClick: ((Track) -> Unit)? = null

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
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)

        val currentTrack = getItem(position)
        val songNameTextView = view.findViewById<TextView>(R.id.txtSongName)
        val singerTextView = view.findViewById<TextView>(R.id.txtSinger)
        val songImageView = view.findViewById<ImageView>(R.id.imvPhoto)
        val btnAddPlaylist: ImageView  = view.findViewById(R.id.btnAddPlaylist)
        val btnMoreAction: ImageView  = view.findViewById(R.id.btnMoreAction)

        currentTrack?.let { track ->
            songNameTextView.text = track.trackTitle
            singerTextView.text = track.artist
            track.trackImage?.let { imageUrl ->
                val gsReference = storage.getReferenceFromUrl(imageUrl)
                Glide.with(context)
                    .load(gsReference)
                    .into(songImageView)
            }

            btnAddPlaylist.setOnClickListener {
                onAddPlaylistClick.invoke(track)
            }

            btnMoreAction.setOnClickListener {
//                val activityContext = view.context
//                // Kiểm tra xem context có phải là Activity không
//                if (activityContext is Activity) {
//                    val fragmentManager =
//                        (activityContext as AppCompatActivity).supportFragmentManager
//                    // Tạo instance của MoreActionFragment và hiển thị
//                    val fragment = MoreActionFragment.newInstance(track)
//                    fragmentManager.beginTransaction()
//                        .replace(R.id.fragmenthome,fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }
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