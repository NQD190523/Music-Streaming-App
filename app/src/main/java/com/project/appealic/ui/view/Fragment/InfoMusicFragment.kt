package com.project.appealic.ui.view.Fragment

import android.annotation.SuppressLint
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
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.PlaylistForYouAdapter

import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class InfoMusicFragment : Fragment() {
    private var songTitle: String? = null
    private var artistName: String? = null
    private var trackImage: String? = null
    private var genre: String? = null
    private var albumId: String? = null
    private var releaseDate: String? = null
    private lateinit var rcsong: ListView
    private lateinit var songViewModel: SongViewModel
    private var player: ExoPlayer? = null

    private var duration: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            songTitle = it.getString("songTitle")
            artistName = it.getString("artistName")
            trackImage = it.getString("trackImage")
            genre = it.getString("genre")
            albumId = it.getString("albumId")
            releaseDate = it.getString("releaseDate")

        }

        val songFactory = SongViewModelFactory(
            SongRepository(requireActivity().application),
            UserRepository(requireActivity().application)
        )

        songViewModel = ViewModelProvider(requireActivity(), songFactory).get(SongViewModel::class.java)

        songViewModel.recentTrack.observe(requireActivity(), Observer {track ->
            Log.i("hello", "HELOOOOOOOOO")
            requireView().findViewById<TextView>(R.id.txtSongName).text = track[0].trackTitle
            requireView().findViewById<TextView>(R.id.txtSinger).text = track[0].artist
            if (track[0].trackImage?.isNotEmpty() == true) {
                val gsReference = track[0].trackImage?.let { FirebaseStorage.getInstance().getReferenceFromUrl(it) }
                Glide.with(this).load(gsReference).into(requireView().findViewById<ImageView>(R.id.imvPhoto))
            }
            requireView().findViewById<TextView>(R.id.txtAlbum).text = track[0].albumId
            requireView().findViewById<TextView>(R.id.txtMusician).text = track[0].artist
            requireView().findViewById<TextView>(R.id.txtGenre).text = track[0].genre
            requireView().findViewById<TextView>(R.id.txtReleased).text = track[0].releaseDate
        })
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_music, container, false)
        view.findViewById<TextView>(R.id.txtSongName).text = songTitle
        view.findViewById<TextView>(R.id.txtSinger).text = artistName

        val imageView = view.findViewById<ImageView>(R.id.imvPhoto)
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(trackImage.toString())
        Glide.with(this)
            .load(storageReference)
            .into(imageView)

        view.findViewById<TextView>(R.id.txtAlbum).text = albumId
        view.findViewById<TextView>(R.id.txtGenre).text = genre
        view.findViewById<TextView>(R.id.txtMusician).text = artistName
        view.findViewById<TextView>(R.id.txtReleased).text = releaseDate

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val songFactory = SongViewModelFactory(
            SongRepository(requireActivity().application),
            UserRepository(requireActivity().application)
        )
        songViewModel = ViewModelProvider(this, songFactory)[SongViewModel::class.java]

        super.onViewCreated(view, savedInstanceState)
        songViewModel = ViewModelProvider(this).get(SongViewModel::class.java)


        rcsong = view.findViewById(R.id.lstRecommendSong)
        songViewModel.getAllTracks()
        songViewModel.tracks.observe(viewLifecycleOwner, Observer {tracks ->
            val adapter = NewReleaseAdapter(requireContext(),tracks)
            adapter.setOnAddPlaylistClickListener { track ->
                // Mở dialog thêm playlist
                val addPlaylistFragment = AddPlaylistFragment.newInstance(track)
                addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
            }

            adapter.setOnMoreActionClickListener {track ->
                track.trackUrl?.let { songViewModel.getTrackByUrl(it) }

                val moreActionFragment = MoreActionFragment.newInstance(track)
                val bundle = Bundle()
                bundle.putString("SONG_TITLE", track.trackTitle)
                bundle.putString("SINGER_NAME", track.artist)
                bundle.putString("TRACK_IMAGE", track.trackImage)
                bundle.putString("ARTIST_ID", track.artistId)
                bundle.putString("TRACK_ID", track.trackId)
                bundle.putString("TRACK_URL",track.trackUrl)
                moreActionFragment.arguments = bundle
                moreActionFragment.show(childFragmentManager, "MoreActionsFragment")
            }
            rcsong.adapter = adapter
        })
        // Khởi tạo RecyclerView cho danh sách các playlist cho người dùng
        val playlistForU: RecyclerView = view.findViewById(R.id.recyclerView)
        playlistForU.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val playlistForUAdapter = PlaylistForYouAdapter(requireContext(), emptyList())
        playlistForU.adapter = playlistForUAdapter

        songViewModel.playlists.observe(viewLifecycleOwner, Observer { playlists ->
            playlistForUAdapter.updateData(playlists)
        })
        songViewModel.getAllPlaylists()

    }

    companion object {
        fun newInstance(
            songTitle: String?,
            artistName: String?,
            trackImage: String?,
            duration: Int
        ): InfoMusicFragment {
            return InfoMusicFragment().apply {
                arguments = Bundle().apply {
                    putString("songTitle", songTitle)
                    putString("artistName", artistName)
                    putString("trackImage", trackImage)
                    putInt("duration", duration)
                }
            }
        }
    }
}