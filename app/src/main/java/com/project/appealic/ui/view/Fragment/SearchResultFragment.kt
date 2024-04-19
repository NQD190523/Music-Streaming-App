package com.project.appealic.ui.view.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.project.appealic.R
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.model.SongEntity
import com.project.appealic.ui.view.ActivityHome
import com.project.appealic.ui.view.ActivityMusicControl
import com.project.appealic.ui.view.ActivityNotification
import com.project.appealic.ui.view.Adapters.AlbumsResultAdapter
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.ArtistResultAdapter
import com.project.appealic.ui.view.Adapters.PlaylistResultAdapter
import com.project.appealic.ui.view.Adapters.SongResultAdapter
import java.util.ArrayList

class SearchResultFragment: Fragment() {
    private lateinit var listSong: ListView
    private lateinit var songViewModel: SongViewModel
    private lateinit var listArtist: ListView
    private lateinit var listPlaylist: ListView
    private lateinit var listAlbum: ListView

    private lateinit var searchDatabase: DatabaseReference
    private var originalTracks: List<Track> = emptyList() // Danh sách gốc

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)

        listSong = view.findViewById(R.id.lvSearchResultSongs)
        listArtist = view.findViewById(R.id.lvSearchResultArtists)
        listPlaylist = view.findViewById(R.id.lvSearchResultPlaylists)
        listAlbum = view.findViewById(R.id.lvSearchResultAlbums)

        // Khởi tạo SongViewModel
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        // Nhận dữ liệu từ Bundle
        val searchQuery = arguments?.getString("search_query")
        Log.d("SearchResultsActivity", "Received searchQuery: $searchQuery")
        if (!searchQuery.isNullOrEmpty()) {
            // Gọi phương thức trong ViewModel để tải dữ liệu từ Firebase dựa trên searchQuery
            songViewModel.loadSearchResults(searchQuery)

            // Quan sát LiveData _tracks để cập nhật ListView khi có kết quả tìm kiếm mới
            songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
                val adapterSong = SongResultAdapter(requireContext(), tracks)
                listSong.adapter = adapterSong

                // Cập nhật chiều cao của ListView
                setListViewHeightBasedOnItems(listSong)

                // Thiết lập OnItemClickListener cho ListView
                listSong.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        // Lấy dữ liệu của mục được chọn từ Adapter
                        val selectedSong = parent.getItemAtPosition(position) as Track
                        //lưu bài hát vừa mở vào database của thiết bị
                        val user = FirebaseAuth.getInstance().currentUser?.uid
                        val intent = Intent(requireContext(), ActivityMusicControl::class.java)
                        val trackUrlList = ArrayList<String>()

                        val song = selectedSong.trackId?.let {
                            SongEntity(
                                it,
                                selectedSong.trackImage,
                                selectedSong.trackTitle,
                                selectedSong.artist,
                                user,
                                null,
                                System.currentTimeMillis(),
                                null,
                                selectedSong.duration?.toLong(),
                                selectedSong.artistId,
                            )
                        }

                        if (song != null) {
                            songViewModel.insertSong(song)
                            Log.d(" test status", "success")
                        }
                        //lấy dữ liệu vài hát vừa nghẻ được lưu ra
                        if (user != null) {
                            val info = songViewModel.getRecentSongs(user)
                                .observe(viewLifecycleOwner, Observer { song ->
                                    Log.d("info", song.toString())
                                })
                        }
//              Lấy dữ liệu các url trogn playlist
                        for (i in 0 until parent.count) {
                            val item = parent.getItemAtPosition(i) as Track
                            item.trackUrl?.let { trackUrl ->
                                trackUrlList.add(trackUrl)
                            }
                        }
                        // Truyền dữ liệu cần thiết qua Intent
                        intent.putExtra("SONG_TITLE", selectedSong.trackTitle)
                        intent.putExtra("SINGER_NAME", selectedSong.artist)
                        intent.putExtra("SONG_NAME", selectedSong.trackTitle)
                        intent.putExtra("TRACK_IMAGE", selectedSong.trackImage)
                        intent.putExtra("ARTIST_ID", selectedSong.artistId)
                        intent.putExtra("DURATION", selectedSong.duration)
                        intent.putExtra("TRACK_URL", selectedSong.trackUrl)
                        intent.putExtra("TRACK_ID", selectedSong.trackId)
                        intent.putExtra("TRACK_INDEX", position)
                        intent.putStringArrayListExtra("TRACK_LIST", trackUrlList)
                        startActivity(intent)
                }
            })

            songViewModel.artists.observe(viewLifecycleOwner, Observer { artists->
                val adapterArtist = ArtistResultAdapter(requireContext(), artists)
                listArtist.adapter = adapterArtist
                // Cập nhật chiều cao của ListView
                setListViewHeightBasedOnItems(listArtist)
            })

            songViewModel.playlists.observe(viewLifecycleOwner, Observer { playlits ->
                val adapterPlaylists = PlaylistResultAdapter(requireContext(), playlits)
                listPlaylist.adapter = adapterPlaylists
                setListViewHeightBasedOnItems(listPlaylist)
            })

            songViewModel.albums.observe(viewLifecycleOwner, Observer { albums ->
                val adapterAlbum = AlbumsResultAdapter(requireContext(), albums)
                listAlbum.adapter = adapterAlbum
                setListViewHeightBasedOnItems(listAlbum)
            })
        }


        return view
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

}