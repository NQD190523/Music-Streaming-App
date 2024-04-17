package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.project.appealic.ui.viewmodel.SpotifyViewModel
import com.project.appealic.utils.ValidationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private lateinit var auth :FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: AuthViewModel

    private val spotifyViewModel: SpotifyViewModel by viewModels()


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

        // Đăng nhập bằng tài khoản google
        binding.btnGoogle.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                launcher.launch(googleSignInClient.signInIntent)
            }
        }
        // Đăng nhập bằng Email
        binding.btnLogin.setOnClickListener() {
            val email = binding.txtLoginEmail.text.toString()
            val password = binding.txtLoginPassword.text.toString()

            val isValidEmail = ValidationUtils.isValidEmail(email)

            if (isValidEmail != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidEmail) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Vui lòng nhập Email"
                    ValidationUtils.EMAIL_MISMATCH_ERROR -> errorMessage = "Vui lòng nhập đúng định dạng Email"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValidPassword = ValidationUtils.isValidPassword(password)

            if (isValidPassword != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidPassword) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Vui lòng nhập mật khẩu"
                    ValidationUtils.PASSWORD_LENGTH_ERROR -> errorMessage = "Vui lòng nhập ít nhất 8 ký tự"
                    ValidationUtils.PASSWORD_TYPE_ERROR -> errorMessage = "Mật khẩu phải bao gồm cả chữ và số"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.loginWithEmailAndPassword(email, password)
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

        binding.btnForgetPassword.setOnClickListener {
            // Future add forgot password intent
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }

        binding.btnPhone.setOnClickListener {
            val intent = Intent(this, ActivityLoginPhone::class.java)
            startActivity(intent)
        }
    }
    private fun navigateToMainScreen() {
        // Chuyển hướng đến màn hình chính hoặc màn hình tiếp theo sau khi đăng nhập thành công
        intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
    }
}