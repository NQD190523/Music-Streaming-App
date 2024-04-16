package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.project.appealic.R

class AddPlaylistLibraryFragment : Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_song, container, false)

        view.findViewById<FrameLayout>(R.id.flAddPlaylist).setOnClickListener {
            val addPlaylistFragment = AddPlaylistFragment()
            addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
        }
        return view
    }
}