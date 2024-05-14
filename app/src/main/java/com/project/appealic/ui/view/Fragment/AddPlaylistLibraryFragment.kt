package com.project.appealic.ui.view.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.PlaylistResultAdapter
import com.project.appealic.ui.view.Adapters.UserPlaylistAdapter
import com.project.appealic.ui.viewmodel.PlayListViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.PlayListViewModelFactory
import com.project.appealic.utils.SongViewModelFactory


class AddPlaylistLibraryFragment : Fragment()  {
    private lateinit var songViewModel: SongViewModel
    private lateinit var listViewPlaylist: ListView
    private lateinit var playListViewModel: PlayListViewModel
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userId = auth.currentUser?.uid

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_song, container, false)

        view.findViewById<FrameLayout>(R.id.flAddPlaylist).setOnClickListener {
            val addPlaylistFragment = AddPlaylistFragment()
            addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
        }

        val songFactory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, songFactory)[SongViewModel::class.java]

        val factory = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playListViewModel = ViewModelProvider(this, factory)[PlayListViewModel::class.java]

        val containerCreatePlaylist = view.findViewById<LinearLayout>(R.id.containerCreatePlaylist)
        containerCreatePlaylist.setOnClickListener{
            showCreatePlaylistDialog()
        }


        listViewPlaylist = view.findViewById(R.id.lvPlaylists)

        if (userId != null) playListViewModel.getUserPlaylist(userId)

        playListViewModel.userPlayLists.observe(viewLifecycleOwner, Observer { playlists ->
            playlists?.let { playlist ->
                val adapter = UserPlaylistAdapter(requireContext(), playlist)
                listViewPlaylist.adapter = adapter
            }
            listViewPlaylist.setOnItemClickListener { _, _, position, _ ->
                val selectedPlaylist = playlists!![position]
                // Chuyển sang PlaylistPageFragment với thông tin của playlist đã chọn
                val bundle = Bundle().apply {
                    putParcelable("user_selected_playlist", selectedPlaylist)
                    putInt("playlist_index",position)
                }
                val playlistPageFragment = PlaylistPageFragment()
                playlistPageFragment.arguments = bundle

                // Thay đổi Fragment hoặc khởi chạy Activity mới (tùy vào thiết kế của bạn)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmenthome, playlistPageFragment)
                    .addToBackStack(null)
                    .commit()
            }
            setListViewHeightBasedOnItems(listViewPlaylist)
        })

        return view
    }

    private fun showCreatePlaylistDialog() {
        val addPlaylistDialog = AddPlaylistDialog()
        addPlaylistDialog.show(parentFragmentManager, "AddPlaylistDialog")
    }

    private fun setListViewHeightBasedOnItems(listView: ListView) {
        val listAdapter = listView.adapter
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem: View = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }
}