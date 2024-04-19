package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
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
import com.project.appealic.ui.view.ActivityHome
import com.project.appealic.ui.view.ActivityMusicControl
import com.project.appealic.ui.view.ActivityNotification
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.BannerAdapter
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.PlaylistForYouAdapter
import com.project.appealic.ui.view.Adapters.RecentlySongAdapter
import com.project.appealic.ui.view.setOnItemClickListener
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var listView: ListView
    private lateinit var musicPlayerViewModel: MusicPlayerViewModel
    private lateinit var recyclerViewArtists: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize SongViewModel
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory)[SongViewModel::class.java]

        // Khởi tạo và cấu hình RecyclerView cho banner
        val recyclerViewBanner: RecyclerView = rootView.findViewById(R.id.rrBanner)
        recyclerViewBanner.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val bannerImages = listOf(R.drawable.imagecart, R.drawable.imagecart, R.drawable.imagecart)
        val bannerAdapter = BannerAdapter(bannerImages)
        recyclerViewBanner.adapter = bannerAdapter

        // Khởi tạo và cấu hình ListView cho danh sách các bài hát mới
        listView = rootView.findViewById(R.id.lvNewRelease)
        songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            listView.setOnItemClickListener(requireContext(),songViewModel,tracks)
            val adapter = NewReleaseAdapter(requireContext(), tracks)

            adapter.setOnAddPlaylistClickListener { track ->
                // Mở dialog thêm playlist
                val addPlaylistFragment = AddPlaylistFragment.newInstance(track)
                addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
            }

            adapter.setOnMoreActionClickListener {track ->
                val moreActionFragment = MoreActionFragment.newInstance(track)
                val bundle = Bundle()
                bundle.putString("SONG_TITLE", track.trackTitle)
                bundle.putString("SINGER_NAME", track.artist)
                bundle.putString("TRACK_IMAGE", track.trackImage)
                bundle.putString("ARTIST_ID", track.artistId)
                bundle.putString("TRACK_ID", track.trackId)
                moreActionFragment.arguments = bundle
                moreActionFragment.show(childFragmentManager, "MoreActionsFragment")
            }
            listView.adapter = adapter
            setListViewHeightBasedOnItems(listView)
        })

// Khởi tạo RecyclerView cho danh sách các bài hát đã xem gần đây
        val recentlyViewSong: RecyclerView = rootView.findViewById(R.id.RecentlyViewSong)
        recentlyViewSong.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recentlySongAdapter = RecentlySongAdapter(requireContext(), emptyList())
        recentlyViewSong.adapter = recentlySongAdapter

        songViewModel.getRecentSongs(FirebaseAuth.getInstance().currentUser?.uid.toString()).observe(viewLifecycleOwner, Observer { songs ->
            recentlySongAdapter.updateData(songs)
            println(songs)
            recentlyViewSong.post {
                recentlyViewSong.setOnItemClickListener(requireContext(), songViewModel, songs)
            }
        })


// Khởi tạo và cấu hình RecyclerView cho danh sách nghệ sĩ
        val recyclerViewArtists: RecyclerView = rootView.findViewById(R.id.recyclerViewArtist)
        recyclerViewArtists.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        songViewModel.artists.observe(viewLifecycleOwner, Observer { artists ->
            val artistAdapter = ArtistAdapter(requireContext(), artists)
            recyclerViewArtists.adapter = artistAdapter
        })

// Khởi tạo RecyclerView cho danh sách các playlist cho người dùng
        val playlistForU: RecyclerView = rootView.findViewById(R.id.PlaylistForYou)
        playlistForU.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val playlistForUAdapter = PlaylistForYouAdapter(requireContext(), emptyList())
        playlistForU.adapter = playlistForUAdapter

        songViewModel.playlists.observe(viewLifecycleOwner, Observer { playlists ->
            playlistForUAdapter.updateData(playlists)
        })

// Xử lý sự kiện click trên item của RecyclerView
        playlistForUAdapter.onItemClickListener = { selectedPlaylist ->
            // Chuyển sang PlaylistPageFragment với thông tin của playlist đã chọn
            val bundle = Bundle().apply {
                putParcelable("selected_playlist", selectedPlaylist)
            }
            val playlistPageFragment = PlaylistPageFragment()
            playlistPageFragment.arguments = bundle

            // Thay đổi Fragment hoặc khởi chạy Activity mới (tùy vào thiết kế của bạn)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmenthome, playlistPageFragment)
                .addToBackStack(null)
                .commit()
        }
// Lấy danh sách tracks và artists và playlist từ repository

        songViewModel.getAllTracks()
        songViewModel.getAllArtists()
        songViewModel.getAllPlaylists()


        val imageView: ImageView = rootView.findViewById(R.id.imvSearch);
        imageView.setOnClickListener{
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragmentSearch = SearchFragment()
            fragmentTransaction.replace(R.id.fragmenthome, fragmentSearch)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            (activity as ActivityHome).bottomNavigationView.selectedItemId = R.id.navigation_search
        }


        // Notification intent
        rootView.findViewById<ImageView>(R.id.imvAlert).setOnClickListener {
            val intent = Intent(requireContext(), ActivityNotification::class.java)
            startActivity(intent)
        }


        return rootView
    }
    private fun NewReleaseAdapter.setOnAddPlaylistClickListener(listener: (Track) -> Unit) {
        this.onAddPlaylistClick = listener
    }

    private fun NewReleaseAdapter.setOnMoreActionClickListener(listener: (Track) -> Unit) {
        this.moreActionClickListener = listener
    }

    private fun setListViewHeightBasedOnItems(listView: ListView) {
        val listAdapter = listView.adapter
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem: View = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }

}
