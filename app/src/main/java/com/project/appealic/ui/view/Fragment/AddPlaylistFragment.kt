package com.project.appealic.ui.view.Fragment

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.ui.viewmodel.PlayListViewModel
import com.project.appealic.utils.PlayListViewModelFactory
import kotlin.random.Random

class AddPlaylistFragment : DialogFragment() {

    private lateinit var playListViewModel: PlayListViewModel
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_playlist, container, false)

        val factory = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playListViewModel = ViewModelProvider(this, factory ).get(PlayListViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())

        // Khai báo dialog không có tiêu đề
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Gán background cho dialog
        dialog.window?.setBackgroundDrawableResource(R.drawable.background)

        // Tạo và gán layout cho dialog
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_add_playlist, null)
        dialog.setContentView(view)

        // Tùy chỉnh cài đặt của Window
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
    }

    private fun showCreatePlaylistDialog() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_create_playlist)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.radius_background)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)

        val edtPlaylistName = dialog.findViewById<EditText>(R.id.edtPlaylistName)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            val image = getRandomImageDrawable(this)
            // Chức năng tạo playlist mới
            val userId = auth.currentUser!!.uid
            playListViewModel.createNewPlayList(PlayListEntity("", userId,edtPlaylistName.text.toString(),null,null))

        }

        dialog.show()
    }
    fun getRandomImageDrawable(fragment: AddPlaylistFragment): Drawable? {
        // Danh sách tên của các hình ảnh trong thư mục drawable
        val imageNames = listOf("song_1", "song_2", "song_3", "song_4")

        // Chọn ngẫu nhiên một tên hình ảnh từ danh sách
        val randomImageName = imageNames.random()

        // Lấy ID của hình ảnh từ tên hình ảnh
        val resourceId = fragment.requireContext().resources.getIdentifier(randomImageName, "drawable", fragment.requireContext().packageName)

        // Lấy Drawable từ ID
        return fragment.requireContext().getDrawable(resourceId)
    }
}


