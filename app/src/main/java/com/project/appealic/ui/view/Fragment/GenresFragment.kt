package com.project.appealic.ui.view.Fragment
import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.appealic.R
import com.project.appealic.data.model.Genre
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.setOnItemClickListener
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class GenresFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var songViewModel: SongViewModel
    private lateinit var gensong: ListView
    private lateinit var rcsong: ListView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_page, container, false)
        imageView = view.findViewById(R.id.imageView5)
        textView = view.findViewById(R.id.textView16)
        gensong = view.findViewById(R.id.lstPlalist)
        return view
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val songFactory = SongViewModelFactory(
            SongRepository(requireActivity().application),
            UserRepository(requireActivity().application)
        )
        songViewModel = ViewModelProvider(this, songFactory)[SongViewModel::class.java]

        super.onViewCreated(view, savedInstanceState)
        songViewModel = ViewModelProvider(this).get(SongViewModel::class.java)

        val imageViewBack = view.findViewById<ImageView>(R.id.imv_back)
        imageViewBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

            val selectedGenre = requireArguments().getParcelable<Genre>("selected_genre")
            imageView.setImageResource(selectedGenre?.id ?: 0)
            textView.text = selectedGenre?.name ?: "Unknown"

            songViewModel.getTrackFromGenres(selectedGenre?.name ?: "")
            songViewModel.gerneTracks.observe(viewLifecycleOwner, Observer { tracks ->
                val adapter = NewReleaseAdapter(requireContext(), tracks)
                gensong.adapter = adapter
                setListViewHeightBasedOnItems(gensong)
                gensong.setOnItemClickListener(requireContext(), songViewModel, tracks)
            })


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