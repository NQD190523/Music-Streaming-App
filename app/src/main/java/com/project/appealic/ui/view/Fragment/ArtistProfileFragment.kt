package com.project.appealic.ui.view.Fragment

import android.os.Bundle
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
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.SleepingPlaylistAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class ArtistProfileFragment: Fragment() {
    private lateinit var featuredPlaylist: SleepingPlaylistAdapter
    private lateinit var recommendedSongAdapter: NewReleaseAdapter
    private lateinit var songViewModel: SongViewModel
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

        // Correctly initialize SongViewModel using the custom factory
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        val selectedArtist = arguments?.getParcelable<Artist>("selected_artist")
        selectedArtist?.let {
            view.findViewById<ImageView>(R.id.imageView).let { artistCover ->
                selectedArtist.ImageResource?.let { imageUrl ->
                    val gsReference = storage.getReferenceFromUrl(imageUrl)
                    Glide.with(requireContext())
                        .load(gsReference)
                        .into(artistCover)
                }
            }
            view.findViewById<TextView>(R.id.txtNameArtist).let { artistName ->

            }
            view.findViewById<TextView>(R.id.txtFollowed).let { artistFollowers ->

            }
        }

        rcsong = view.findViewById(R.id.lvRecommendSongs)
        songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            rcsong.adapter = adapter
        })
    }
}