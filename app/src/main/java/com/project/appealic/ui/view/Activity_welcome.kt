package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.project.appealic.R
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.databinding.ActivityWelcomeBinding
import com.project.appealic.ui.viewmodel.AuthViewModel
import com.project.appealic.ui.viewmodel.SpotifyViewModel

class Activity_welcome : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityWelcomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sử dụng DataBindingUtil để gắn kết layout
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Khởi tạo viewModel tham chiếu đến AuthViewModel
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        // Khởi tạo GoogleSignInOptions
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        // Xử lý sự kiện khi nút "Continue" được nhấn
        binding.btnContinue.setOnClickListener {
            viewModel.signOut(googleSignInClient)
            val intent = Intent(this, GoogleLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }}