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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackImage = arguments?.getString("TRACK_IMAGE")
        val songImageView = view?.findViewById<ImageView>(R.id.imvGround)
        if (songImageView != null && trackImage != null) {
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(trackImage)
            Glide.with(this)
                .load(storageReference)
                .into(songImageView)
        }
    }
}