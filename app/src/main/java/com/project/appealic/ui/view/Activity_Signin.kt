package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.databinding.ActivityLoginBinding

class Activity_Signin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện khi Forgot Password được nhấn
        binding.btnForgetPassword.setOnClickListener {
            val intent = Intent(this, ActivityForgotPassword::class.java)
            startActivity(intent)
        }

    }
}
