package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.ActivityHome
import com.project.appealic.ui.view.ActivityNotification
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.BannerAdapter
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.PlaylistForYouAdapter
import com.project.appealic.ui.view.Adapters.RecentlySongAdapter
import com.project.appealic.ui.view.setOnItemClickListener
import com.project.appealic.ui.viewmodel.ArtistViewModel
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.ArtistViewModelFactory
import com.project.appealic.utils.SongViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var listView: ListView
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
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

        // Initialize ArtistViewModel
        val factoryArtist = ArtistViewModelFactory(ArtistRepository(requireActivity().application))
        artistViewModel = ViewModelProvider(this, factoryArtist)[ArtistViewModel::class.java]

        // Khởi tạo và cấu hình RecyclerView cho banner
        val recyclerViewBanner: RecyclerView = rootView.findViewById(R.id.rrBanner)
        recyclerViewBanner.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val bannerImages = listOf(R.drawable.imagecart, R.drawable.imagecart2, R.drawable.imagecart3,R.drawable.imagecart4, R.drawable.imagecart5,R.drawable.imagecart6)
        val bannerAdapter = BannerAdapter(bannerImages)
        recyclerViewBanner.adapter = bannerAdapter

        // Lấy danh sách tracks và artists và playlist từ repository

        songViewModel.getAllTracks()
        songViewModel.getAllArtists()
        songViewModel.getAllPlaylists()

        // Khởi tạo và cấu hình ListView cho danh sách các bài hát mới
        listView = rootView.findViewById(R.id.lvNewRelease)
// Khởi tạo một biến boolean để theo dõi xem người dùng đã bấm vào nút "View All" chưa
        var isViewAllClicked = false

// Lấy dữ liệu ban đầu khi Fragment Home được tạo ra
        songViewModel.getNewReleaseTracks()

// Thêm một Observer để quan sát thay đổi trong dữ liệu bài hát ban đầu
        songViewModel.newReleaseTracks.observe(viewLifecycleOwner, Observer { tracks ->
            if (!isViewAllClicked) {
                val adapter = NewReleaseAdapter(requireContext(), tracks)
                listView.adapter = adapter
                setListViewHeightBasedOnItems(listView)
                listView.setOnItemClickListener(requireContext(),songViewModel,tracks)

                adapter.setOnAddPlaylistClickListener { track ->
                    // Mở dialog thêm playlist
                    val addPlaylistFragment = AddPlaylistFragment.newInstance(track)
                    addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
                }

                adapter.setOnMoreActionClickListener { track ->
                    track.trackUrl?.let { songViewModel.getTrackByUrl(it) }

                    val moreActionFragment = MoreActionFragment.newInstance(track)
                    val bundle = Bundle()
                    bundle.putString("SONG_TITLE", track.trackTitle)
                    bundle.putString("SINGER_NAME", track.artist)
                    bundle.putString("TRACK_IMAGE", track.trackImage)
                    bundle.putString("ARTIST_ID", track.artistId)
                    bundle.putString("TRACK_ID", track.trackId)
                    bundle.putString("TRACK_URL", track.trackUrl)
                    moreActionFragment.arguments = bundle
                    moreActionFragment.show(childFragmentManager, "MoreActionsFragment")
                }
            }
        })

        fun setButtonBackground(button: Button, backgroundResource: Int) {
            button.setBackgroundResource(backgroundResource)
        }
// Xử lý sự kiện khi người dùng bấm vào nút "View All"
        val btnViewAll: Button = rootView.findViewById(R.id.btnViewAll)
        val btnVietnam: Button = rootView.findViewById(R.id.btnVietnam)
        val btnInternational: Button = rootView.findViewById(R.id.btnInternational)

        btnViewAll.setOnClickListener {
            // Đặt cờ là true để chỉ định rằng người dùng đã bấm vào nút "View All"
            isViewAllClicked = false
            // Lấy tất cả các bài hát
            songViewModel.getNewReleaseTracks()
            // Set background cho các nút
            setButtonBackground(btnViewAll, R.drawable.rounded_button)
            setButtonBackground(btnVietnam, R.drawable.button_color_white)
            setButtonBackground(btnInternational, R.drawable.button_color_white)


        }

