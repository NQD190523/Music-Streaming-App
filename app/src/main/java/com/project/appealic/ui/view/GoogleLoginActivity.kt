package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import com.project.appealic.ui.viewmodel.ProfileViewModel
import com.project.appealic.ui.viewmodel.SpotifyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class  GoogleLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private lateinit var auth :FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel


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
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]


        auth = FirebaseAuth.getInstance()

        // Khởi tạo GoogleSignInOptions
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        // Đăng nhập bằng tài khoản google
        binding.btnGoogle.setOnClickListener(){
            CoroutineScope(Dispatchers.Main).launch {
                // Chuyển sang Dispatchers.IO để thực hiện các hoạt động I/O
                val signInAccount = withContext(Dispatchers.IO) {
                    googleSignInClient.signInIntent
                }
                launcher.launch(signInAccount)

                val applicationContext = binding.root.context.applicationContext

                // Sử dụng applicationContext khi gọi getLastSignedInAccount()
                val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(applicationContext)

                lastSignedInAccount?.let { account ->
                    auth.currentUser?.let { currentUser ->
                        profileViewModel.createUserProfileByGoogle(account, currentUser)
                    }
                }
            }
        }
        //Đăng nhập bằng Email
        binding.btnLogin.setOnClickListener() {
            val email = binding.txtLoginEmail.text.toString()
            val password = binding.txtLoginPassword.text.toString()
            viewModel.loginWithEmailAndPassword(email, password )
        }

        viewModel.signInSuccess.observe(this) { signInSuccess ->
            if (signInSuccess) {
                navigateToMainScreen()
            } else {
                Log.e("error", "incompleted")
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
        intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
    }
}