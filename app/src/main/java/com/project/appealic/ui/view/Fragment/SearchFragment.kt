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
import androidx.fragment.app.FragmentManager
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
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentMain, SearchMainFragment.newInstance(searchHistory))
                .commit()
        }

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (searchHistory.size >= 5) {
                        searchHistory.removeFirst()
                    }
                    searchHistory.addLast(query)
                    updateSearchMainFragment()

                    val bundle = Bundle().apply {
                        putString("search_query", query)
                    }
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentMain, SearchResultFragment().apply { arguments = bundle })
                        .addToBackStack(null)
                        .commit()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val txtCancel = view.findViewById<TextView>(R.id.txtCancel)
        txtCancel.setOnClickListener {
            childFragmentManager.popBackStack()
        }

        return view
    }

    private fun updateSearchMainFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentMain, SearchMainFragment.newInstance(searchHistory))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newInstance(searchHistory: LinkedList<String>): SearchFragment {
            val fragment = SearchFragment()
            val bundle = Bundle()
            bundle.putStringArrayList("search_history", ArrayList(searchHistory))
            fragment.arguments = bundle
            return fragment
        }
    }
}