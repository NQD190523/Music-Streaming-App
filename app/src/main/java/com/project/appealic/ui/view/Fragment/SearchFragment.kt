package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.project.appealic.R
import com.project.appealic.databinding.FragmentSearchResultBinding
import com.project.appealic.ui.view.ActivityHome
import com.project.appealic.ui.view.Adapters.PlaylistAdapter
import java.util.LinkedList

class SearchFragment : Fragment() {

    private val searchHistory = LinkedList<String>()

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
                .replace(R.id.fragmentMain, SearchMainFragment.newInstance(searchHistory))
                .commit()
        }

        // Thêm sự kiện cho SearchView
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (searchHistory.contains(query)) {
                        showDuplicateSearchQueryMessage(query)
                    } else {
                        if (searchHistory.size >= 5) {
                            searchHistory.removeFirst()
                        }
                        searchHistory.addLast(query)
                        updateSearchMainFragment()

                        // Khi người dùng nhấn nút tìm kiếm, hiển thị FragmentSearch
                        val bundle = Bundle()
                        bundle.putString("search_query", query)
                        childFragmentManager.beginTransaction()
                            .replace(R.id.fragmentMain, SearchResultFragment().apply {
                                arguments = bundle
                            })
                            .addToBackStack(null)
                            .commit()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchView.setOnClickListener {

        }


        val txtCancel = view.findViewById<TextView>(R.id.txtCancel)
        txtCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun showDuplicateSearchQueryMessage(query: String) {
        val message = "Bạn đã tìm kiếm '$query' trước đó."
//        show toast
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()

    }


    private fun updateSearchMainFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentMain, SearchMainFragment.newInstance(searchHistory))
            .addToBackStack(null)
            .commit()
    }
}