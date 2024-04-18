package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.PlaylistAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class SearchMainFragment: Fragment() {
    private lateinit var listSong: ListView
    private lateinit var songViewModel: SongViewModel
    private lateinit var searchtest: androidx.appcompat.widget.SearchView
    private lateinit var listArtist: ListView
    private lateinit var listAlbum: ListView

    private var originalTracks: List<Track> = emptyList() // Danh sách gốc

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_main, container, false)
        val gridView: GridView = view.findViewById(R.id.gridviewSearch)

        // Danh sách hình ảnh từ thư mục drawable
        val imageList = listOf(
            R.drawable.playlist1,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok
        )

        // Tạo adapter và thiết lập cho GridView
        val adapter = PlaylistAdapter(requireContext(), imageList)
        gridView.adapter = adapter

        return view
    }
}


