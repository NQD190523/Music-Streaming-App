package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.databinding.FragmentSearchResultBinding
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

        // Truy xuất đến editview search
        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                // Người dùng nhấn nút tìm kiếm
                if (query != null) {
                    // Chuyển hướng đến Activity hoặc Fragment chứa trang kết quả tìm kiếm
                    val intent = Intent(requireActivity(), SearchResultsActivity::class.java)
                    intent.putExtra("searchQuery", query)
                    Log.d("SearchResultsActivity", "Search query: $query")
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Người dùng đang gõ vào SearchView
                return false
            }
        })

        view.findViewById<TextView>(R.id.txtCancel).setOnClickListener {
            (activity as ActivityHome).onSearchCancel()
        }

        return view
    }
}
