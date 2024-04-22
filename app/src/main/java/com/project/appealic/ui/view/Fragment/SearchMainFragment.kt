package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.data.model.Genre
import com.project.appealic.ui.view.Adapters.PlaylistAdapter

class SearchMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_main, container, false)

        val gridView: GridView = view.findViewById(R.id.gridviewSearch)
        val imageList = listOf(
            R.drawable.sleeping,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok
        )

        val adapter = PlaylistAdapter(requireContext(), imageList, object : PlaylistAdapter.OnPlaylistClickListener {
            override fun onPlaylistClick(position: Int) {
                val genre = when (position) {
                    0 -> Genre(R.drawable.chill, "Sleeping Songs")
                    1 -> Genre(R.drawable.kpop, "K-pop")
                    2 -> Genre(R.drawable.vpop, "V-Pop")
                    3 -> Genre(R.drawable.tiktok, "TikTok Hits")
                    else -> Genre(0, "Unknown")
                }

                val bundle = Bundle().apply {
                    putParcelable("selected_genre", genre)
                }
                val genresFragment = GenresFragment()
                genresFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmenthome, genresFragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        gridView.adapter = adapter

        return view
    }
}