package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.databinding.ActivityForgotpassOtpBinding

class ActivityForgotpassOtp : AppCompatActivity(){
    private lateinit var binding: ActivityForgotpassOtpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotpassOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnVerify.setOnClickListener {
            val otp = binding.editText1.text.toString() + binding.edtNumb2.text.toString() +
                    binding.edtNumb3.text.toString() + binding.edtNumb4.text.toString()

            if (otp.isEmpty() || otp.length < 4) {
                Toast.makeText(applicationContext, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show()
            } else {
                // Code to verify the OTP
                // Đây là một đoạn mã giả mạo và bạn cần thay thế bằng logic xác minh OTP thực tế của bạn
                // Nếu OTP được xác minh thành công, bắt đầu ActivityNewPassword
                val intent = Intent(this, ActivityNewPassword::class.java)
                startActivity(intent)
            }
        }
    }
}