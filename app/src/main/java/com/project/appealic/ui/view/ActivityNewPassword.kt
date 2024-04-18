package com.project.appealic.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.databinding.ActivityFotgotpassBinding
import com.project.appealic.databinding.ActivityNewpassBinding

class ActivityNewPassword : AppCompatActivity() {
    private lateinit var binding: ActivityNewpassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewpassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveNewPass.setOnClickListener {
            val newPassword = binding.edtNewPassword.text.toString().trim()
            val confirmPassword = binding.edtCfPassword.text.toString().trim()

            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Password cannot be left blank: Please enter a password. This field cannot be left blank.", Toast.LENGTH_LONG).show()
            } else {
                val passwordValidationResult = validatePassword(newPassword)
                if (passwordValidationResult.isNotEmpty()) {
                    Toast.makeText(this, passwordValidationResult, Toast.LENGTH_LONG).show()
                } else if (confirmPassword.isEmpty()) {
                    Toast.makeText(this, "Confirm password cannot be left blank: Please enter a confirm password. This field cannot be left blank.", Toast.LENGTH_LONG).show()
                } else if (newPassword != confirmPassword) {
                    Toast.makeText(this, "Passwords do not match: Please ensure both passwords match.", Toast.LENGTH_LONG).show()
                } else {
                    // Code to update the new password
                    // This is where you would typically call your backend service to update the password
                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validatePassword(password: String): String {
        val messages = mutableListOf<String>()
        if (password.length !in 8..20) {
            messages.add("Password must be between 8 and 20 characters.")
        }
        if (!password.matches(Regex(".*\\d.*"))) {
            messages.add("Password must contain at least one numeric character (0-9).")
        }
        if (!password.matches(Regex(".*[a-z].*"))) {
            messages.add("Password must contain at least one lowercase character (a-z).")
        }
        if (!password.matches(Regex(".*[A-Z].*"))) {
            messages.add("Password must contain at least one uppercase character (A-Z).")
        }
        if (!password.matches(Regex(".*[!@#\\$%\\^&\\*\\(\\)_\\-\\+=\\{\\}\\\\\\[\\]\\|:;\"'<,>.?/].*"))) {
            messages.add("Password must contain at least one special character (such as !, @, #, $, etc.).")
        }
        return messages.joinToString("\n")
    }
}