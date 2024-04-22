package com.project.appealic.ui.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
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
import com.project.appealic.utils.ValidationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class GoogleLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                viewModel.signInWithGoogle(account)
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
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
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val sharedPrefForLogin = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefForLogin.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Nếu đã đăng nhập, chuyển sang ActivityHome
            navigateToMainScreen()
        } else {
            // Nếu chưa đăng nhập, hiển thị màn hình đăng nhập
            // ...
        }

        // Đăng nhập bằng tài khoản google
        binding.btnGoogle.setOnClickListener() {
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

        // Ẩn hiện mật khẩu
        val txtPassword: EditText = binding.txtLoginPassword
        val imgShowPass: ImageView = binding.imgShowpass

        imgShowPass.setOnClickListener {
            if (txtPassword.transformationMethod is PasswordTransformationMethod) {
                txtPassword.transformationMethod = null
                imgShowPass.setImageResource(R.drawable.ic_show_pass)
            } else {
                txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imgShowPass.setImageResource(R.drawable.ic_hirre_password)
            }
        }

        // Ghi nhớ mật khẩu
        val edtUsername: EditText = binding.txtLoginEmail
        val edtPassword: EditText = binding.txtLoginPassword
        val cbRememberMe: CheckBox = binding.cbRememberMe

        val sharedPreferences: SharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Kiểm tra xem người dùng có yêu cầu nhớ mật khẩu không
        if (sharedPreferences.getBoolean("rememberPassword", false)) {
            edtUsername.setText(sharedPreferences.getString("username", ""))
            edtPassword.setText(sharedPreferences.getString("password", ""))
            cbRememberMe.isChecked = true
        }

        cbRememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Lưu tên đăng nhập và mật khẩu
                editor.putBoolean("rememberPassword", true)
                editor.putString("username", edtUsername.text.toString())
                editor.putString("password", edtPassword.text.toString())
                editor.apply()
            } else {
                // Xóa tên đăng nhập và mật khẩu đã lưu
                editor.clear()
                editor.apply()
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
                    ValidationUtils.EMAIL_MISMATCH_ERROR -> errorMessage =
                        "Vui lòng nhập đúng định dạng Email"
                }

                showToast(errorMessage, android.R.color.holo_red_light)
                return@setOnClickListener
            }

            val isValidPassword = ValidationUtils.isValidPassword(password)

            if (isValidPassword != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidPassword) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Vui lòng nhập mật khẩu"
                    ValidationUtils.PASSWORD_LENGTH_ERROR -> errorMessage =
                        "Vui lòng nhập ít nhất 8 ký tự"

                    ValidationUtils.PASSWORD_TYPE_ERROR -> errorMessage =
                        "Mật khẩu phải bao gồm cả chữ và số"
                }

                showToast(errorMessage, android.R.color.holo_red_light)
                return@setOnClickListener
            }
            viewModel.loginWithEmailAndPassword(email, password)
        }

        viewModel.signInSuccess.observe(this) { signInSuccess ->
            if (signInSuccess) {
                showToast("Đăng nhập thành công", android.R.color.holo_green_light)

                // Lưu trạng thái đăng nhập vào SharedPreferences
                val sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                navigateToMainScreen()
            } else {
                showToast("Đăng nhập thất bại", android.R.color.holo_red_light)
                Log.e("error", "incompleted")
            }
        }

        viewModel.logoutSuccess.observe(this) { logoutSuccess ->
            if (logoutSuccess) {
                // Xóa trạng thái đăng nhập khỏi SharedPreferences
                val sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", false)
                editor.apply()

                // Xử lý sau khi đăng xuất thành công
            } else {
                // Xử lý khi đăng xuất thất bại
            }
        }


        binding.btnForgetPassword.setOnClickListener {
            val intent = Intent(this, ActivityForgotPassword::class.java)
            startActivity(intent)
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
        finish() // Đóng màn hình đăng nhập
    }

    private fun showToast(message: String, colorResId: Int) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        val view = toast.view
        view?.setBackgroundResource(colorResId)
        toast.show()
    }
}
