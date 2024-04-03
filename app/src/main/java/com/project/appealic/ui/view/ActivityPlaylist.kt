package com.project.appealic.ui.view

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.project.appealic.R
import com.project.appealic.ui.view.Fragment.AddPlaylistFragment
import com.squareup.picasso.Picasso
import okhttp3.internal.concurrent.formatDuration

class ActivityPlaylist : AppCompatActivity() {

    private lateinit var progressTv: TextView
    private lateinit var progressSb: SeekBar
    private lateinit var durationTv: TextView
    private lateinit var previousBtn: ImageView
    private lateinit var playBtn: ImageView
    private lateinit var mixBtn: ImageView
    private lateinit var playFrameLayout: FrameLayout
    private lateinit var nextBtn: ImageView
    private lateinit var repeatBtn: ImageView
    private lateinit var commentBtn: ImageView
    private lateinit var downloadBtn: ImageView
    private lateinit var moreBtn: Button
    private lateinit var shareBtn: ImageView
    private lateinit var multiplyBtn: ImageView
    private lateinit var mediaItem: MediaItem
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playsong)

        //Cấu hình exoplayer

        player = ExoPlayer.Builder(this)
            .build()

        // Khởi tạo tất cả các thành phần UI
        progressTv = findViewById(R.id.progressTv)
        progressSb = findViewById(R.id.progressSb)
        durationTv = findViewById(R.id.durationTv)
        previousBtn = findViewById(R.id.previous)
        mixBtn = findViewById(R.id.mix)
        playFrameLayout = findViewById(R.id.playPause)
        nextBtn = findViewById(R.id.next)
        repeatBtn = findViewById(R.id.repeat)
        commentBtn = findViewById(R.id.comment)
        downloadBtn = findViewById(R.id.dowmload)
        moreBtn = findViewById(R.id.more)
        shareBtn = findViewById(R.id.share)
        multiplyBtn = findViewById(R.id.multiply)
        playBtn = findViewById(R.id.playPauseIcon)


        // Lấy dữ liệu từ Intent và hiển thị trên giao diện
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val artistName = intent.getStringExtra("SINGER_NAME")
        val trackImage = intent.getStringExtra("TRACK_IMAGE")
        val duration = intent.getIntExtra("DURATION", 0)
        val trackUrl = intent.getStringExtra("TRACK_URL")

        findViewById<TextView>(R.id.song_name).text = songTitle
        findViewById<TextView>(R.id.singer_name).text = artistName
        findViewById<TextView>(R.id.durationTv).text = formatDuration(duration)

        // Load hình ảnh sử dụng Glide
        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            val songImageView = findViewById<ImageView>(R.id.trackImage)

            Glide.with(this)
                .load(storageReference)
                .into(songImageView)
        }

        //Khởi tạo exoplayer
        val storage = Firebase.storage
        val storageRef = storage.reference
        val trackPath = trackUrl?.substring(trackUrl.indexOf("/", 5) + 1)
        val audioRef = trackPath?.let { storageRef.child(it) }
        println(audioRef)
        if (audioRef != null) {
            audioRef.downloadUrl.addOnSuccessListener { url ->
                mediaItem = MediaItem.fromUri(url)
                player.setMediaItem(mediaItem)
                player.setPlaybackParameters(PlaybackParameters(1f))
                player.prepare()
            }
        }

        // Gắn các hàm xử lý sự kiện cho các thành phần UI
        previousBtn.setOnClickListener { handlePreviousButtonClick() }
        mixBtn.setOnClickListener { handlePlayButtonClick() }
        nextBtn.setOnClickListener { handleNextButtonClick() }
        repeatBtn.setOnClickListener { handleRepeatButtonClick() }
        commentBtn.setOnClickListener { handleCommentButtonClick() }
        downloadBtn.setOnClickListener { handleDownloadButtonClick() }
        moreBtn.setOnClickListener { handleMoreButtonClick() }
        shareBtn.setOnClickListener { handleShareButtonClick() }
        multiplyBtn.setOnClickListener { handleMultiplyButtonClick() }
        playBtn.setOnClickListener { handelPlayButtonClick() }

        // Thiết lập SeekBarChangeListener cho progressSb
        progressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Xử lý sự kiện thay đổi tiến trình

                // Cập nhật tiến trình vào TextView progressTv
                progressTv.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Xử lý khi bắt đầu chạm vào SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Xử lý khi kết thúc chạm vào SeekBar
            }
        })
    }

    private fun handelPlayButtonClick() {
        if (player.isPlaying) player.pause()
        else player.play()
    }

    private fun formatDuration(durationInSeconds: Int): String {
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds % 6000
        return "$minutes'${String.format("%02d", seconds)}''"
    }

    // Các hàm xử lý sự kiện khi nhấn các nút
    private fun handlePreviousButtonClick() {
        // Xử lý khi nhấn nút Previous
    }

    private fun handlePlayButtonClick() {
        // Xử lý khi nhấn nút Play
    }

    private fun handleNextButtonClick() {
        // Xử lý khi nhấn nút Next
    }

    private fun handleRepeatButtonClick() {
        // Xử lý khi nhấn nút Repeat
    }

    private fun handleCommentButtonClick() {
        // Xử lý khi nhấn nút Comment
    }

    private fun handleDownloadButtonClick() {
        // Xử lý khi nhấn nút Download
    }


    private fun handleMoreButtonClick() {
        // Tạo Dialog mới
        val dialog = Dialog(this)

        // Inflate layout cho dialog
        val view = layoutInflater.inflate(R.layout.botton_more_action, null)

        // Lưu tham chiếu đến dialog để có thể dismiss sau này
        val bottonMoreActionDialog = dialog

        // Lấy dữ liệu từ Intent và hiển thị trên giao diện playlist
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val artistName = intent.getStringExtra("SINGER_NAME")
        val trackImage = intent.getStringExtra("TRACK_IMAGE")
        val txtSongName = view.findViewById<TextView>(R.id.txtSongName)
        txtSongName.text = songTitle
        val txtSinger = view.findViewById<TextView>(R.id.txtSinger)
        txtSinger.text = artistName
        val songImageView = view.findViewById<ImageView>(R.id.imvPhoto)
        trackImage?.let { imageUrl ->
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

            Glide.with(this)
                .load(storageReference)
                .into(songImageView)
        }

        // Thêm sự kiện click vào các LinearLayout
        val llAddPlay = view.findViewById<LinearLayout>(R.id.llAddPlay)
        llAddPlay.setOnClickListener {
            bottonMoreActionDialog.dismiss()
            showDialogForAddPlay()
        }

        val llAddFav = view.findViewById<LinearLayout>(R.id.llAddFav)
        llAddFav.setOnClickListener {
            bottonMoreActionDialog.dismiss()
            showDialogForAddFav()
        }

        val llComment = view.findViewById<LinearLayout>(R.id.llComment)
        llComment.setOnClickListener {
            bottonMoreActionDialog.dismiss()
            showDialogForComment()
        }

        dialog.setContentView(view)

        // Tùy chỉnh Window của dialog
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.radius_background)
        val layoutParams = window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM or Gravity.START or Gravity.END
        layoutParams?.width =
            WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = layoutParams

        // Hiển thị dialog
        dialog.show()
    }

    private fun showDialogForComment() {
        val bottomSheetDialog = BottomSheetDialog(this)

        // Inflate layout cho dialog
        val view = layoutInflater.inflate(R.layout.bottom_comment, null)

        // Tùy chỉnh Window của dialog
        val window = bottomSheetDialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM) // Hiển thị ở dưới cùng

        // Thiết lập nội dung cho dialog và hiển thị
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
    private fun showDialogForAddPlay() {
        val addPlaylistFragment = AddPlaylistFragment()
        addPlaylistFragment.show(supportFragmentManager, "AddPlaylistFragment")
    }
}

    private fun showDialogForAddFav() {
        // Tạo và hiển thị dialog cho Add to favourite
        // Bạn có thể tạo một dialog khác nhau tại đây
    }




    private fun handleShareButtonClick() {
        // Xử lý khi nhấn nút Share
    }

    private fun handleMultiplyButtonClick() {
        // Xử lý khi nhấn nút Multiply
    }


