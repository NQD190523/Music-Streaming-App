package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.SleepingPlaylistAdapter
import com.project.appealic.ui.viewmodel.ArtistViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.ArtistViewModelFactory
import com.project.appealic.utils.SongViewModelFactory

class ArtistProfileFragment: Fragment() {
    private lateinit var featuredPlaylist: SleepingPlaylistAdapter
    private lateinit var recommendedSongAdapter: NewReleaseAdapter
    private lateinit var songViewModel: SongViewModel
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var rcsong: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storage = FirebaseStorage.getInstance()

        val artistImageView: ImageView = view.findViewById(R.id.imvArtistImage)
        val artistNameTextView: TextView = view.findViewById(R.id.txtNameArtist)

        // Correctly initialize SongViewModel using the custom factory
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        val artistFatory = ArtistViewModelFactory(ArtistRepository(requireActivity().application))
        artistViewModel = ViewModelProvider(this, artistFatory).get(ArtistViewModel::class.java)


        val selectedArtist = arguments?.getParcelable<Artist>("selectedArtist")
        selectedArtist?.let {
            // Hiển thị thông tin về artist được chọn
            artistNameTextView.text = it.Name
            // Hiển thị hình ảnh
            val gsReference = selectedArtist.ImageResource?.let { storage.getReferenceFromUrl(it) }
            Glide.with(requireContext())
                .load(gsReference)
                .into(artistImageView)
        }

        rcsong = view.findViewById(R.id.lvRecommendSongs)

        songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            rcsong.adapter = adapter
        })
    }
}