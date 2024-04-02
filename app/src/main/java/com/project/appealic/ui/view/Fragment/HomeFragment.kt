package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.ActivityPlaylist
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.BannerAdapter
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.RecentlySongAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.ui.viewmodel.SongViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var listView: ListView
    private lateinit var recyclerViewArtists: RecyclerView
    private lateinit var RecentlySongAdapter: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Khởi tạo SongViewModel
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        // Khởi tạo và cấu hình RecyclerView cho banner
        val recyclerViewBanner: RecyclerView = rootView.findViewById(R.id.rrBanner)
        recyclerViewBanner.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val bannerImages = listOf(R.drawable.imagecart, R.drawable.imagecart, R.drawable.imagecart)
        val bannerAdapter = BannerAdapter(bannerImages)
        recyclerViewBanner.adapter = bannerAdapter

        // Khởi tạo và cấu hình ListView cho danh sách các bài hát mới
        listView = rootView.findViewById(R.id.lvNewRelease)
        songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            listView.adapter = adapter
        })
//Khởi tao rectcleview cho recently sobng
        RecentlySongAdapter = rootView.findViewById(R.id.RecentlyViewSong)
        RecentlySongAdapter.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        songViewModel.artists.observe(viewLifecycleOwner, Observer { artists ->
            val artistAdapter = ArtistAdapter(requireContext(), artists)
            RecentlySongAdapter.adapter = artistAdapter
        })

        // Khởi tạo và cấu hình RecyclerView cho danh sách nghệ sĩ
        recyclerViewArtists = rootView.findViewById(R.id.recyclerViewArtist)
        recyclerViewArtists.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        songViewModel.artists.observe(viewLifecycleOwner, Observer { artists ->
            val artistAdapter = ArtistAdapter(requireContext(), artists)
            recyclerViewArtists.adapter = artistAdapter
        })

        // Lấy danh sách tracks và artists từ repository
        songViewModel.getAllTracks()
        songViewModel.getAllArtists()

        // Xác định ListView
        listView = rootView.findViewById(R.id.lvNewRelease)

        // Thiết lập OnItemClickListener cho ListView
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // Lấy dữ liệu của mục được chọn từ Adapter
                val selectedSong = parent.getItemAtPosition(position) as Track

                //lưu bài hát vừa mở vào database của thiết bị
                val user = FirebaseAuth.getInstance().currentUser?.uid
                val song =
                    selectedSong.trackId?.let { SongEntity(it,selectedSong.trackImage,selectedSong.trackTitle,selectedSong.artist,user,null,System.currentTimeMillis()) }
                if (song != null) {
                    songViewModel.insertSong(song)
                    Log.d(" test status", "success")
                }

                //lấy dữ liệu vài hát vừa nghẻ được lưu ra
                if (user != null) {
                    val info = songViewModel.getRecentSongs(user).observe(viewLifecycleOwner,Observer{ song ->
                        Log.d("info", song.toString())
                    })
                }

                // Tạo Intent để chuyển sang ActivityPlaylist
                val intent = Intent(requireContext(), ActivityPlaylist::class.java)

                // Truyền dữ liệu cần thiết qua Intent
                intent.putExtra("SONG_TITLE", selectedSong.trackTitle)
                intent.putExtra("SINGER_NAME", selectedSong.artist)
                intent.putExtra("SONG_NAME", selectedSong.trackTitle)
                intent.putExtra("track_image", selectedSong.trackImage)
                intent.putExtra("DURATION", selectedSong.duration) // Truyền thời lượng bài hát

                // Bắt đầu ActivityPlaylist
                startActivity(intent)
            }

        return rootView
    }
}
