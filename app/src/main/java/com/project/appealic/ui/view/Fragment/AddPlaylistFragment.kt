package com.project.appealic.ui.view.Fragment

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
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
import android.widget.ListView
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.UserPlaylist
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.ui.view.Adapters.UserPlaylistAdapter
import com.project.appealic.ui.viewmodel.PlayListViewModel
import com.project.appealic.utils.PlayListViewModelFactory
import java.io.ByteArrayOutputStream

class AddPlaylistFragment : DialogFragment() {

    private lateinit var playListViewModel: PlayListViewModel
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var  userPLayList : List<PlayListEntity>

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
        playListViewModel = ViewModelProvider(this, factory).get(PlayListViewModel::class.java)
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


        val userPlaylists = listOf(
            UserPlaylist(R.drawable.song1, "Jienne", "Playlist 1"),
            UserPlaylist(R.drawable.song1, "Jienne", "Playlist 2")
        )

        val lvUserPlaylist = view.findViewById<ListView>(R.id.lvUserPlaylist)
        val adapter = UserPlaylistAdapter(requireContext(), userPlaylists)
        lvUserPlaylist.adapter = adapter

    }

    private fun showCreatePlaylistDialog() {

        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_create_playlist)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(R.drawable.radius_background)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)

        val edtPlaylistName = dialog.findViewById<EditText>(R.id.edtPlaylistName)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
//            val image = getRandomImageDrawable(requireContext())
//            val imageBytes = drawableToByteArray(requireContext().resources,R.drawable.song1)
            // Chức năng tạo playlist mới
            val userId = auth.currentUser?.uid
            userId?.let { it1 ->
                PlayListEntity(
                    "",
                    it1, edtPlaylistName.text.toString(), null, null
                )
            }?.let { it2 -> playListViewModel.createNewPlayList(it2) }
        }
        dialog.show()
    }

    //    fun getRandomImageDrawable(context: Context?): Drawable? {
//        context ?: return null
//        // Danh sách tên của các hình ảnh trong thư mục drawable
//        val imageNames = listOf("song_1", "song_2", "song_3", "song_4")
//
//        // Chọn ngẫu nhiên một tên hình ảnh từ danh sách
//        val randomImageName = imageNames.random()
//
//        // Lấy ID của hình ảnh từ tên hình ảnh
//        val resourceId = context.resources.getIdentifier(randomImageName, "drawable", context.packageName)
//
//        // Lấy Drawable từ ID
//        return context.getDrawable(resourceId)
//    }
    fun drawableToByteArray(resources: Resources, drawableResId: Int): ByteArray {
        val bitmap = BitmapFactory.decodeResource(resources, drawableResId)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}