// Thêm một sự kiện click listener cho nút View All

        btnVietnam.setOnClickListener {
            // Lọc bài hát theo thể loại "V-Pop"
            songViewModel.getTrackFromGenres("V-Pop")
            setButtonBackground(btnViewAll, R.drawable.button_color_white)
            setButtonBackground(btnVietnam, R.drawable.rounded_button)
            setButtonBackground(btnInternational, R.drawable.button_color_white)
        }

        btnInternational.setOnClickListener {
            // Lọc bài hát theo thể loại "K-Pop"
            songViewModel.getTrackFromGenres("K-pop")
            setButtonBackground(btnViewAll, R.drawable.button_color_white)
            setButtonBackground(btnVietnam, R.drawable.button_color_white)
            setButtonBackground(btnInternational, R.drawable.rounded_button)
        }

// Thêm một Observer để quan sát thay đổi trong dữ liệu bài hát đã lọc
        songViewModel.gerneTracks.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            listView.adapter = adapter
            setListViewHeightBasedOnItems(listView)
            listView.setOnItemClickListener(requireContext(),songViewModel,tracks)
            adapter.setOnAddPlaylistClickListener { track ->
                // Mở dialog thêm playlist
                val addPlaylistFragment = AddPlaylistFragment.newInstance(track)
                addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
            }

            adapter.setOnMoreActionClickListener {track ->
                track.trackUrl?.let { songViewModel.getTrackByUrl(it) }
                val moreActionFragment = MoreActionFragment.newInstance(track)
                val bundle = Bundle()
                bundle.putString("SONG_TITLE", track.trackTitle)
                bundle.putString("SINGER_NAME", track.artist)
                bundle.putString("TRACK_IMAGE", track.trackImage)
                bundle.putString("ARTIST_ID", track.artistId)
                bundle.putString("TRACK_ID", track.trackId)
                bundle.putString("TRACK_URL",track.trackUrl)
                moreActionFragment.arguments = bundle
                moreActionFragment.show(childFragmentManager, "MoreActionsFragment")
            }
            listView.adapter = adapter
            setListViewHeightBasedOnItems(listView)
        })


// Khởi tạo RecyclerView cho danh sách các bài hát đã xem gần đây
        val recentSongTitle = rootView.findViewById<TextView>(R.id.txtRecentlyPlayed)
        val recentlyViewSong: RecyclerView = rootView.findViewById(R.id.RecentlyViewSong)
        recentlyViewSong.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recentlySongAdapter = RecentlySongAdapter(requireContext(), emptyList())
        recentlyViewSong.adapter = recentlySongAdapter

        songViewModel.getRecentSongs(FirebaseAuth.getInstance().currentUser?.uid.toString()).observe(viewLifecycleOwner, Observer { songs ->

            if (songs.isEmpty()) {

                recentlyViewSong.visibility = View.GONE
                recentSongTitle.visibility = View.GONE

            } else {

                recentlyViewSong.visibility = View.VISIBLE
                recentSongTitle.visibility = View.VISIBLE

                recentlySongAdapter.updateData(songs)
                println(songs)
                recentlyViewSong.post {
                    recentlyViewSong.setOnItemClickListener(requireContext(), songViewModel, songs)
                }
            }
        })


// Khởi tạo và cấu hình RecyclerView cho danh sách nghệ sĩ
        val favArtistsTitle = rootView.findViewById<TextView>(R.id.txtFavArtists)
        val recyclerViewArtists: RecyclerView = rootView.findViewById(R.id.recyclerViewArtist)
        recyclerViewArtists.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Quan sát LiveData để nhận danh sách nghệ sĩ mà người dùng đã follow
        artistViewModel.likedArtist.observe(viewLifecycleOwner, Observer { likedArtists ->

            if (likedArtists.isEmpty()) {

                recyclerViewArtists.visibility = View.GONE
                favArtistsTitle.visibility = View.GONE

            } else {

                recyclerViewArtists.visibility = View.VISIBLE
                favArtistsTitle.visibility = View.VISIBLE

                // Tạo một Adapter mới với danh sách nghệ sĩ mà người dùng đã follow
                val artistAdapter = ArtistAdapter(requireContext(), likedArtists, requireActivity().supportFragmentManager)
                // Đặt Adapter cho RecyclerView
                recyclerViewArtists.adapter = artistAdapter
            }
        })

        // Gọi hàm để lấy danh sách các nghệ sĩ đã follow
        artistViewModel.getFollowArtistFromUser(userId)

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
