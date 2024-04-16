package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.media3.common.util.Log
import com.bumptech.glide.Glide
import com.project.appealic.R

class InfoMusicFragment : Fragment() {
    private var songTitle: String? = null
    private var artistName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            songTitle = it.getString(ARG_SONG_TITLE)
            artistName = it.getString(ARG_SINGER_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_music, container, false)
        view.findViewById<TextView>(R.id.txtSongName).text = songTitle
        view.findViewById<TextView>(R.id.txtSinger).text = artistName
        return view
    }

    companion object {
        private const val ARG_SONG_TITLE = "SONG_TITLE"
        private const val ARG_SINGER_NAME = "SINGER_NAME"

        fun newInstance(songTitle: String, singerName: String) = InfoMusicFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_SONG_TITLE, songTitle)
                putString(ARG_SINGER_NAME, singerName)
            }
        }
    }
}