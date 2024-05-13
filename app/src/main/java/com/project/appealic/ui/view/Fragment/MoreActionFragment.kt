package com.project.appealic.ui.view.Fragment

import ArtistDetailFragment
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoreActionFragment : DialogFragment() {

    private lateinit var songViewModel: SongViewModel
    private lateinit var trackId: String
    private lateinit var songTitle: String
    private lateinit var artistName: String
    private lateinit var trackImage: String
    private lateinit var trackUrl: String
    private var album: Album? = null

    companion object {
        private const val ARG_TRACK = "arg_track"
        private const val ARG_ALBUM = "arg_album"

        fun newInstance(track: Track, album: Album? = null): MoreActionFragment {
            val fragment = MoreActionFragment()
            val args = Bundle().apply {
                putParcelable(ARG_TRACK, track)
                putString("TRACK_ID", track.trackId)
                putParcelable(ARG_ALBUM, album)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = SongViewModelFactory(
            SongRepository(requireActivity().application),
            UserRepository(requireActivity().application)
        )
        songViewModel = ViewModelProvider(requireActivity(), factory)[SongViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val track: Track? = arguments?.getParcelable(ARG_TRACK)
        return inflater.inflate(R.layout.fragment_more_action, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.fragment_more_action, null)
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

        songTitle = arguments?.getString("SONG_TITLE").toString()
        artistName = arguments?.getString("SINGER_NAME").toString()
        trackImage = arguments?.getString("TRACK_IMAGE").toString()
        trackId = arguments?.getString("TRACK_ID").toString()
        trackUrl = arguments?.getString("TRACK_URL").toString()
        album = arguments?.getParcelable(ARG_ALBUM)

        val txtSongName = view.findViewById<TextView>(R.id.txtSongName)
        val txtSinger = view.findViewById<TextView>(R.id.txtSinger)
        val songImageView = view.findViewById<ImageView>(R.id.imvPhoto)

        txtSongName.text = songTitle
        txtSinger.text = artistName

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(trackImage)
        Glide.with(this)
            .load(storageReference)
            .into(songImageView)

        songViewModel.recentTrack.observe(viewLifecycleOwner, Observer { recentSong ->
            txtSongName.text = recentSong[0].trackTitle.toString()
            txtSinger.text = recentSong[0].artist.toString()

            val newImageURL = recentSong[0].trackImage
            if (newImageURL != null) {
                val newStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(newImageURL)
                Glide.with(this)
                    .load(newStorageReference)
                    .into(songImageView)
            }

            trackId = recentSong[0].trackId.toString()
        })

        val heartIcon = view.findViewById<ImageView>(R.id.heartIcon)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            songViewModel.getLikedSongs(userId)
            songViewModel.likedSongs.observe(this, Observer { likedSongs ->
                if (likedSongs.any { it.trackId == trackId }) {
                    heartIcon.setImageResource(R.drawable.ic_isliked)
                } else {
                    heartIcon.setImageResource(R.drawable.ic_heart_24_outlined)
                }
            })
        } else {
            Toast.makeText(
                requireContext(),
                "You need to sign in to use this feature",
                Toast.LENGTH_SHORT
            ).show()
        }

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

        view.findViewById<LinearLayout>(R.id.llAblum).setOnClickListener {
            album?.let { showAlbumPage(it) }
        }

        view.findViewById<LinearLayout>(R.id.llArtist).setOnClickListener {
            showDialogForArtist()
        }

        view.findViewById<LinearLayout>(R.id.llSleep).setOnClickListener {
            dismiss()
            SleepFragmentDialog().show(
                requireActivity().supportFragmentManager,
                "SleepFragmentDialog"
            )
        }
    }

    private fun showDialogForAddPlay() {
        val addPlaylistFragment = AddPlaylistFragment()
        addPlaylistFragment.show(parentFragmentManager, "AddPlaylistFragment")
    }

    private fun handleAddToFavorites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
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
        } else {Toast.makeText(
            requireContext(),
            "You need to sign in to use this feature",
            Toast.LENGTH_SHORT
        ).show()
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

    private fun showDialogForArtist() {
        val bundle = Bundle().apply {
            putString("ARTIST_ID", arguments?.getString("ARTIST_ID"))
        }

        val artistDetailFragment = ArtistDetailFragment(requireActivity())
        artistDetailFragment.arguments = bundle
        artistDetailFragment.show(parentFragmentManager, "ArtistDetailFragment")
    }

    private fun showAlbumPage(album: Album) {
        val bundle = Bundle().apply {
            putParcelable("selected_album", album)
        }
        val albumPageFragment = AlbumPageFragment().apply {
            arguments = bundle
        }
        dismiss() // Đóng DialogFragment hiện tại
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragmenthome, albumPageFragment)
            .addToBackStack(null)
            .commit()
    }
}