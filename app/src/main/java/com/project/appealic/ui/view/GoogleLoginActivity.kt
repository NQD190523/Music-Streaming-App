package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.databinding.ActivityLoginBinding
import com.project.appealic.ui.viewmodel.AuthViewModel

class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private lateinit var auth :FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: AuthViewModel

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        val data : Intent? = result.data
        val task =GoogleSignIn.getSignedInAccountFromIntent(data)
        if(task.isSuccessful){
            val account : GoogleSignInAccount? =task.result
            if(account!=null){
                viewModel.signInWithGoogle(account)
            }else{
                Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo ViewModel
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        // Khởi tạo GoogleSignInOptions
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.btnGoogle.setOnClickListener(){
            launcher.launch(googleSignInClient.signInIntent)
        }

        viewModel.signInSuccess.observe(this) { signInSuccess ->
            if (signInSuccess) {
                navigateToMainScreen()
            }
        }
        viewModel.logoutSuccess.observe(this) { logoutSuccess ->
            if (logoutSuccess) {
                // Xử lý sau khi đăng xuất thành công
            } else {
                // Xử lý khi đăng xuất thất bại
            }
        }
    }
    private fun navigateToMainScreen() {
        // Chuyển hướng đến màn hình chính hoặc màn hình tiếp theo sau khi đăng nhập thành công
        intent = Intent(this, Activity_welcome::class.java)
        startActivity(intent)
    }
}