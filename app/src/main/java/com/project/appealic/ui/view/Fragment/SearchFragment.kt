package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.ui.view.ActivityHome
import com.project.appealic.ui.view.Adapters.PlaylistAdapter

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Tìm kiếm GridView trong layout của fragment
        val gridView: GridView = view.findViewById(R.id.gridviewSearch)

        // Danh sách hình ảnh từ thư mục drawable
        val imageList = listOf(
            R.drawable.playlist1,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok
        )

        // Tạo adapter và thiết lập cho GridView
        val adapter = PlaylistAdapter(requireContext(), imageList)
        gridView.adapter = adapter

        view.findViewById<TextView>(R.id.txtCancel).setOnClickListener {
            (activity as ActivityHome).onSearchCancel()
        }

        return view
    }
}