package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.view.Adapters.SleepingPlaylistAdapter
import com.project.appealic.ui.view.setOnItemClickListener
import com.project.appealic.ui.viewmodel.ArtistViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.ArtistViewModelFactory
import com.project.appealic.utils.SongViewModelFactory

class ArtistProfileFragment: Fragment() {

    private lateinit var recommendedSongAdapter: NewReleaseAdapter
    private lateinit var songViewModel: SongViewModel
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var rcsong: ListView
    private lateinit var ftSong: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storage = FirebaseStorage.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val artistImageView: ImageView = view.findViewById(R.id.imvArtistImage)
        val artistNameTextView: TextView = view.findViewById(R.id.txtNameArtist)
        val followBtn : Button = view.findViewById(R.id.btnFollow)

        // Correctly initialize SongViewModel using the custom factory
        val factory = SongViewModelFactory(SongRepository(requireActivity().application), UserRepository(requireActivity().application))
        songViewModel = ViewModelProvider(this, factory).get(SongViewModel::class.java)

        val artistFatory = ArtistViewModelFactory(ArtistRepository(requireActivity().application))
        artistViewModel = ViewModelProvider(this, artistFatory).get(ArtistViewModel::class.java)


        val selectedArtist = arguments?.getParcelable<Artist>("selectedArtist")
        selectedArtist?.let {
            // Hiển thị thông tin về artist được chọn
            artistNameTextView.text = it.Name
            // Hiển thị hình ảnh
            val gsReference = selectedArtist.ImageResource?.let { storage.getReferenceFromUrl(it) }
            Glide.with(requireContext())
                .load(gsReference)
                .into(artistImageView)
        }

        val artistId = selectedArtist?.Id.toString()

        // Xác định trạng thái của nút follow và cập nhật màu nền và văn bản
        if (userId != null) {

            artistViewModel.getFollowArtistFromUser(userId)
            artistViewModel.likedArtist.observe(context as LifecycleOwner, Observer {
                val isFollowed = artistViewModel.likedArtist.value?.any { it.Id == artistId } ?: false
                if (isFollowed) {
                    followBtn.text = "Following"
                    followBtn.setBackgroundResource(R.drawable.btn_following)
                } else {
                    followBtn.text = "Follow"
                    followBtn.setBackgroundResource(R.drawable.btn_not_follow)
                }
            })

        } else {
            // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
            Toast.makeText(context, "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
        }

        followBtn.setOnClickListener {
            if (userId != null) {

                val isFollowed = artistViewModel.likedArtist.value?.any { it.Id == artistId } ?: false
                if (selectedArtist != null) {
                    if (isFollowed) {
                        // Nếu đang following thì remove khỏi danh sách
                        artistViewModel.removeArtistToUserFollowArtist(userId, artistId)
                        followBtn.text = "Follow"
                        followBtn.setBackgroundResource(R.drawable.btn_not_follow)
                    } else {
                        // Nếu chưa following thì add vào danh sách
                        artistViewModel.addArtistToUserFollowArtist(userId, artistId)
                        followBtn.text = "Following"
                        followBtn.setBackgroundResource(R.drawable.btn_following)
                    }
                }
            } else {
                // Hiển thị thông báo yêu cầu đăng nhập nếu người dùng chưa đăng nhập
                Toast.makeText(context, "You need to sign in to use this feature", Toast.LENGTH_SHORT).show()
            }
        }

        ftSong = view.findViewById(R.id.lvFeaturedSongs)

        // Gọi hàm getTracksFromArtist từ artistViewModel để lấy danh sách bài hát của nghệ sĩ
        artistViewModel.getTracksFromArtist(artistId)

        // Lắng nghe thay đổi trong danh sách các bài hát và cập nhật giao diện khi có thay đổi
        artistViewModel.track.observe(viewLifecycleOwner, Observer { tracks ->
            // Khởi tạo adapter cho ListView và nạp danh sách bài hát vào adapter
            println(tracks[0].toString())
            val ftAdapter = NewReleaseAdapter(requireContext(), tracks)
            ftAdapter.setOnAddPlaylistClickListener { track ->
                // Mở dialog thêm playlist
                val addPlaylistFragment = AddPlaylistFragment.newInstance(track)
                addPlaylistFragment.show(childFragmentManager, "AddPlaylistFragment")
            }

            ftAdapter.setOnMoreActionClickListener {track ->
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
            ftSong.adapter = ftAdapter
            setListViewHeightBasedOnItems(ftSong)
            ftSong.setOnItemClickListener(requireContext(), songViewModel,tracks )
        })

        rcsong = view.findViewById(R.id.lvRecommendSongs)
        songViewModel.getAllTracks()
        songViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
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
