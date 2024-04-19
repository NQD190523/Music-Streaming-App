package com.project.appealic.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.Log
import com.project.appealic.R
import com.project.appealic.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện khi Forgot Password được nhấn
        binding.btnForgetPassword.setOnClickListener {
            val intent = Intent(this, ActivityForgotPassword::class.java)
            startActivity(intent)
        }

        val txtPassword: EditText = findViewById(R.id.txtLoginPassword)
        val imgShowPass: ImageView = findViewById(R.id.imgShowpass)

        imgShowPass.setOnClickListener {
            Log.d("PasswordToggle", "ImageView clicked")
            if (txtPassword.transformationMethod is PasswordTransformationMethod) {
                Log.d("PasswordToggle", "Password hidden, showing it now")
                txtPassword.transformationMethod = null
                imgShowPass.setImageResource(R.drawable.ic_show_pass)
            } else {
                Log.d("PasswordToggle", "Password shown, hiding it now")
                txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imgShowPass.setImageResource(R.drawable.ic_hirre_password)
            }
        }
        val edtUsername: EditText = findViewById(R.id.txtLoginEmail)
        val edtPassword: EditText = findViewById(R.id.txtLoginPassword)
        val cbRememberMe: CheckBox = findViewById(R.id.cbRememberMe)

        val sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
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

    }
}
