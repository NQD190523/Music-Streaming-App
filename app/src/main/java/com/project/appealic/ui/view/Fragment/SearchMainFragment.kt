package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayout
import com.project.appealic.R
import com.project.appealic.data.model.Genre
import com.project.appealic.ui.view.Adapters.PlaylistAdapter
import java.util.LinkedList

class SearchMainFragment : Fragment() {
    private var searchHistory: LinkedList<String> = LinkedList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_main, container, false)
        val flexboxLayout = view.findViewById<FlexboxLayout>(R.id.flexboxLayout)

        searchHistory = arguments?.getStringArrayList("search_history")?.let { LinkedList(it) } ?: LinkedList()
        flexboxLayout.removeAllViews()
        searchHistory.forEach { query ->
            val textView = TextView(requireContext())
            textView.text = query
            textView.setTextAppearance(R.style.Body2)
            textView.setBackgroundResource(R.drawable.button_color_white)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            textView.setPadding(
                resources.getDimensionPixelSize(R.dimen.dp_16),
                resources.getDimensionPixelSize(R.dimen.dp_4),
                resources.getDimensionPixelSize(R.dimen.dp_16),
                resources.getDimensionPixelSize(R.dimen.dp_4)
            )
            val layoutParams = FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(
                resources.getDimensionPixelSize(R.dimen.dp_4), // Left margin
                resources.getDimensionPixelSize(R.dimen.dp_8), // Top margin
                resources.getDimensionPixelSize(R.dimen.dp_4), // Right margin
                resources.getDimensionPixelSize(R.dimen.dp_4) // Bottom margin
            )
            textView.layoutParams = layoutParams

            textView.setOnClickListener {
                loadQueryToSearchView(query)
            }
            flexboxLayout.addView(textView)
        }



        val gridView: GridView = view.findViewById(R.id.gridviewSearch)
        val imageList = listOf(
            R.drawable.sleeping,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok,
            R.drawable.playlistgenz,
            R.drawable.playlistlofi
        )

        val adapter = PlaylistAdapter(requireContext(), imageList, object : PlaylistAdapter.OnPlaylistClickListener {
            override fun onPlaylistClick(position: Int) {
                val genre = when (position) {
                    0 -> Genre(R.drawable.chill, "Sleeping Songs")
                    1 -> Genre(R.drawable.kpop, "K-pop")
                    2 -> Genre(R.drawable.vpop, "V-Pop")
                    3 -> Genre(R.drawable.tiktok, "TikTok Hits")
                    4 -> Genre(R.drawable.genz, "Gen Z")
                    5 -> Genre(R.drawable.lofi, "Lo-fi")
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

    private fun loadQueryToSearchView(query: String) {
        // Tìm SearchView và nạp nội dung tìm kiếm vào đó
        val searchView = requireActivity().findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        searchView.setQuery(query, false)
    }

    companion object {
        fun newInstance(searchHistory: LinkedList<String>): SearchMainFragment {
            val fragment = SearchMainFragment()
            val bundle = Bundle()
            bundle.putStringArrayList("search_history", ArrayList(searchHistory))
            fragment.arguments = bundle
            return fragment
        }
    }
}