package com.project.appealic.ui.view
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.appealic.databinding.ActivityFotgotpassBinding

class ActivityForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityFotgotpassBinding
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFotgotpassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgetPassword.setOnClickListener {
            val email = binding.edtemail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter your email address", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(applicationContext, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            } else {
                // Code to handle valid email
                Toast.makeText(applicationContext, "Email sent successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

