package com.project.appealic.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.project.appealic.MainActivity
import com.project.appealic.R
import com.project.appealic.databinding.ActivityLoginPhoneBinding
import com.project.appealic.ui.viewmodel.AuthViewModel
import com.project.appealic.ui.viewmodel.SpotifyViewModel
import com.project.appealic.utils.ValidationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ActivityLoginPhone : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPhoneBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: AuthViewModel
    private lateinit var verificationId: String
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

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
        binding = ActivityLoginPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Toast.makeText(this@ActivityLoginPhone, "Verification failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationId, token)
                this@ActivityLoginPhone.verificationId = verificationId
                // Navigate to the OTP verification screen
                val intent = Intent(this@ActivityLoginPhone, ActivityLoginPhoneOTP::class.java)
                intent.putExtra("verificationId", verificationId)
                startActivity(intent)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnGoogle.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                launcher.launch(googleSignInClient.signInIntent)
            }
        }

        binding.btnLogin.setOnClickListener {
            val phone = binding.txtLoginPhone.text.toString()

            if (ValidationUtils.isValidPhoneNumber(phone) == ValidationUtils.VALID) {
                sendVerificationCodeToUser(phone)
            } else {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationCodeToUser(phoneNo: String) {
        val phoneNumber = formatPhoneNumber(phoneNo)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            callbacks
        )
    }

    private fun formatPhoneNumber(phoneNo: String): String {
        val digitsOnly = phoneNo.replace("\\D".toRegex(), "")
        return if (digitsOnly.startsWith("0")) {
            "+84" + digitsOnly.substring(1)
        } else {
            "+84$digitsOnly"
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("SignIn", "signInWithCredential:success")
                    // Navigate to the main screen after successful sign-in
                    navigateToMainScreen()
                } else {
                    Log.w("SignIn", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}