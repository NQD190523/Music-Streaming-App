package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.project.appealic.databinding.ActivityLoginPhoneOtpBinding

class ActivityLoginPhoneOTP : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPhoneOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPhoneOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        verificationId = intent.getStringExtra("verificationId") ?: ""

        binding.btnVerify.setOnClickListener {
            val otp = getEnteredOTP()
            if (otp.length == 4) {
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, "Please enter the 4-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getEnteredOTP(): String {
        val otp1 = binding.editText1.text.toString()
        val otp2 = binding.edtNumb2.text.toString()
        val otp3 = binding.edtNumb3.text.toString()
        val otp4 = binding.edtNumb4.text.toString()

        return otp1 + otp2 + otp3 + otp4
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this@ActivityLoginPhoneOTP, "Đăng nhập thành công bằng số điện thoại", Toast.LENGTH_SHORT).show()
                    navigateToMainScreen()
                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(this@ActivityLoginPhoneOTP, "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun navigateToMainScreen() {
        // Navigate to the main screen or the next screen after successful login
        val intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
        finish()
    }
}