package com.project.appealic.ui.view.Fragment

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.repository.service.MusicPlayerService
import com.project.appealic.ui.viewmodel.MusicPlayerViewModel

class MusicControlFragment : Fragment(){

    private var isRepeating = false
    private lateinit var progressTv: TextView
    private lateinit var progressSb: SeekBar
    private lateinit var durationTv: TextView
    private lateinit var previousBtn: ImageView
    private lateinit var playBtn: ImageView
    private lateinit var mixBtn: ImageView
    private lateinit var nextBtn: ImageView
    private lateinit var repeatBtn: ImageView
    private lateinit var commentBtn: ImageView
    private lateinit var downloadBtn: ImageView
    private lateinit var moreBtn: Button
    private lateinit var addPlaylistBtn: ImageView
    private lateinit var shareBtn: ImageView
    private lateinit var player: ExoPlayer
    private lateinit var musicPlayerViewModel: MusicPlayerViewModel
    private lateinit var trackId: String

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.MusicBinder
            val musicService = binder.getService()
            // Thiết lập MusicService cho MusicPlayerViewModel
            musicPlayerViewModel.setMusicService(musicService)
            player = musicPlayerViewModel.getPlayerInstance()!!
        }
        override fun onServiceDisconnected(className: ComponentName) {
            // Do nothing
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicPlayerServiceIntent = Intent(requireContext(), MusicPlayerService::class.java)
        requireContext().startService(musicPlayerServiceIntent)
        requireContext().bindService(musicPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        musicPlayerViewModel = ViewModelProvider(this)[MusicPlayerViewModel::class.java]


        // Khởi tạo thành phần UI
        progressTv = view.findViewById(R.id.progressTv)
        progressSb = view.findViewById(R.id.progressSb)
        durationTv = view.findViewById(R.id.durationTv)
        previousBtn = view.findViewById(R.id.previous)
        mixBtn = view.findViewById(R.id.mix)
        nextBtn = view.findViewById(R.id.next)
        repeatBtn = view.findViewById(R.id.repeat)
        commentBtn = view.findViewById(R.id.comment)
        downloadBtn = view.findViewById(R.id.dowmload)
        moreBtn = view.findViewById(R.id.more)
        addPlaylistBtn = view.findViewById(R.id.addPlaylist)
        shareBtn = view.findViewById(R.id.share)
        playBtn = view.findViewById(R.id.playPauseIcon)


        val songTitle = arguments?.getString("SONG_TITLE")
        val singerName = arguments?.getString("SINGER_NAME")
        val trackImage = arguments?.getString("TRACK_IMAGE")
        val duration = arguments?.getInt("DURATION") ?: 0
        val trackId = arguments?.getString("TRACK_ID")

        /// Tìm các view bằng ID
        val songTitleTextView = view.findViewById<TextView>(R.id.song_name)
        val singerNameTextView = view.findViewById<TextView>(R.id.singer_name)
        val songImageView = view.findViewById<ImageView>(R.id.trackImage)

// Hiển thị dữ liệu lên các view
        songTitle?.let {
            songTitleTextView.text = it
        }

        singerName?.let {
            singerNameTextView.text = it
        }

        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            val songImageView = view.findViewById<ImageView>(R.id.trackImage)

            songImageView?.let { imageView ->
                Glide.with(this)
                    .load(storageReference)
                    .into(imageView)
            }
        }
        // Gắn các hàm xử lý sự kiện cho các thành phần UI
        previousBtn.setOnClickListener { handlePreviousButtonClick() }
        mixBtn.setOnClickListener { handleMixButtonClick() }
        nextBtn.setOnClickListener { handleNextButtonClick() }
        repeatBtn.setOnClickListener { handleRepeatButtonClick() }
        commentBtn.setOnClickListener { handleCommentButtonClick() }
        downloadBtn.setOnClickListener { handleDownloadButtonClick() }
        moreBtn.setOnClickListener { handleMoreButtonClick() }
        addPlaylistBtn.setOnClickListener { handleAddPlaylistButtonClick() }
        shareBtn.setOnClickListener { handleShareButtonClick() }
        playBtn.setOnClickListener { handlePlayButtonClick() }

        //Cập nhật trạng thái khi thay đổi progressBar
        progressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Xử lý sự kiện thay đổi tiến trình
                if(fromUser) {
                    player.seekTo((progress *1000).toLong())
                    progressTv.text = formatDuration(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Xử lý khi bắt đầu chạm vào SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Xử lý khi kết thúc chạm vào SeekBar
            }
        })
        //ProgressBar cập nhật theo tiến độ của bài hát
        progressSb.max = duration / 1000
        musicPlayerViewModel.getCurrentPositionLiveData().observe(viewLifecycleOwner) { currentPosition ->
            progressSb.progress = (currentPosition / 1000).toInt()
            progressTv.text = formatDuration(currentPosition)
            val remainingDuration = (duration - currentPosition)
            durationTv.text = formatDuration(remainingDuration)
        }
    }


    private fun handleMixButtonClick() {
        // Xử lý khi nhấn nút Mix
    }

    private fun handlePreviousButtonClick() {
        // Xử lý khi nhấn nút Previous
        if (player != null) {
            // Kiểm tra xem có phát nhạc không
            if (player!!.playbackState != Player.STATE_IDLE && player!!.playbackState != Player.STATE_ENDED) {
                // Lấy vị trí của mục phương tiện hiện tại trong danh sách phát
                val currentMediaItemIndex = player!!.currentMediaItemIndex

                // Xác định vị trí của mục phương tiện trước đó
                val previousMediaItemIndex = if (currentMediaItemIndex > 0) currentMediaItemIndex - 1 else currentMediaItemIndex

                // Chuyển đến mục phương tiện trước đó
                player!!.seekToDefaultPosition(previousMediaItemIndex)
            }
        }
    }

    private fun handlePlayButtonClick() {
        // Xử lý khi nhấn nút Play
        if (player?.isPlaying == true) {
            player?.pause()
            playBtn.setImageResource(R.drawable.ic_play_24_filled)
        } else {
            player?.play()
            playBtn.setImageResource(R.drawable.ic_pause_24_filled)
        }
    }

    private fun handleNextButtonClick() {
        // Xử lý khi nhấn nút Next
        if (player != null) {
            // Kiểm tra xem có phát nhạc không
            if (player!!.playbackState != Player.STATE_IDLE && player!!.playbackState != Player.STATE_ENDED) {
                // Lấy vị trí của mục phương tiện hiện tại trong danh sách phát
                val currentMediaItemIndex = player!!.currentMediaItemIndex

                // Xác định vị trí của mục phương tiện kế tiếp
                val nextMediaItemIndex = if (currentMediaItemIndex < player!!.mediaItemCount - 1) currentMediaItemIndex + 1 else currentMediaItemIndex

                // Chuyển đến mục phương tiện kế tiếp
                player!!.seekToDefaultPosition(nextMediaItemIndex)
            }
        }
    }

    private fun handleRepeatButtonClick() {
        // Xử lý khi nhấn nút Repeat
        if (player.isPlaying) {
            isRepeating = !isRepeating
            player.repeatMode = if (isRepeating) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        }
    }

    private fun handleCommentButtonClick() {
        // Xử lý khi nhấn nút Comment
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_comment, null)

        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.more_background)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.END)

        dialog.setContentView(view)
        dialog.show()
    }


    private fun handleDownloadButtonClick() {
        // Xử lý khi nhấn nút Download
    }

    private fun handleMoreButtonClick() {
        val moreActionFragment = MoreActionFragment()
        val bundle = Bundle()
        bundle.putString("SONG_TITLE", arguments?.getString("SONG_TITLE"))
        bundle.putString("SINGER_NAME", arguments?.getString("SINGER_NAME"))
        bundle.putString("TRACK_IMAGE", arguments?.getString("TRACK_IMAGE"))
        moreActionFragment.arguments = bundle
        moreActionFragment.show(parentFragmentManager, "MoreActionsFragment")
    }


    private fun handleAddPlaylistButtonClick() {
        val addPlaylistFragment = AddPlaylistFragment()
        addPlaylistFragment.show(parentFragmentManager, "AddPlaylistFragment")
    }

    private fun handleShareButtonClick() {
        // Xử lý khi nhấn nút Share
    }

    private fun formatDuration(durationInSeconds: Long): String {
        val seconds = (durationInSeconds / 1000) % 60
        val minutes = durationInSeconds / 60000
        return "$minutes:${String.format("%02d", seconds)}"
    }
}