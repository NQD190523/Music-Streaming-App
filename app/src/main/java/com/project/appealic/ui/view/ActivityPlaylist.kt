package com.project.appealic.ui.view

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playsong)

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


        // Lấy dữ liệu từ Intent và hiển thị trên giao diện
        val songTitle = intent.getStringExtra("SONG_TITLE")
        val artistName = intent.getStringExtra("SINGER_NAME")
        val trackImage = intent.getStringExtra("track_image")
        val duration = intent.getIntExtra("DURATION", 0)

        findViewById<TextView>(R.id.song_title).text = songTitle
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

    private fun formatDuration(durationInSeconds: Int): String {
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds % 60
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
        // Tạo BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)

        // Inflate layout cho dialog
        val view = layoutInflater.inflate(R.layout.botton_more_action, null)

        // Đặt hình nền có đường viền bo tròn cho dialog
        val window = bottomSheetDialog.window
        window?.setBackgroundDrawableResource(R.drawable.radius_background)
        val layoutParams = window?.attributes
        layoutParams?.gravity = Gravity.TOP or Gravity.START or Gravity.END
        // layoutParams?.gravity = Gravity.TOP or Gravity.END

        window?.attributes = layoutParams

        // Đặt nội dung cho dialog và hiển thị
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


    private fun handleShareButtonClick() {
        // Xử lý khi nhấn nút Share
    }

    private fun handleMultiplyButtonClick() {
        // Xử lý khi nhấn nút Multiply
    }
}
