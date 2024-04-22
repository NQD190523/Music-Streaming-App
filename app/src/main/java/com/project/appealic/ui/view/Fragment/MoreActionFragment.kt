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
    private lateinit var trackId : String
    private lateinit var songTitle : String
    private lateinit var artistName : String
    private lateinit var trackImage : String
    private lateinit var trackUrl : String


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
        songViewModel = ViewModelProvider(requireActivity(), factory)[SongViewModel::class.java]

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

        // Lấy dữ liệu từ Bundle
        songTitle = arguments?.getString("SONG_TITLE").toString()
        artistName = arguments?.getString("SINGER_NAME").toString()
        trackImage = arguments?.getString("TRACK_IMAGE").toString()
        trackId = arguments?.getString("TRACK_ID").toString()
        trackUrl = arguments?.getString("TRACK_URL").toString()

        // Ánh xạ các thành phần trong layout
        val txtSongName = view.findViewById<TextView>(R.id.txtSongName)
        val txtSinger = view.findViewById<TextView>(R.id.txtSinger)
        val songImageView = view.findViewById<ImageView>(R.id.imvPhoto)

        // Gán giá trị ban đầu vào layout
        txtSongName.text = songTitle
        txtSinger.text = artistName

        // Load hình ảnh từ Firebase Storage
        println(trackImage)
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(trackImage)
        Glide.with(this)
            .load(storageReference)
            .into(songImageView)

        // Quan sát LiveData để cập nhật layout khi dữ liệu thay đổi
        songViewModel.recentTrack.observe(viewLifecycleOwner, Observer { recentSong ->
            // Cập nhật layout với dữ liệu mới
            txtSongName.text = recentSong[0].trackTitle.toString()
            txtSinger.text = recentSong[0].artist.toString()

            // Load hình ảnh mới từ Firebase Storage
            val newImageURL = recentSong[0].trackImage
            if (newImageURL != null) {
                val newStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(newImageURL)
                Glide.with(this)
                    .load(newStorageReference)
                    .into(songImageView)
            }

            // Cập nhật trackId (nếu cần thiết)
            trackId = recentSong[0].trackId.toString()
        })




        // Xét điều kiện hiển thị thêm vào yêu thích
        val heartIcon = view.findViewById<ImageView>(R.id.heartIcon)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        // Xét hiển thị bài hát yêu thích
        if (userId != null) {
            songViewModel.getLikedSongs(userId)
            songViewModel.likedSongs.observe(this, Observer { likedSongs ->
                if (likedSongs.any { it.trackId == trackId }) {
                    // Track hiện tại đã được người dùng yêu thích
                    heartIcon.setImageResource(R.drawable.ic_isliked)
                } else {
                    // Track hiện tại chưa được người dùng yêu thích
                    heartIcon.setImageResource(R.drawable.ic_heart_24_outlined)
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
        println(userId)
        val heartIcon = view?.findViewById<ImageView>(R.id.heartIcon)
        if (userId != null) {
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
                    println(artistImage)

                    // Hiển thị thông tin chi tiết của nghệ sĩ trên giao diện của Dialog
                    artistNameTextView.text = artistName
                    // Đoạn này chưa load được ảnh

                    if (artistImage != null && isAdded) {
                        val gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(artistImage)
                        println(gsReference)
                        Glide.with(this)
                            .load(gsReference)
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
