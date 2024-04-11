package com.project.appealic.ui.view.Fragment

import SongAdapter
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Track

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoreActionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreActionFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View? {
        return inflater.inflate(R.layout.fragment_more_action, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(requireActivity())
        // Tạo và gán layout cho dialog
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_more_action, null)
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


        // Lấy dữ liệu từ Intent và hiển thị trên giao diện playlist
        val songTitle = arguments?.getString("SONG_TITLE")
        val artistName = arguments?.getString("SINGER_NAME")
        val trackImage = arguments?.getString("TRACK_IMAGE")
        val txtSongName = view.findViewById<TextView>(R.id.txtSongName)
        txtSongName.text = songTitle
        val txtSinger = view.findViewById<TextView>(R.id.txtSinger)
        txtSinger.text = artistName
        val songImageView = view.findViewById<ImageView>(R.id.imvPhoto)
        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

            Glide.with(this)
                .load(storageReference)
                .into(songImageView)
        }

        // Xử lý các sự kiện click
        view.findViewById<LinearLayout>(R.id.llAddPlay).setOnClickListener {
            dismiss()
            showDialogForAddPlay()
        }

        view.findViewById<LinearLayout>(R.id.llAddFav).setOnClickListener {
            dismiss()
            showDialogForAddFav()
        }

        view.findViewById<LinearLayout>(R.id.llComment).setOnClickListener {
            dismiss()
            showDialogForComment()
        }

        view.findViewById<LinearLayout>(R.id.llArtist).setOnClickListener {
            dismiss()
            showDialogForArtist()
        }

        view.findViewById<LinearLayout>(R.id.llSleep).setOnClickListener {
            dismiss()
            showDialogForSleep()
        }
    }

    private fun showDialogForAddPlay() {
        val addPlaylistFragment = AddPlaylistFragment()
        addPlaylistFragment.show(parentFragmentManager, "AddPlaylistFragment")
    }

    private fun showDialogForAddFav() {
    }

    private fun showDialogForComment() {
        val dialog = Dialog(requireActivity())
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.more_background)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.END)
        dialog.setContentView(R.layout.bottom_comment)
        dialog.show()
    }

    private fun showDialogForArtist(){
        val dialog = Dialog(requireActivity())
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.more_background)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.END)
        dialog.setContentView(R.layout.bottom_artist)
        dialog.show()
    }

    private fun showDialogForSleep(){
        val dialog = Dialog(requireActivity())
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.more_background)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.END)
        dialog.setContentView(R.layout.bottom_sleep)
        dialog.show()
    }
}
