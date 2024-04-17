package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.project.appealic.ui.view.Adapters.ArtistAdapter

class SearchResultFragment: Fragment() {
    private lateinit var listSong: ListView
    private lateinit var songViewModel: SongViewModel
    private lateinit var listArtist: RecyclerView
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
        listArtist.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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
                val adapterSong = NewReleaseAdapter(requireContext(), tracks)
                listSong.adapter = adapterSong
            })
            songViewModel.artists.observe(viewLifecycleOwner, Observer { artists->
                val adapterArtist = ArtistAdapter(requireContext(), artists)
                listArtist.adapter = adapterArtist
            })
        }

        return view
    }
}


//        // Lấy dữ liệu từ ViewModel và hiển thị trong ListView
//        songViewModel.tracks.observe(this, Observer { tracks ->
//            originalTracks = tracks // Lưu danh sách gốc
//            val adapter = NewReleaseAdapter(this, tracks)
//            listSong.adapter = adapter
//        })
//        songViewModel.getAllTracks()

//        searchtest.setOnQueryTextListener(object :
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterList(newText)
//                return true
//            }
//        })
//    }
//    private fun filterList(newText: String?) {
//        if (newText != null && newText.isNotEmpty()) {
//            Log.d("SearchResultsActivity", "Filtering tracks with query: $newText")
//            val filteredTracks = originalTracks.filter { it.trackTitle?.contains(newText, ignoreCase = true) == true }
//            Log.d("SearchResultsActivity", "Filtered tracks count: ${filteredTracks.size}")
//            Log.d("SearchResultsActivity", "Original tracks: $originalTracks")
//            Log.d("SearchResultsActivity", "Filtered tracks: $filteredTracks")
//            val adapter = NewReleaseAdapter(this, filteredTracks)
//            listSong.adapter = adapter
//            listSong.invalidateViews()
//        } else {
//            val adapter = NewReleaseAdapter(this, originalTracks)
//            listSong.adapter = adapter
//        }
//    }



//            val firebaseSearchQuery = searchDatabase.orderByChild("tracks").startAt(searchtest)
//            // Khởi tạo SongViewModel
//            val factory = SongViewModelFactory(SongRepository(application), UserRepository(application))
//            songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)
//
//            // Lấy dữ liệu từ ViewModel và hiển thị trong ListView
//            songViewModel.tracks.observe(this, Observer { tracks ->
//                val adapter = NewReleaseAdapter(this, tracks)
//                listSong.adapter = adapter
//            })
//            songViewModel.getAllTracks()
//        }
//    } else{
//        val firebaseSearchQuery = searchDatabase.orderByChild("name").startAt(newText).endAt(newText + "\uf8ff")
//            FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Track, TrackViewHolder>(
//                Track ::class.java,
//                R.layout.item_song,
//                TrackViewHolder::class.java,
//                firebaseSearchQuery
//
//            ){
//
//            }
//
//        }    }

// Khởi tạo SongViewModel
//        val factory = SongViewModelFactory(SongRepository(application), UserRepository(application))
//        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)
//
//        // Lấy từ khóa tìm kiếm từ Intent
//        val searchQuery = intent.getStringExtra("searchQuery")
//        Log.d("SearchResultsActivity", "Search query new: $searchQuery")
//
//        // Gọi phương thức để tải dữ liệu từ Firebase dựa trên từ khóa tìm kiếm
//        loadSearchResults(searchQuery)

//    private fun loadSearchResults(searchQuery: String?) {
//        // Gọi phương thức trong ViewModel để tải dữ liệu từ Firebase
//        songViewModel.loadSearchResults(searchQuery)
//
//        // Quan sát LiveData _tracks để cập nhật ListView khi có kết quả tìm kiếm mới
//        songViewModel.tracks.observe(this, Observer { tracks ->
//            val adapter = NewReleaseAdapter(this, tracks)
//            listView.adapter = adapter
//        })
//    }
//}
