package com.project.appealic.ui.view.Fragment

import SongAdapter
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    private lateinit var songViewModel: SongViewModel


    companion object {
        private const val ARG_TRACK = "arg_track"

        fun newInstance(track: Track): MoreActionFragment {
            val fragment = MoreActionFragment()
            val args = Bundle().apply {
                putParcelable(ARG_TRACK, track)
                putString("TRACK_ID", track.trackId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(requireActivity(), factory).get(SongViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View? {
        // Khởi tạo SongViewModel

        val track: Track? = arguments?.getParcelable(ARG_TRACK)
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
        val trackId = arguments?.getString("TRACK_ID").toString()

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

        // Xét điều kiện hiển thị thêm vào yêu thích
        val heartIcon = view.findViewById<ImageView>(R.id.heartIcon)
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Xét hiển thị bài hát yêu thích
        if (userId != null) {
            songViewModel.getLikedSongs(userId)
            songViewModel.likedSongs.observe(this, Observer { likedSong ->
                for (i in 0 until likedSong.size) {
                    if (likedSong.get(i).trackId == trackId)
                        heartIcon.setImageResource(R.drawable.ic_isliked)
                    else heartIcon.setImageResource(R.drawable.ic_heart_24_outlined)
                }
            })
        } else {
            // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
            Toast.makeText(requireContext(), "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
        }

        // Xử lý các sự kiện click
        view.findViewById<LinearLayout>(R.id.llAddPlay).setOnClickListener {
            dismiss()
            showDialogForAddPlay()
        }

        view.findViewById<LinearLayout>(R.id.llAddFav).setOnClickListener {
            handleAddToFavorites()
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

    private fun handleAddToFavorites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val heartIcon = view?.findViewById<ImageView>(R.id.heartIcon)
        val trackId = arguments?.getString("TRACK_ID")
        if (userId != null && trackId != null) {
            songViewModel.getLikedSongs(userId)
            val isLiked = songViewModel.likedSongs.value?.any { it.trackId == trackId } ?: false
            if (isLiked) {
                songViewModel.removeTrackFromUserLikedSongs(userId, trackId)
                heartIcon?.setImageResource(R.drawable.ic_heart_24_outlined)
            } else {
                songViewModel.addTrackToUserLikedSongs(userId, trackId)
                heartIcon?.setImageResource(R.drawable.ic_isliked)
            }
        } else {
            // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
            Toast.makeText(requireContext(), "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            delay(800)
            dismiss()
        }
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

        // Lấy ID của nghệ sĩ từ bundle
        val artistId = arguments?.getString("ARTIST_ID")

        // Truy vấn Firebase để lấy thông tin chi tiết về nghệ sĩ
        val artistNameTextView = dialog.findViewById<TextView>(R.id.artistName)
        val artistImageView = dialog.findViewById<ImageView>(R.id.artistImage)
        val artistRef = Firebase.firestore.collection("artists").document(artistId.toString())
        artistRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Lấy thông tin chi tiết về nghệ sĩ từ Firestore
                    val artistName = document.getString("Name")
                    val artistImage = document.getString("ImageResource")

                    // Hiển thị thông tin chi tiết của nghệ sĩ trên giao diện của Dialog
                    artistNameTextView.text = artistName
                    // Đoạn này chưa load được ảnh
                    if (artistImage != null && isAdded) {
                        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(artistImage)
                        Glide.with(requireContext()) // Sử dụng requireContext() thay vì this
                            .load(storageReference)
                            .into(artistImageView)
                        }
                    } else {
                        Log.d("MoreActionFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("MoreActionFragment", "get failed with ", exception)
                }


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
