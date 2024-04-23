package com.project.appealic.ui.view.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.PlaylistResultAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory


class AddPlaylistLibraryFragment : Fragment()  {
    private lateinit var songViewModel: SongViewModel
    private lateinit var listViewPlaylist: ListView

    @SuppressLint("MissingInflatedId")
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
        val songFactory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, songFactory)[SongViewModel::class.java]

        listViewPlaylist = view.findViewById(R.id.lvPlaylists)
        songViewModel.playlists.observe(viewLifecycleOwner, Observer { playlists ->
            val adapter = PlaylistResultAdapter(requireContext(), playlists)
            listViewPlaylist.adapter = adapter
        })
        songViewModel.getAllPlaylists()
        return view
    }
}