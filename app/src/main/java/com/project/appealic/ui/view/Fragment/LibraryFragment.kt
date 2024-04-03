package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.appealic.R
import com.project.appealic.ui.view.Adapters.BannerAdapter

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

// Tìm kiếm các TextView tương ứng
        val tvAddArtists = view.findViewById<TextView>(R.id.tvAddArtists)
        val tvAddAlbums = view.findViewById<TextView>(R.id.tvAddAlbums)
        val tvAddSongs = view.findViewById<TextView>(R.id.tvAddPlaylists)

        // Thiết lập onClickListener cho TextViews
        tvAddSongs.setOnClickListener {
            // Thay thế fragment hiện tại bằng AddSongToPlaylistFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddPlaylistLibraryFragment())
                .addToBackStack(null)
                .commit()
        }

        tvAddArtists.setOnClickListener {
            // Replace the current fragment with AddArtistFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddArtistFragment())
                .addToBackStack(null)
                .commit()
        }

        tvAddAlbums.setOnClickListener {
            // Replace the current fragment with AddAlbumFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddAlbumFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}