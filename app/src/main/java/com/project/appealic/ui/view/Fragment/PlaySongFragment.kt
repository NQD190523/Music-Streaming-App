package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R

class PlaySongFragment : Fragment() {

    private var trackImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackImageUrl = it.getString(ARG_TRACK_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_play, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imvGround)
        trackImageUrl?.let { imageUrl ->
            Glide.with(this).load(imageUrl).into(imageView)
        }
        return view
    }

    companion object {
        private const val ARG_TRACK_IMAGE = "TRACK_IMAGE"

        fun newInstance(trackImage: String) = PlaySongFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TRACK_IMAGE, trackImage)
            }
        }
    }
}