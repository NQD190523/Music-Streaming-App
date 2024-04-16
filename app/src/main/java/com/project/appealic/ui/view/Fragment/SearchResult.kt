package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

//class SearchResultsActivity : Fragment() {
//    private lateinit var listSong: ListView
//    private lateinit var songViewModel: SongViewModel
//    private lateinit var searchtest: androidx.appcompat.widget.SearchView
//    private lateinit var listArtist: ListView
//    private lateinit var listAlbum: ListView
//
//    private lateinit var searchDatabase: DatabaseReference
//    private var originalTracks: List<Track> = emptyList() // Danh sách gốc
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_search_result)
//
//        listSong = findViewById(R.id.lvSearchResultSongs)
//        listArtist = findViewById(R.id.lvSearchResultArtists)
//        listAlbum = findViewById(R.id.lvSearchResultAlbums)
//
//        searchtest = findViewById(R.id.searchtest)
//
////        // Khởi tạo SongViewModel
//        val factory = SongViewModelFactory(SongRepository(application), UserRepository(application))
//        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)
//
////        // Lấy dữ liệu từ ViewModel và hiển thị trong ListView
////        songViewModel.tracks.observe(this, Observer { tracks ->
////            originalTracks = tracks // Lưu danh sách gốc
////            val adapter = NewReleaseAdapter(this, tracks)
////            listSong.adapter = adapter
////        })
////        songViewModel.getAllTracks()
//
////        searchtest.setOnQueryTextListener(object :
////            androidx.appcompat.widget.SearchView.OnQueryTextListener {
////            override fun onQueryTextSubmit(query: String?): Boolean {
////                return false
////            }
////
////            override fun onQueryTextChange(newText: String?): Boolean {
////                filterList(newText)
////                return true
////            }
////        })
////    }
//        // Nhận dữ liệu từ Intent
//        val searchQuery = intent.getStringExtra("searchQuery")
//        Log.d("SearchResultsActivity", "Received searchQuery: $searchQuery")
//        if (!searchQuery.isNullOrEmpty()) {
//            // Gọi phương thức trong ViewModel để tải dữ liệu từ Firebase dựa trên searchQuery
//            songViewModel.loadSearchResults(searchQuery)
//
//            // Quan sát LiveData _tracks để cập nhật ListView khi có kết quả tìm kiếm mới
//            songViewModel.tracks.observe(this, Observer { tracks ->
//                val adapter = NewReleaseAdapter(this, tracks)
//                listSong.adapter = adapter
//            })
//        }
//    }
//}

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
