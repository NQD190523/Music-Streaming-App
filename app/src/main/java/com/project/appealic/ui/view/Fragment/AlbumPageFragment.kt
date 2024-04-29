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
import com.project.appealic.data.model.Album
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.AlbumViewModel
import com.project.appealic.ui.viewmodel.AlbumViewModelFactory
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class AlbumPageFragment() : Fragment() {
    private lateinit var songViewModel: SongViewModel
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var rcsong: ListView
    private lateinit var trackInAlbum: ListView
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

        val imageViewBack = view.findViewById<ImageView>(R.id.imv_back)
        imageViewBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Correctly initialize SongViewModel using the custom factory
        val songFactory = SongViewModelFactory(
            SongRepository(requireActivity().application),
            UserRepository(requireActivity().application)
        )
        songViewModel = ViewModelProvider(this, songFactory)[SongViewModel::class.java]

        val viewModelFactory = AlbumViewModelFactory(requireActivity().application)
        albumViewModel = ViewModelProvider(this, viewModelFactory).get(AlbumViewModel::class.java)

        val selectedAlbum: Album? = arguments?.getParcelable("selected_album")
        selectedAlbum?.let {
            view.findViewById<ImageView>(R.id.imageView5).let { albumCover ->
                selectedAlbum.thumbUrl?.let { imageUrl ->
                    val gsReference = storage.getReferenceFromUrl(imageUrl)
                    Glide.with(requireContext())
                        .load(gsReference)
                        .into(albumCover)
                }
            }
        }

        title = view.findViewById(R.id.textView16)
        songNumb = view.findViewById(R.id.txtSongNumb)
        trackInAlbum = view.findViewById(R.id.lstPlalist)
        rcsong = view.findViewById(R.id.lstRecommendSong)

        // Gọi hàm để lấy tổng số bài hát từ dữ liệu album và cập nhật UI
        if (selectedAlbum != null) {
            getTotalSongsFromFirebase(selectedAlbum) { totalSongs ->
                songNumb.text = "$totalSongs Songs"
            }
        }

        if (selectedAlbum != null) {
            albumViewModel.getTracksFromAlbum(selectedAlbum.albumId)
            songViewModel.getAllTracks()
            title.text = selectedAlbum.title
        }

        albumViewModel.track.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            adapter.setOnMoreActionClickListener { track ->
                track.trackUrl?.let { songViewModel.getTrackByUrl(it) }

                val moreActionFragment = MoreActionFragment.newInstance(track)
                val bundle = Bundle()
                bundle.putString("SONG_TITLE", track.trackTitle)
                bundle.putString("SINGER_NAME", track.artist)
                bundle.putString("TRACK_IMAGE", track.trackImage)
                bundle.putString("ARTIST_ID", track.artistId)
                bundle.putString("TRACK_ID", track.trackId)
                bundle.putString("TRACK_URL", track.trackUrl)
                moreActionFragment.arguments = bundle
                moreActionFragment.show(childFragmentManager, "MoreActionsFragment")
            }
            trackInAlbum.adapter = adapter
            setListViewHeightBasedOnItems(trackInAlbum)
        })

        songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            val adapter = NewReleaseAdapter(requireContext(), tracks)
            adapter.setOnAddPlaylistClickListener { track ->
                // Mở dialog thêm playlist
                val addPlaylistFragment = AddPlaylistFragment.newInstance(track)
                addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
            }

            adapter.setOnMoreActionClickListener { track ->
                track.trackUrl?.let { songViewModel.getTrackByUrl(it) }

                val moreActionFragment = MoreActionFragment.newInstance(track)
                val bundle = Bundle()
                bundle.putString("SONG_TITLE", track.trackTitle)
                bundle.putString("SINGER_NAME", track.artist)
                bundle.putString("TRACK_IMAGE", track.trackImage)
                bundle.putString("ARTIST_ID", track.artistId)
                bundle.putString("TRACK_ID", track.trackId)
                bundle.putString("TRACK_URL", track.trackUrl)
                moreActionFragment.arguments = bundle
                moreActionFragment.show(childFragmentManager, "MoreActionsFragment")
            }
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

    private fun getTotalSongsFromFirebase(album: Album, onResult: (Int) -> Unit) {
        val totalSongs = album.trackIds?.size ?: 0
        onResult(totalSongs)
    }
}