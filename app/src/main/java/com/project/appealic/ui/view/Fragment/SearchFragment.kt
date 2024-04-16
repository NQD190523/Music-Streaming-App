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
import android.widget.TextView
import androidx.appcompat.widget.SearchView
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

        // Đảm bảo FragmentMain được thêm vào layout
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentMain, SearchMainFragment())
                .commit()
        }

        // Thêm sự kiện cho SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Khi người dùng nhấn nút tìm kiếm, hiển thị FragmentSearch
                val bundle = Bundle()
                bundle.putString("search_query", query)
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentMain, SearchResultFragment().apply {
                        arguments = bundle
                    })
                    .commit()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Nếu cần, bạn có thể thực hiện các thao tác khi người dùng thay đổi nội dung tìm kiếm
                return false
            }
        })

        // Thêm sự kiện cho TextView "Cancel"
        val txtCancel = view.findViewById<TextView>(R.id.txtCancel)
        txtCancel.setOnClickListener {
            // Khi người dùng nhấn "Cancel", hiển thị FragmentMain lại
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentMain, SearchMainFragment())
                .commit()
        }

        return view
    }
}
//
//        // Truy xuất đến  search
//        val searchView: SearchView = view.findViewById(R.id.searchView)

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                // Người dùng nhấn nút tìm kiếm
//                if (query != null) {
//                    // Chuyển hướng đến Activity hoặc Fragment chứa trang kết quả tìm kiếm
//                    val intent = Intent(requireActivity(), SearchResultsActivity::class.java)
//                    intent.putExtra("searchQuery", query)
//                    Log.d("SearchResultsActivity", "Search query: $query")
//                    startActivity(intent)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // Người dùng đang gõ vào SearchView
//                return false
//            }
//        })
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // Khởi tạo Intent để mở SearchResultsActivity
//                val intent = Intent(requireActivity(), SearchResultsActivity::class.java)
//                // Truyền dữ liệu từ SearchView qua Intent
//                intent.putExtra("searchQuery", query)
//                startActivity(intent)
//                Log.d("SearchResultsActivity", "Search query: $query")
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//
//        return view
//    }
//}
