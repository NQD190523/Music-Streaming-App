package com.project.appealic.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.project.appealic.R
import com.project.appealic.utils.ValidationUtils

class ActivityRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.btnSignUp)
        signUpButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.txtxRegisterEmail).text.toString()
            val password = findViewById<EditText>(R.id.txtRegisterPassword).text.toString()
            val confirmedPassword = findViewById<EditText>(R.id.txtRegisterCfpassword).text.toString()

            val isValidEmail = ValidationUtils.isValidEmail(email)

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