package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
<<<<<<< Updated upstream
=======
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
>>>>>>> Stashed changes
import com.project.appealic.R
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.BannerAdapter
<<<<<<< Updated upstream
=======
import com.project.appealic.ui.view.Adapters.PlaylistCardAdapter
import com.project.appealic.ui.view.Adapters.RecentlySongAdapter
import com.project.appealic.ui.view.Fragment.AddAlbumFragment
import com.project.appealic.ui.view.Fragment.AddArtistFragment
import com.project.appealic.ui.view.Fragment.AddPlaylistLibraryFragment
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.ui.viewmodel.SongViewModelFactory
>>>>>>> Stashed changes

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val bannerImages = listOf(
            R.drawable.imagecard2,
            R.drawable.imagecard2,
            R.drawable.imagecard2
        )
        val bannerAdapter = BannerAdapter(bannerImages)

        // Initialize and configure the RecyclerView for banner
        val recyclerViewBanner = view.findViewById<RecyclerView>(R.id.rrBanner)
        recyclerViewBanner.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBanner.adapter = bannerAdapter

<<<<<<< Updated upstream
=======
        // Tìm kiếm các TextView tương ứng
        val tvAddArtists = view.findViewById<TextView>(R.id.tvAddArtists)
        val tvAddAlbums = view.findViewById<TextView>(R.id.tvAddAlbums)
        val tvAddSongs = view.findViewById<TextView>(R.id.tvAddPlaylists)

//        val underline = view.findViewById<View>(R.id.underline)

// Lưu trữ vị trí ban đầu của underline
        val initialX = 203f

// Tính toán deltaX dựa trên vị trí của từng TextView
        val deltaXSongs =  tvAddSongs.x - initialX*2
        val deltaXArtists = tvAddArtists.x - initialX*5/4
        val deltaXAlbums = tvAddAlbums.x - initialX * 1/2

//// Thiết lập onClickListener cho TextViews
//        tvAddSongs.setOnClickListener {
//            // Thay thế fragment hiện tại bằng AddSongToPlaylistFragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentAddPlaylistLibrary, AddPlaylistLibraryFragment())
//                .addToBackStack(null)
//                .commit()
//            underline.animate().translationX(deltaXSongs).setDuration(300).start()
//        }
//
//        tvAddArtists.setOnClickListener {
//            // Replace the current fragment with AddArtistFragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentAddPlaylistLibrary, AddArtistFragment())
//                .addToBackStack(null)
//                .commit()
//            underline.animate().translationX(deltaXArtists).setDuration(300).start()
//        }
//
//        tvAddAlbums.setOnClickListener {
//            // Replace the current fragment with AddAlbumFragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentAddPlaylistLibrary, AddAlbumFragment())
//                .addToBackStack(null)
//                .commit()
//            underline.animate().translationX(deltaXAlbums).setDuration(300).start()
//        }

        // Danh sách hình ảnh từ thư mục drawable
        val imageList = listOf(
            R.drawable.libraryliked_song,
            R.drawable.librarydownload,
            R.drawable.playlist1,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok,
            R.drawable.playlistgenz,
            R.drawable.playlistlofi
        )

        // Tạo adapter và thiết lập cho card_recycle
        val cardAdapter = PlaylistCardAdapter(requireContext(), imageList)
        val recycleCardPlaylist : RecyclerView  = view.findViewById(R.id.card_gird)

        recycleCardPlaylist.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        recycleCardPlaylist.adapter = cardAdapter

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.dp_2) // Khoảng cách ngang mong muốn
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.dp_2) // Khoảng cách dọc mong muốn
        val decoration = StaggeredGridSpacingItemDecoration(horizontalSpacing, verticalSpacing)
        recycleCardPlaylist.addItemDecoration(decoration)


        // Khởi tạo SongViewModel
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        var songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        // Khởi tạo và cấu hình RecyclerView
        var recyclerRecent  = view.findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerRecent.layoutManager = layoutManager
        val recentlySongAdapter = RecentlySongAdapter(requireContext(), emptyList())
        recyclerRecent.adapter = recentlySongAdapter
        songViewModel.getRecentSongs(FirebaseAuth.getInstance().currentUser?.uid.toString()).observe(viewLifecycleOwner, Observer { songs ->
            recentlySongAdapter.updateData(songs)
        })
>>>>>>> Stashed changes
        return view

    }
}