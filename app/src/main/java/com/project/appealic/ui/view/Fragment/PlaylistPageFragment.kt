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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.data.model.PlayListEntity
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
    private lateinit var songNumb : TextView
    private val uid = Firebase.auth.currentUser!!.uid

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

        val imageViewBack = view.findViewById<ImageView>(R.id.imv_back)
        imageViewBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        // Correctly initialize SongViewModel using the custom factory
        val playlistFactory = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playListViewModel = ViewModelProvider(this,playlistFactory)[PlayListViewModel::class.java]
        val songFactory = SongViewModelFactory(SongRepository(requireActivity().application),UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this,songFactory)[SongViewModel::class.java]

        title = view.findViewById(R.id.textView16)
        songNumb = view.findViewById(R.id.txtSongNumb)
        trackInPlaylist = view.findViewById(R.id.lstPlalist)

        // Lấy đối tượng playlist từ arguments, có thể là Playlist hoặc PlayListEntity
        val selectedPlaylist: Playlist? = arguments?.getParcelable("selected_playlist") as? Playlist
        val selectedPlaylistEntity: PlayListEntity? = arguments?.getParcelable("user_selected_playlist") as? PlayListEntity
        // Kiểm tra kiểu của selectedPlaylist trước khi sử dụng
        if(selectedPlaylist !=null) {
            selectedPlaylist.let {
                view.findViewById<ImageView>(R.id.imageView5).let { playlistCover ->
                    selectedPlaylist.playlistThumb?.let { imageUrl ->
                        val gsReference = storage.getReferenceFromUrl(imageUrl)
                        Glide.with(requireContext())
                            .load(gsReference)
                            .into(playlistCover)
                    }
                }
            }
            // Gọi hàm để lấy tổng số bài hát từ Firebase và cập nhật UI
            getTotalSongsFromFirebase(selectedPlaylist.playlistId) { totalSongs ->
                songNumb.text = (totalSongs.toString() + " Songs")
            }
            rcsong = view.findViewById(R.id.lstRecommendSong)
            // Initialize adapter for ListView displaying recommended songs
            playListViewModel.getTracksFromPlaylist(selectedPlaylist.playlistId)
            songViewModel.getAllTracks()
            title.text = selectedPlaylist.playlistName
        }
        if(selectedPlaylistEntity !=null) {
            selectedPlaylistEntity.let { playlistEntity ->
                view.findViewById<ImageView>(R.id.imageView5)
                    .setImageResource(playlistEntity.playlistThumb)
                // Gọi hàm để lấy tổng số bài hát từ Firebase và cập nhật UI
                songNumb.text = (playlistEntity.trackIds.size.toString() + " Songs")
                rcsong = view.findViewById(R.id.lstRecommendSong)
                // Initialize adapter for ListView displaying recommended songs
                arguments?.getInt("playlist_index")
                    ?.let { playListViewModel.getTracksFromUserPlaylist(uid, it) }
                songViewModel.getAllTracks()
                title.text = playlistEntity.playListName
            }
        }
        playListViewModel.track.observe(viewLifecycleOwner, Observer { tracks ->
            songViewModel.recommendSong(tracks)
            val adapter = NewReleaseAdapter(requireContext(), tracks)
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
            trackInPlaylist.adapter = adapter
            setListViewHeightBasedOnItems(trackInPlaylist)
            trackInPlaylist.setOnItemClickListener(requireContext(), songViewModel,tracks )
        })
        songViewModel.recTracks.observe(viewLifecycleOwner, Observer {tracks ->
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
            setListViewHeightBasedOnItems(rcsong)
        })




        // Placeholder for sleepingPlaylistAdapter setup
        // Initialize and set up sleepingPlaylistAdapter here if needed
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
        playlistsRef.document(playlistId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val playlistData = task.result?.toObject(Playlist::class.java)
                val trackIds = playlistData?.trackIds ?: emptyList()
                val totalSongs = trackIds.size
                Log.d(ContentValues.TAG, "Total songs: $totalSongs")
                onResult(totalSongs)
            } else {
                Log.d(ContentValues.TAG, "Failed to get playlist data", task.exception)
                onResult(0)
            }
        }
    }
}