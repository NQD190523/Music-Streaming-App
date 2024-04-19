package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.Album
import com.project.appealic.data.model.SongEntity
import com.project.appealic.ui.view.ActivityHome
import com.project.appealic.ui.view.ActivityMusicControl
import com.project.appealic.ui.view.ActivityNotification
import com.project.appealic.ui.view.Adapters.AlbumsResultAdapter
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.ArtistResultAdapter
import com.project.appealic.ui.view.Adapters.PlaylistResultAdapter
import com.project.appealic.ui.view.Adapters.SongResultAdapter
import com.project.appealic.ui.view.setOnItemClickListener
import java.util.ArrayList

class SearchResultFragment: Fragment() {
    private lateinit var listSong: ListView
    private lateinit var songViewModel: SongViewModel
    private lateinit var listArtist: ListView
    private lateinit var listPlaylist: ListView
    private lateinit var listAlbum: ListView

    private lateinit var searchDatabase: DatabaseReference
    private var originalTracks: List<Track> = emptyList() // Danh sách gốc

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)

        listSong = view.findViewById(R.id.lvSearchResultSongs)
        listArtist = view.findViewById(R.id.lvSearchResultArtists)
        listPlaylist = view.findViewById(R.id.lvSearchResultPlaylists)
        listAlbum = view.findViewById(R.id.lvSearchResultAlbums)

        // Khởi tạo SongViewModel
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        // Nhận dữ liệu từ Bundle
        val searchQuery = arguments?.getString("search_query")
        Log.d("SearchResultsActivity", "Received searchQuery: $searchQuery")
        if (!searchQuery.isNullOrEmpty()) {
            // Gọi phương thức trong ViewModel để tải dữ liệu từ Firebase dựa trên searchQuery
            songViewModel.loadSearchResults(searchQuery)

            // Quan sát LiveData _tracks để cập nhật ListView khi có kết quả tìm kiếm mới
            songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
                val adapterSong = SongResultAdapter(requireContext(), tracks)
                listSong.adapter = adapterSong

                // Thiết lập OnItemClickListener cho ListView
                listSong.setOnItemClickListener(requireContext(), songViewModel, tracks)
            })

            songViewModel.artists.observe(viewLifecycleOwner, Observer { artists->
                val adapterArtist = ArtistResultAdapter(requireContext(), artists)
                listArtist.adapter = adapterArtist
            })

            songViewModel.playlists.observe(viewLifecycleOwner, Observer { playlits ->
                val adapterPlaylists = PlaylistResultAdapter(requireContext(), playlits)
                listPlaylist.adapter = adapterPlaylists
            })

            songViewModel.albums.observe(viewLifecycleOwner, Observer { albums ->
                val adapterAlbum = AlbumsResultAdapter(requireContext(), albums)
                listAlbum.adapter = adapterAlbum

                listAlbum.setOnItemClickListener { parent, view, position, id ->
                    val selectedAlbum = adapterAlbum.getItem(position)

                    val bundle = Bundle().apply {
                        putParcelable("selected_album", selectedAlbum)
                    }

                    val albumPageFragment = AlbumPageFragment()
                    albumPageFragment.arguments = bundle

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmenthome, albumPageFragment)
                        .addToBackStack(null)
                        .commit()
                }
            })
}

        return view
    }
}