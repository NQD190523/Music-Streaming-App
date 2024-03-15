package com.project.appealic

import Activity_welcome2
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.databinding.ActivityWelcomeBinding

class Activity_welcome : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng DataBindingUtil để gắn kết layout
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện khi nút "Continue" được nhấn
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, Activity_welcome2::class.java)
            startActivity(intent)
        }
    }}