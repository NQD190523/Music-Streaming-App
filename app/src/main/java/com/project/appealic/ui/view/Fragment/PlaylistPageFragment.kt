package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.project.appealic.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository

import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.SleepingPlaylistAdapter
import com.project.appealic.ui.viewmodel.PlayListViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.PlayListViewModelFactory
import com.project.appealic.utils.SongViewModelFactory
import com.project.appealic.ui.view.setOnItemClickListener

class PlaylistPageFragment : Fragment() {

    private lateinit var sleepingPlaylistAdapter: SleepingPlaylistAdapter
    private lateinit var recommendedSongAdapter: NewReleaseAdapter
    private lateinit var playListViewModel: PlayListViewModel
    private lateinit var songViewModel: SongViewModel
    private lateinit var rcsong: ListView
    private lateinit var trackInPlaylist : ListView
    private lateinit var title : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storage = FirebaseStorage.getInstance()

        // Correctly initialize SongViewModel using the custom factory
        val playlistFactory = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playListViewModel = ViewModelProvider(this,playlistFactory)[PlayListViewModel::class.java]
        val songFactory = SongViewModelFactory(SongRepository(requireActivity().application),UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this,songFactory)[SongViewModel::class.java]

        val selectedPlaylist = arguments?.getParcelable<Playlist>("selected_playlist")
        selectedPlaylist?.let {
            view.findViewById<ImageView>(R.id.imageView5).let { playlistCover ->
                selectedPlaylist.playlistThumb?.let { imageUrl ->
                    val gsReference = storage.getReferenceFromUrl(imageUrl)
                    Glide.with(requireContext())
                        .load(gsReference)
                        .into(playlistCover)
                }
            }
        }
        title = view.findViewById(R.id.textView16)
        trackInPlaylist = view.findViewById(R.id.lstPlalistSleepping)

        rcsong = view.findViewById(R.id.lstRecommendSong)
        // Initialize adapter for ListView displaying recommended songs
        if (selectedPlaylist != null) {
            playListViewModel.getTracksFromPlaylist(selectedPlaylist.playlistId)
            songViewModel.getAllTracks()
            title.text = selectedPlaylist.playlistName
        }
        playListViewModel.track.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            trackInPlaylist.adapter = adapter
            trackInPlaylist.setOnItemClickListener(requireContext(), songViewModel,tracks )
        })
        songViewModel.tracks.observe(viewLifecycleOwner, Observer {tracks ->
            val adapter = NewReleaseAdapter(requireContext(),tracks)
            rcsong.adapter = adapter
        })




        // Placeholder for sleepingPlaylistAdapter setup
        // Initialize and set up sleepingPlaylistAdapter here if needed
    }
}