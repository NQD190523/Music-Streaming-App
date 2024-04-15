package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class SearchResultsActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var songViewModel: SongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search_result)

        listView = findViewById(R.id.lvSearchResultSongs)

//        // Khởi tạo SongViewModel
//        val factory = SongViewModelFactory(SongRepository(application), UserRepository(application))
//        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)
//
//        // Lấy dữ liệu từ ViewModel và hiển thị trong ListView
//        songViewModel.tracks.observe(this, Observer { tracks ->
//            val adapter = NewReleaseAdapter(this, tracks)
//            listView.adapter = adapter
//        })
//        songViewModel.getAllTracks()
//        songViewModel.getAllArtists()


        // Khởi tạo SongViewModel
        val factory = SongViewModelFactory(SongRepository(application), UserRepository(application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        // Lấy từ khóa tìm kiếm từ Intent
        val searchQuery = intent.getStringExtra("searchQuery")
        Log.d("SearchResultsActivity", "Search query new: $searchQuery")

        // Gọi phương thức để tải dữ liệu từ Firebase dựa trên từ khóa tìm kiếm
        loadSearchResults(searchQuery)
    }

    private fun loadSearchResults(searchQuery: String?) {
        // Gọi phương thức trong ViewModel để tải dữ liệu từ Firebase
        songViewModel.loadSearchResults(searchQuery)

        // Quan sát LiveData _tracks để cập nhật ListView khi có kết quả tìm kiếm mới
        songViewModel.tracks.observe(this, Observer { tracks ->
            val adapter = NewReleaseAdapter(this, tracks)
            listView.adapter = adapter
        })
    }
}
