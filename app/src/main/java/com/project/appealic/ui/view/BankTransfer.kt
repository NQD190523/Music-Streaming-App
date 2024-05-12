package com.project.appealic.ui.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.project.appealic.R
import org.junit.runner.manipulation.Ordering
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.jar.Manifest

class BankTransfer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_transfer)

        // Nhận dữ liệu amount từ Intent
        val amount = intent.getDoubleExtra("AMOUNT", 0.0)

        // Hiển thị dữ liệu amount trên giao diện
        val txtAmount = findViewById<TextView>(R.id.txtAmount)
        txtAmount.text = "$" + amount

        // Lấy ra các TextView chứa dữ liệu cần sao chép
        val txtBankName = findViewById<TextView>(R.id.txtBankNameValue)
        val txtAccountName = findViewById<TextView>(R.id.txtAccountNameValue)
        val txtAccountNumber = findViewById<TextView>(R.id.txtAccountNumberValue)

        // Lấy ra nút copy tương ứng
        val btnCopyBankName = findViewById<ImageButton>(R.id.btnCopyBankName)
        val btnCopyAccountName = findViewById<ImageButton>(R.id.btnCopyAccountName)
        val btnCopyAccountNumber = findViewById<ImageButton>(R.id.btnCopyAccountNumber)

        // Thiết lập sự kiện click cho nút copy
        btnCopyBankName.setOnClickListener {
            copyToClipboard(txtBankName.text.toString())
        }

        btnCopyAccountName.setOnClickListener {
            copyToClipboard(txtAccountName.text.toString())
        }

        btnCopyAccountNumber.setOnClickListener {
            copyToClipboard(txtAccountNumber.text.toString())
        }

        val btnDownloadQRCode = findViewById<Button>(R.id.btnDownloadQRCode)
        btnDownloadQRCode.setOnClickListener {
            downloadQRCode()
        }

        val btnTransferred = findViewById<Button>(R.id.btnTransferred)
        btnTransferred.setOnClickListener {
            Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
        }
    }

    // Phương thức sao chép dữ liệu vào clipboard
    private fun copyToClipboard(data: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", data)
        clipboard.setPrimaryClip(clip)
        // Hiển thị thông báo hoặc thực hiện hành động khác sau khi sao chép thành công (tuỳ vào nhu cầu của bạn)
    }

    // Phương thức tải ảnh QR Code về thiết bị
    private fun downloadQRCode() {
        // Kiểm tra quyền ghi vào bộ nhớ ngoài
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Nếu chưa được cấp quyền, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST
            )
            return
        }

        // Lấy bitmap từ ImageView chứa QR Code
        val imageView = findViewById<ImageView>(R.id.imvQRCode)
        imageView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(imageView.drawingCache)
        imageView.isDrawingCacheEnabled = false

        // Tạo đường dẫn và tên file
        val fileName = "bank_tranfer_qr.png"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        try {
            // Lưu bitmap vào thư mục Download
            val file = File(directory, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            // Thêm ảnh vào thư viện ảnh của thiết bị
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            // Hiển thị thông báo tải ảnh thành công
            Toast.makeText(this, "QR Code downloaded", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            // Xử lý nếu gặp lỗi khi tải ảnh
            e.printStackTrace()
            Toast.makeText(this, "Failed to download QR Code", Toast.LENGTH_SHORT).show()
        }
    }

    // Hằng số để xác định request code khi yêu cầu quyền ghi vào bộ nhớ ngoài
    companion object {
        private const val WRITE_EXTERNAL_STORAGE_REQUEST = 101
    }
}
