package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.Track
import com.project.appealic.ui.view.Adapters.PlaylistAdapter
import com.project.appealic.ui.viewmodel.SongViewModel

class SearchMainFragment : Fragment() {
    private lateinit var listSong: ListView
    private lateinit var songViewModel: SongViewModel
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var listArtist: ListView
    private lateinit var listAlbum: ListView

    private var originalTracks: List<Track> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_main, container, false)

        val gridView: GridView = view.findViewById(R.id.gridviewSearch)
        val imageList = listOf(
            R.drawable.playlist1,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok
        )

        // Tạo adapter và thiết lập cho GridView
        val adapter = PlaylistAdapter(requireContext(), imageList, object : PlaylistAdapter.OnPlaylistClickListener {
            override fun onPlaylistClick(position: Int) {
                val drawable = imageList[position]

                val bundle = Bundle().apply {
                    putInt("selected_drawable", drawable)
                }
                val genresFragment = GenresFragment()
                genresFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmenthome, genresFragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        gridView.adapter = adapter

        return view
    }
}