import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.ActivityLikedSong
import com.project.appealic.ui.view.Adapters.BannerAdapter
import com.project.appealic.ui.view.Adapters.PlaylistCardAdapter
import com.project.appealic.ui.view.Adapters.RecentlySongAdapter
import com.project.appealic.ui.view.Fragment.AddAlbumFragment
import com.project.appealic.ui.view.Fragment.AddArtistFragment
import com.project.appealic.ui.view.Fragment.AddPlaylistLibraryFragment
import com.project.appealic.ui.view.Fragment.SearchMainFragment
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class LibraryFragment : Fragment() {

    private lateinit var songViewModel: SongViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val bannerImages = listOf(
            R.drawable.imagecard2,
            R.drawable.imagecard2,
            R.drawable.imagecard2
        )
        val bannerAdapter = BannerAdapter(bannerImages)
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)


        // Initialize and configure the RecyclerView for banner
        val recyclerViewBanner = view.findViewById<RecyclerView>(R.id.rrBanner)
        recyclerViewBanner.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBanner.adapter = bannerAdapter

        // Tìm kiếm các TextView tương ứng
        val tvAddArtists = view.findViewById<TextView>(R.id.tvAddArtists)
        val tvAddAlbums = view.findViewById<TextView>(R.id.tvAddAlbums)
        val tvAddSongs = view.findViewById<TextView>(R.id.tvAddPlaylists)

        val underline = view.findViewById<View>(R.id.underline)

//recently view song
        val recentlyViewSong: RecyclerView = view.findViewById(R.id.recyclerView)
        recentlyViewSong.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val recentlySongAdapter = RecentlySongAdapter(requireContext(), emptyList())
        recentlyViewSong.adapter = recentlySongAdapter
        songViewModel.getRecentSongs(FirebaseAuth.getInstance().currentUser?.uid.toString()).observe(viewLifecycleOwner, Observer { songs ->
            recentlySongAdapter.updateData(songs)
        })


// Lưu trữ vị trí ban đầu của underline
        val initialX = 203f
        val deltaXSongs =  tvAddSongs.x - initialX*2
        val deltaXArtists = tvAddArtists.x - initialX*5/4
        val deltaXAlbums = tvAddAlbums.x - initialX * 1/2

// Thiết lập onClickListener cho TextViews

        tvAddSongs.setOnClickListener {
            // Thay thế fragment hiện tại bằng AddSongToPlaylistFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddPlaylistLibraryFragment())
                .addToBackStack(null)
                .commit()
            underline.animate().translationX(deltaXSongs).setDuration(300).start()
        }

        tvAddArtists.setOnClickListener {
            // Replace the current fragment with AddArtistFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddArtistFragment())
                .addToBackStack(null)
                .commit()
            underline.animate().translationX(deltaXArtists).setDuration(300).start()
        }

        tvAddAlbums.setOnClickListener {
            // Replace the current fragment with AddAlbumFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAddPlaylistLibrary, AddAlbumFragment())
                .addToBackStack(null)
                .commit()
            underline.animate().translationX(deltaXAlbums).setDuration(300).start()
        }

        // Danh sách hình ảnh từ thư mục drawable
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

        // Tạo adapter và thiết lập cho card_recycle
        val cardAdapter = PlaylistCardAdapter(requireContext(), imageList)
        val recycleCardPlaylist : RecyclerView  = view.findViewById(R.id.card_gird)

        recycleCardPlaylist.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        recycleCardPlaylist.adapter = cardAdapter

        cardAdapter.setOnItemClickListener(object : PlaylistCardAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (position == 0) {
                    // Chuyển sang ActivityLikedSong
                    val intent = Intent(requireContext(), ActivityLikedSong::class.java)
                    startActivity(intent)
                }
            }
        })

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.dp_2) // Khoảng cách ngang mong muốn
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.dp_2) // Khoảng cách dọc mong muốn
        val decoration = StaggeredGridSpacingItemDecoration(horizontalSpacing, verticalSpacing)
        recycleCardPlaylist.addItemDecoration(decoration)


        // Select playlists
        view.findViewById<Button>(R.id.btnSelect).setOnClickListener {
            val dialog = SelectPlaylistFragmentDialog()
            dialog.show(childFragmentManager, dialog::class.simpleName)
        }
        return view

        // Create playlist

    }

    class StaggeredGridSpacingItemDecoration(private val horizontalSpacing: Int, private val verticalSpacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
    }
}
