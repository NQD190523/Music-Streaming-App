package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.Album
import com.project.appealic.data.repository.AlbumRepository
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.ui.view.Adapters.AlbumsResultAdapter
import com.project.appealic.ui.view.Adapters.ArtistResultAdapter
import com.project.appealic.ui.view.Adapters.PlaylistResultAdapter
import com.project.appealic.ui.view.Adapters.SongResultAdapter
import com.project.appealic.ui.viewmodel.AlbumViewModel
import com.project.appealic.ui.viewmodel.ArtistViewModel
import com.project.appealic.ui.viewmodel.PlayListViewModel
import com.project.appealic.utils.AlbumViewModelFactory
import com.project.appealic.utils.ArtistViewModelFactory
import com.project.appealic.utils.PlayListViewModelFactory
import com.project.appealic.ui.view.setOnItemClickListener

class SearchResultFragment: Fragment() {
    private lateinit var listSong: ListView
    private lateinit var songViewModel: SongViewModel
    private lateinit var playlistViewModel: PlayListViewModel
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var listArtist: ListView
    private lateinit var listPlaylist: ListView
    private lateinit var listAlbum: ListView

    private lateinit var searchDatabase: DatabaseReference
    private var originalTracks: List<Track> = emptyList() // Danh sách gốc

    val userId = FirebaseAuth.getInstance().currentUser?.uid



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)

        val artistFactory = ArtistViewModelFactory(ArtistRepository(requireActivity().application))
        artistViewModel = ViewModelProvider(requireActivity(), artistFactory)[ArtistViewModel::class.java]

        listSong = view.findViewById(R.id.lvSearchResultSongs)
        listArtist = view.findViewById(R.id.lvSearchResultArtists)
        listPlaylist = view.findViewById(R.id.lvSearchResultPlaylists)
        listAlbum = view.findViewById(R.id.lvSearchResultAlbums)

        // Khởi tạo SongViewModel
        val factorySong = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factorySong)[SongViewModel::class.java]

        val factoryArtist = ArtistViewModelFactory(ArtistRepository(requireActivity().application))
        artistViewModel = ViewModelProvider(this, factoryArtist)[ArtistViewModel::class.java]

        val factoryPlaylist = PlayListViewModelFactory(PlayListRepository(requireActivity().application))
        playlistViewModel = ViewModelProvider(this, factoryPlaylist)[PlayListViewModel:: class.java]

        val factoryAlbum = AlbumViewModelFactory(AlbumRepository(requireActivity().application))
        albumViewModel = ViewModelProvider(this, factoryAlbum)[AlbumViewModel:: class.java]

        // Nhận dữ liệu từ Bundle
        val searchQuery = arguments?.getString("search_query")
        Log.d("SearchResultsActivity", "Received searchQuery: $searchQuery")
        if (!searchQuery.isNullOrEmpty()) {
            // Gọi phương thức trong ViewModel để tải dữ liệu từ Firebase dựa trên searchQuery
            songViewModel.SearchSongResults(searchQuery)
            artistViewModel.SearchArtistResults(searchQuery)
            playlistViewModel.SearchPlaylistResults(searchQuery)
            albumViewModel.SearchAlbumResults(searchQuery)
            // Quan sát LiveData _tracks để cập nhật ListView khi có kết quả tìm kiếm mới
            songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
                val adapterSong = SongResultAdapter(requireContext(), tracks)
                val txtSongResult = view.findViewById<TextView>(R.id.txtSongResult)

                if (tracks.isEmpty()){
                    listSong.visibility = View.GONE
                    txtSongResult.visibility = View.GONE
                } else {
                    listSong.visibility = View.VISIBLE
                    txtSongResult.visibility = View.VISIBLE

                    listSong.adapter = adapterSong

                    // Cập nhật chiều cao của ListView
                    setListViewHeightBasedOnItems(listSong)

                    // Thiết lập OnItemClickListener cho ListView
                    listSong.setOnItemClickListener(requireContext(), songViewModel, tracks)
                }
            })

            artistViewModel.artists.observe(viewLifecycleOwner, Observer { artists->
                val adapterArtist = ArtistResultAdapter(requireContext(), artists, artistViewModel)
                val txtArtistResult = view.findViewById<TextView>(R.id.txtArtistResult)
                if (artists.isEmpty()){
                    listArtist.visibility = View.GONE
                    txtArtistResult.visibility = View.GONE
                } else {
                    listArtist.visibility = View.VISIBLE
                    txtArtistResult.visibility = View.VISIBLE

                    listArtist.adapter = adapterArtist
                    setListViewHeightBasedOnItems(listArtist)
                }
            })

            playlistViewModel.playLists.observe(viewLifecycleOwner, Observer { playlits ->
                val adapterPlaylists = PlaylistResultAdapter(requireContext(), playlits)
                val txtPlaylistResult = view.findViewById<TextView>(R.id.txtPlaylistResult)
                if (playlits.isEmpty()){
                    listPlaylist.visibility = View.GONE
                    txtPlaylistResult.visibility = View.GONE
                } else {
                    listPlaylist.visibility = View.VISIBLE
                    txtPlaylistResult.visibility = View.VISIBLE

                    listPlaylist.adapter = adapterPlaylists
                    setListViewHeightBasedOnItems(listPlaylist)
                }
            })

            albumViewModel.album.observe(viewLifecycleOwner, Observer { albums ->
                val adapterAlbum = AlbumsResultAdapter(requireContext(), albums)
                val txtAlbumResult = view.findViewById<TextView>(R.id.txtAlbumResult)
                if (albums.isEmpty()){
                    listAlbum.visibility = View.GONE
                    txtAlbumResult.visibility = View.GONE
                } else {
                    listAlbum.visibility = View.VISIBLE
                    txtAlbumResult.visibility = View.VISIBLE

                    adapterAlbum.setOnItemClickListener(object : AlbumsResultAdapter.OnItemClickListener {
                        override fun onItemClick(album: Album) {
                            navigateToAlbumPageFragment(album)
                        }
                    })
                    listAlbum.adapter = adapterAlbum
                    setListViewHeightBasedOnItems(listAlbum)
                }

            })

        }
        return view
    }

    private fun navigateToAlbumPageFragment(album: Album) {
        val bundle = Bundle().apply {
            putParcelable("selected_album", album)
        }

        val albumPageFragment = AlbumPageFragment()
        albumPageFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmenthome, albumPageFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setListViewHeightBasedOnItems(listView: ListView) {
        val adapter = listView.adapter ?: return
        val totalItems = adapter.count
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        var totalHeight = 0

        for (i in 0 until totalItems) {
            val listItem: View = adapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (totalItems - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }

}