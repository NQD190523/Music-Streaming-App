package com.project.appealic.ui.view.Fragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.databinding.DialogCreatePlaylistBinding
import com.project.appealic.ui.viewmodel.PlayListViewModel
import com.project.appealic.utils.PlayListViewModelFactory

class AddPlaylistDialog : DialogFragment() {
    private lateinit var binding: DialogCreatePlaylistBinding
    private lateinit var playListViewModel: PlayListViewModel
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userId = auth.currentUser?.uid
//    val trackId = arguments?.getString("TRACK_ID")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playListViewModel = ViewModelProvider(this, factory).get(PlayListViewModel::class.java)

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            if (userId != null) {
                // Tạo một danh sách mới từ trackIds hiện tại và thêm trackId vào danh sách này
                val trackIds = mutableListOf<String>()
                arguments?.getString("TRACK_ID")?.let { trackId ->
                    trackIds.add(trackId)
                }

                val newPlaylist =
                    PlayListEntity(
                        null,
                        userId,
                        binding.edtPlaylistName.text.toString(),
                        R.drawable.song2,
                        trackIds)

                playListViewModel.createNewPlayList(newPlaylist)
                playListViewModel.getUserPlaylist(userId)

                println(trackIds)
                println(newPlaylist)
                
                dialog?.dismiss()

            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_create_playlist)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(R.drawable.radius_background)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)

        return dialog
    }
}
