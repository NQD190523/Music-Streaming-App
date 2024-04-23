package com.project.appealic.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String


    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.btnSignUp)
        signUpButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.txtxRegisterEmail).text.toString()
            val password = findViewById<EditText>(R.id.txtRegisterPassword).text.toString()
            val confirmedPassword = findViewById<EditText>(R.id.txtRegisterCfpassword).text.toString()

            val isValidEmail = ValidationUtils.isValidEmail(email)
            authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
            auth = FirebaseAuth.getInstance()
            profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

            if (isValidEmail != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidEmail) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Vui lòng nhập Email"
                    ValidationUtils.EMAIL_MISMATCH_ERROR -> errorMessage = "Nhập đúng định dang Email"
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
                    ValidationUtils.PASSWORD_TYPE_ERROR -> errorMessage = "Mật khẩu phải có cả chữ và số"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValidConfirmedPassword = ValidationUtils.checkConfirmPassword(password, confirmedPassword)

            if (isValidConfirmedPassword != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidConfirmedPassword) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Vui lòng nhập xác nhận mật khẩu"
                    ValidationUtils.PASSWORD_MISMATCH_ERROR -> errorMessage = "Xác nhận mật khẩu không đúng"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Đăng ký tài khoản mới
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Đăng ký thành công
                        val user = auth.currentUser
                        user?.let {
                            profileViewModel.createUserProfile(user, email)
                        }

                        // Chuyển sang màn hình đăng nhập
                        val intent = Intent(this, GoogleLoginActivity::class.java)
                        startActivity(intent)
                        finish() // Đóng màn hình đăng ký
                    } else {
                        // Đăng ký thất bại
                        Toast.makeText(
                            this@ActivityRegister,
                            "Đăng ký không thành công: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        val txtPassword: EditText = findViewById(R.id.txtRegisterPassword)
        val imgShowPass: ImageView = findViewById(R.id.imgShowpass)

        imgShowPass.setOnClickListener {
            if (txtPassword.transformationMethod is PasswordTransformationMethod) {
                txtPassword.transformationMethod = null
                imgShowPass.setImageResource(R.drawable.ic_show_pass)
            } else {
                txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imgShowPass.setImageResource(R.drawable.ic_hirre_password)
            }
        }

        val txtPassword2: EditText = findViewById(R.id.txtRegisterCfpassword)
        val imgShowPass2: ImageView = findViewById(R.id.imgShowpass2)

        imgShowPass2.setOnClickListener {
            if (txtPassword2.transformationMethod is PasswordTransformationMethod) {
                txtPassword2.transformationMethod = null
                imgShowPass2.setImageResource(R.drawable.ic_show_pass)
            } else {
                txtPassword2.transformationMethod = PasswordTransformationMethod.getInstance()
                imgShowPass2.setImageResource(R.drawable.ic_hirre_password)
            }
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
