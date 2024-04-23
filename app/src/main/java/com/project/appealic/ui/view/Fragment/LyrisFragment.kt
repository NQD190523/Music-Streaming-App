package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R

class LyrisFragment : Fragment() {
    private var songTitle: String? = null
    private var artistName: String? = null
    private var trackImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            songTitle = it.getString("songTitle")
            artistName = it.getString("artistName")
            trackImage = it.getString("trackImage")


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lyris, container, false)
        view.findViewById<TextView>(R.id.txtSongName).text = songTitle
        view.findViewById<TextView>(R.id.txtSinger).text = artistName
        val imageView = view.findViewById<ImageView>(R.id.imvPhoto)
        println(trackImage)
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(trackImage.toString())
        Glide.with(this)
            .load(storageReference)
            .into(imageView)

        return view

    }
    companion object {
        fun newInstance(
            songTitle: String?,
            artistName: String?,
            trackImage: String?,

        ): LyrisFragment {
            return LyrisFragment().apply {
                arguments = Bundle().apply {
                    putString("songTitle", songTitle)
                    putString("artistName", artistName)
                    putString("trackImage", trackImage)

                }
            }
        }
    }
}