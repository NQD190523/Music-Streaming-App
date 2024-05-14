package com.project.appealic.ui.view.Fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.ui.view.Adapters.UserPlaylistAdapter
import com.project.appealic.ui.viewmodel.PlayListViewModel
import com.project.appealic.utils.PlayListViewModelFactory
import java.io.ByteArrayOutputStream

class AddPlaylistFragment() : DialogFragment() {
    private lateinit var lvUserPlaylist: ListView
    private lateinit var playListViewModel: PlayListViewModel
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userId = auth.currentUser?.uid

    companion object {
        fun newInstance(track: Track): AddPlaylistFragment {
            val fragment = AddPlaylistFragment()
            val args = Bundle().apply {
                putParcelable("TRACK", track)
            }
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_playlist, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())

        val factory = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playListViewModel = ViewModelProvider(this, factory)[PlayListViewModel::class.java]

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.background)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_add_playlist, null)
        dialog.setContentView(view)
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.more_background)
        val layoutParams = window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM or Gravity.START or Gravity.END
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = layoutParams

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val llCCreatePlaylist = view.findViewById<LinearLayout>(R.id.llCreatePlaylist)
        llCCreatePlaylist.setOnClickListener {
            dismiss()
            showCreatePlaylistDialog()
        }
        lvUserPlaylist = view.findViewById(R.id.lvUserPlaylist)
        if (userId != null) playListViewModel.getUserPlaylist(userId)
        val track = arguments?.getParcelable<Track>("TRACK")
        playListViewModel.userPlayLists.observe(viewLifecycleOwner, Observer { playlists ->
            playlists?.let { playlist ->
                println(playlist)
                val adapter = UserPlaylistAdapter(requireContext(), playlist)
                lvUserPlaylist.adapter = adapter
            }
//            lvUserPlaylist.setOnItemClickListener { _, _, position, _ ->
//                val selectedPlaylist = playlists!![position]
//                // Chuyển sang PlaylistPageFragment với thông tin của playlist đã chọn
//                val bundle = Bundle().apply {
//                    putParcelable("user_selected_playlist", selectedPlaylist)
//                }
//                val playlistPageFragment = PlaylistPageFragment()
//                playlistPageFragment.arguments = bundle
//
//                // Thay đổi Fragment hoặc khởi chạy Activity mới (tùy vào thiết kế của bạn)
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragmenthome, playlistPageFragment)
//                    .addToBackStack(null)
//                    .commit()
//            }
//            setListViewHeightBasedOnItems(lvUserPlaylist)
            lvUserPlaylist.setOnItemClickListener { _, _, position,_ ->
                val playlist = playlists!![position]
                val trackIds = mutableListOf<String>()
                val selectedTrackIdsInPlaylist = playlist.trackIds
                arguments?.getString("TRACK_ID")?.let { trackIds.add(it) }
                println(selectedTrackIdsInPlaylist)
                println(selectedTrackIdsInPlaylist.isNotEmpty())
                if (selectedTrackIdsInPlaylist.isNotEmpty()) {
                    selectedTrackIdsInPlaylist.filterNotNull().filter { it.isNotEmpty() }.forEach {
                        trackIds.add(it)
                    }
                }
                // Loại bỏ các phần tử trùng lặp
                val uniqueTrackIds = trackIds.distinct()
                val newTrack = PlayListEntity(playlist.playlistId, playlist.userId,playlist.playListName,playlist.playlistThumb,uniqueTrackIds)
                playListViewModel.addTrackToPlaylist(newTrack)
            }
        })
    }

    private fun showCreatePlaylistDialog() {
        val addPlaylistDialog = AddPlaylistDialog()
        val bundle = Bundle()
        bundle.putString("TRACK_ID",arguments?.getString("TRACK_ID"))
        addPlaylistDialog.arguments = bundle
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

    fun drawableToByteArray(resources: Resources, drawableResId: Int): ByteArray {
        val bitmap = BitmapFactory.decodeResource(resources, drawableResId)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}