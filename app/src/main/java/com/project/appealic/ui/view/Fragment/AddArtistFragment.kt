package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.ArtistResultAdapter
import com.project.appealic.ui.view.Adapters.FavouriteArtistAdapter
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class AddArtistFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var listViewArtists: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_favorite_artists, container, false)

        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        listViewArtists = view.findViewById(R.id.lstAtirstFavourite)
        songViewModel.artists.observe(viewLifecycleOwner, Observer { artirts ->
            val adapter = FavouriteArtistAdapter(requireContext(), artirts)
            listViewArtists.adapter = adapter
        })
        songViewModel.getAllArtists()
        return view
    }
}