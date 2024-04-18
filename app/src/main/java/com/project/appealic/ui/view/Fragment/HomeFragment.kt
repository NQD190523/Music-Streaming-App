package com.project.appealic.ui.view.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
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
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var listView: ListView
    private lateinit var recyclerViewArtists: RecyclerView

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
        })

// Khởi tạo RecyclerView cho danh sách các bài hát đã xem gần đây
        val recentlyViewSong: RecyclerView = rootView.findViewById(R.id.RecentlyViewSong)
        recentlyViewSong.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val recentlySongAdapter = RecentlySongAdapter(requireContext(), emptyList())
        recentlyViewSong.adapter = recentlySongAdapter
        songViewModel.getRecentSongs(FirebaseAuth.getInstance().currentUser?.uid.toString()).observe(viewLifecycleOwner, Observer { songs ->
            recentlySongAdapter.updateData(songs)
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

            // Cập nhật danh sách playlist trong adapter
            playlistForUAdapter.updateData(playlists)
        })

// Lấy danh sách tracks và artists và playlist từ repository
        songViewModel.getAllTracks()
        songViewModel.getAllArtists()
        songViewModel.getAllPlaylists()


        // Xác định ListView
        listView = rootView.findViewById(R.id.lvNewRelease)

        // Thiết lập OnItemClickListener cho ListView
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // Lấy dữ liệu của mục được chọn từ Adapter
                val selectedSong = parent.getItemAtPosition(position) as Track
                //lưu bài hát vừa mở vào database của thiết bị
                val user = FirebaseAuth.getInstance().currentUser?.uid
                val intent = Intent(requireContext(), ActivityMusicControl::class.java)
                val trackUrlList = ArrayList<String>()

                val song = selectedSong.trackId?.let {
                    SongEntity(
                        it,
                        selectedSong.trackImage,
                        selectedSong.trackTitle,
                        selectedSong.artist,
                        user,
                        null,
                        System.currentTimeMillis(),
                        null
                    )
                }

                if (song != null) {
                    songViewModel.insertSong(song)
                    Log.d(" test status", "success")
                }
                //lấy dữ liệu vài hát vừa nghẻ được lưu ra
                if (user != null) {
                    val info = songViewModel.getRecentSongs(user)
                        .observe(viewLifecycleOwner, Observer { song ->
                            Log.d("info", song.toString())
                        })
                }
//              Lấy dữ liệu các url trogn playlist
                for (i in 0 until parent.count){
                    val item = parent.getItemAtPosition(i) as Track
                    item.trackUrl?.let { trackUrl ->
                        trackUrlList.add(trackUrl)
                    }
                }
                // Truyền dữ liệu cần thiết qua Intent
                intent.putExtra("SONG_TITLE", selectedSong.trackTitle)
                intent.putExtra("SINGER_NAME", selectedSong.artist)
                intent.putExtra("SONG_NAME", selectedSong.trackTitle)
                intent.putExtra("TRACK_IMAGE", selectedSong.trackImage)
                intent.putExtra("ARTIST_ID", selectedSong.artistId)
                intent.putExtra("DURATION", selectedSong.duration)
                intent.putExtra("TRACK_URL", selectedSong.trackUrl)
                intent.putExtra("TRACK_ID", selectedSong.trackId)
                intent.putExtra("TRACK_INDEX",position)
                intent.putStringArrayListExtra("TRACK_LIST",trackUrlList)
                startActivity(intent)
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

}
