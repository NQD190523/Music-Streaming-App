import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.Genre
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.BannerAdapter
import com.project.appealic.ui.view.Adapters.PlaylistCardAdapter
import com.project.appealic.ui.view.Adapters.RecentlySongAdapter
import com.project.appealic.ui.view.Fragment.AddArtistFragment
import com.project.appealic.ui.view.Fragment.AddPlaylistLibraryFragment
import com.project.appealic.ui.view.Fragment.GenresFragment
import com.project.appealic.ui.view.Fragment.LikeSongFragment
import com.project.appealic.ui.view.setOnItemClickListener
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class LibraryFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val bannerImageResources = listOf(
            R.drawable.imagecard2,
            R.drawable.imagecard2,
            R.drawable.imagecard2
        )
        val bannerAdapter = BannerAdapter(bannerImageResources)

        val factory = SongViewModelFactory(
            SongRepository(requireActivity().application),
            UserRepository(requireActivity().application)
        )
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        val recyclerViewBanner = view.findViewById<RecyclerView>(R.id.rrBanner)
        recyclerViewBanner.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBanner.adapter = bannerAdapter

        val textViewAddArtists = view.findViewById<TextView>(R.id.tvAddArtists)
        val textViewAddPlaylists = view.findViewById<TextView>(R.id.tvAddPlaylists)

        val underlineView = view.findViewById<View>(R.id.underline)
        val initialUnderlineX = resources.getDimensionPixelOffset(R.dimen.dp_2)
        val deltaXPlaylists = textViewAddPlaylists.x - initialUnderlineX
        val deltaXArtists = textViewAddArtists.x - initialUnderlineX

        textViewAddPlaylists.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddPlaylistLibraryFragment())
                .addToBackStack(null)
                .commit()
            underlineView.animate().translationX(deltaXPlaylists).setDuration(300).start()
        }

        textViewAddArtists.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddArtistFragment())
                .addToBackStack(null)
                .commit()
            underlineView.animate().translationX(deltaXArtists).setDuration(300).start()
        }

        setupRecentlyViewedSongs(view)
        setupPlaylistCards(view)
        setupSelectPlaylistButton(view)

        return view
    }

    private fun setupRecentlyViewedSongs(view: View) {
        val recentlyViewedSongsRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recentlyViewedSongsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recentlySongAdapter = RecentlySongAdapter(requireContext(), emptyList())
        recentlyViewedSongsRecyclerView.adapter = recentlySongAdapter

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            songViewModel.getRecentSongs(currentUserId)
                .observe(viewLifecycleOwner, Observer { songs ->
                    recentlySongAdapter.updateData(songs)
                    recentlyViewedSongsRecyclerView.setOnItemClickListener(requireContext(), songViewModel, songs)
                })
        } else {
            // Handle the case when currentUserId is null
        }
    }

    private fun setupPlaylistCards(view: View) {
        val imageList = listOf(
            R.drawable.libraryliked_song,
            R.drawable.librarydownload,
            R.drawable.playlist1,
            R.drawable.playlistkpop,
            R.drawable.playlistvpop,
            R.drawable.playlisttiktok,
            R.drawable.playlistgenz,
            R.drawable.playlistlofi
        )

        val recyclerViewPlaylistCards = view.findViewById<RecyclerView>(R.id.card_gird)
        val cardAdapter = PlaylistCardAdapter(requireContext(), imageList)
        recyclerViewPlaylistCards.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerViewPlaylistCards.adapter = cardAdapter

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.dp_2)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.dp_2)
        val decoration = StaggeredGridSpacingItemDecoration(horizontalSpacing, verticalSpacing)
        recyclerViewPlaylistCards.addItemDecoration(decoration)

        cardAdapter.setOnItemClickListener(object : PlaylistCardAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                navigateToFragment(position)
            }
        })
    }

    private fun navigateToFragment(position: Int) {
        when (position) {
            0 -> {
                val likeSongFragment = LikeSongFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmenthome, likeSongFragment)
                    .addToBackStack(null)
                    .commit()
            }
            1 -> {
                // Uncomment the following code if you want to navigate to DownloadedSongFragment
                /*
                val downloadedSongFragment = DownloadedSongFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, downloadedSongFragment)
                    .addToBackStack(null)
                    .commit()
                */
            }
            else -> {
                val genre = when (position) {
                    2 -> Genre(R.drawable.chill, "Sleep")
                    3 -> Genre(R.drawable.kpop, "K-pop")
                    4 -> Genre(R.drawable.vpop, "V-Pop")
                    5 -> Genre(R.drawable.tiktok, "TikTok Hits")
                    6 -> Genre(R.drawable.genz, "Gen Z")
                    7 -> Genre(R.drawable.lofi, "Lo-fi")
                    else -> Genre(0, "Unknown")
                }

                val bundle = Bundle().apply {
                    putParcelable("selected_genre", genre)
                }
                val genresFragment = GenresFragment()
                genresFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmenthome, genresFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun setupSelectPlaylistButton(view: View) {
        view.findViewById<Button>(R.id.btnSelect).setOnClickListener {
            val dialog = SelectPlaylistFragmentDialog()
            dialog.show(childFragmentManager, SelectPlaylistFragmentDialog::class.java.simpleName)
        }
    }
}

class StaggeredGridSpacingItemDecoration(
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply {
            left = horizontalSpacing / 6
            right = horizontalSpacing / 6
            bottom = verticalSpacing
            }
        }
    }

    class SelectPlaylistFragmentDialog : DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.dialog_select, container, false)
            return view
        }
        override fun onStart() {
            super.onStart()
            val dialog = dialog
            if (dialog != null) {
                val width = 315
                val height = 655
                val params = dialog.window?.attributes
                params?.width = (width * resources.displayMetrics.density).toInt()
                params?.height = (height * resources.displayMetrics.density).toInt()
                dialog.window?.attributes = params as WindowManager.LayoutParams
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.background_select_playlist)
                dialog.window?.setBackgroundDrawable(drawable)
            }
        }
    }
