package com.project.appealic.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.ui.viewmodel.AuthViewModel
import com.project.appealic.ui.viewmodel.ProfileViewModel
import com.project.appealic.utils.ValidationUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ActivityRegister : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.btnSignUp)
        signUpButton.setOnClickListener {
            email = findViewById<EditText>(R.id.txtxRegisterEmail).text.toString()
            password = findViewById<EditText>(R.id.txtRegisterPassword).text.toString()
            val confirmedPassword =
                findViewById<EditText>(R.id.txtRegisterCfpassword).text.toString()

            val isValidEmail = ValidationUtils.isValidEmail(email)
            authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
            auth = FirebaseAuth.getInstance()
            profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

            if (isValidEmail != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidEmail) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Please enter your email address"
                    ValidationUtils.EMAIL_MISMATCH_ERROR -> errorMessage =
                        "Please enter a valid email address"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValidPassword = ValidationUtils.isValidPassword(password)

            if (isValidPassword != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidPassword) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Please enter your password"
                    ValidationUtils.PASSWORD_LENGTH_ERROR -> errorMessage =
                        "The minimum password allowance is 8 characters"

                    ValidationUtils.PASSWORD_TYPE_ERROR -> errorMessage =
                        "Password must contain both numbers and characters"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValidConfirmedPassword =
                ValidationUtils.checkConfirmPassword(password, confirmedPassword)

            if (isValidConfirmedPassword != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidConfirmedPassword) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Please enter your confirmation password"
                    ValidationUtils.PASSWORD_MISMATCH_ERROR -> errorMessage =
                        "Check your password again"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
                try {
                    authViewModel.signInWithEmailAndPassword(email, password)
                    // Đăng nhập thành công
                    val user = auth.currentUser
                    user?.let {
                        // Tạo dữ liệu người dùng trên Firestore
                        profileViewModel.createUserProfile(user, email)
                    }
                } catch (exception: Exception) {
                    // Xử lý khi đăng nhập thất bại
                    Toast.makeText(this@ActivityRegister, "Đăng ký không thành công: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            Toast.makeText(this, "Pass validation, progress to sign up", Toast.LENGTH_SHORT).show()

        }

        val signInPhoneButton = findViewById<Button>(R.id.btnUsePhone)
        signInPhoneButton.setOnClickListener {
            val intent = Intent(this, ActivityLoginPhone::class.java)
            startActivity(intent)
        }

        val signInButton = findViewById<Button>(R.id.btnSignin)
        signInButton.setOnClickListener {
            val intent = Intent(this, GoogleLoginActivity::class.java)
            startActivity(intent)
        }
    }
}