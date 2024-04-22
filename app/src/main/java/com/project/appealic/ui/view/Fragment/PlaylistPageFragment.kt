package com.project.appealic.ui.view.Fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore
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

    private lateinit var recommendedSongAdapter: NewReleaseAdapter
    private lateinit var playListViewModel: PlayListViewModel
    private lateinit var songViewModel: SongViewModel
    private lateinit var rcsong: ListView
    private lateinit var trackInPlaylist: ListView
    private lateinit var title: TextView
    private lateinit var songNumb: TextView

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

        val playlistFactory = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playListViewModel = ViewModelProvider(this, playlistFactory)[PlayListViewModel::class.java]
        val songFactory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, songFactory)[SongViewModel::class.java]

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
        songNumb = view.findViewById(R.id.txtSongNumb)
        trackInPlaylist = view.findViewById(R.id.lstPlalist)
        rcsong = view.findViewById(R.id.lstRecommendSong)

        if (selectedPlaylist != null) {
            getTotalSongsFromFirebase(selectedPlaylist.playlistId) { totalSongs ->
                songNumb.text = "$totalSongs Songs"
            }
            playListViewModel.getTracksFromPlaylist(selectedPlaylist.playlistId)
            songViewModel.getAllTracks()
            title.text = selectedPlaylist.playlistName
        }

        playListViewModel.track.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            trackInPlaylist.adapter = adapter
            setListViewHeightBasedOnItems(trackInPlaylist)
            trackInPlaylist.setOnItemClickListener(requireContext(), songViewModel, tracks)
        })

        songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            rcsong.adapter = adapter
        })
    }

    private fun setListViewHeightBasedOnItems(listView: ListView) {
        val listAdapter = listView.adapter
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem: View = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun getTotalSongsFromFirebase(playlistId: String, onResult: (Int) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val playlistsRef = db.collection("playlists")
        playlistsRef.document(playlistId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val playlistData = documentSnapshot.toObject(Playlist::class.java)
                    val trackIds = playlistData?.trackIds ?: emptyList()
                    val totalSongs = trackIds.size
                    Log.d(ContentValues.TAG, "Total songs: $totalSongs")
                    onResult(totalSongs)
                } else {
                    Log.d(ContentValues.TAG, "Playlist document with ID $playlistId does not exist")
                    onResult(0)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Failed to get playlist data", exception)
                onResult(0)
            }
    }
}