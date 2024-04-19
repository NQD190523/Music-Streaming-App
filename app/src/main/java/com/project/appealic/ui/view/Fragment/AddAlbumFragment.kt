package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.AlbumsResultAdapter
import com.project.appealic.ui.view.Adapters.ArtistResultAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class AddAlbumFragment: Fragment()  {

    private lateinit var songViewModel: SongViewModel
    private lateinit var listViewAlbums: ListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_favorite_albums, container, false)
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        listViewAlbums = view.findViewById(R.id.lv_fav_album)
        songViewModel.albums.observe(viewLifecycleOwner, Observer { albums ->
            val adapter = AlbumsResultAdapter(requireContext(), albums)
            listViewAlbums.adapter = adapter
        })
        songViewModel.getAllAlbums()
        return view
    }
}