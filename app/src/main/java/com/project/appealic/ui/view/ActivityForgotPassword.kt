package com.project.appealic.ui.view
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.databinding.ActivityFotgotpassBinding

class ActivityForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityFotgotpassBinding
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFotgotpassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()

        binding.btnForgetPassword.setOnClickListener {
            val email = binding.edtemail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter your email address", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(applicationContext, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Email sent successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ActivityForgotpassOtp::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
