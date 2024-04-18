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

    private lateinit var binding : ActivityLoginPhoneBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: AuthViewModel
    private lateinit var verificationId: String
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        val data : Intent? = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (task.isSuccessful) {
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                viewModel.signInWithGoogle(account)
            } else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val intent = Intent(this@ActivityLoginPhone, ActivityLoginPhoneOTP::class.java)
                intent.putExtra("verificationId", verificationId)
                startActivity(intent)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@ActivityLoginPhone, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationId = p0
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.btnGoogle.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                launcher.launch(googleSignInClient.signInIntent)
            }
        }

        binding.btnLogin.setOnClickListener {
            val phone = binding.txtLoginPhone.text.toString()

            val isValidPhoneNumber = ValidationUtils.isValidPhoneNumber(phone)

            if (isValidPhoneNumber != ValidationUtils.VALID) {
                var errorMessage: String = ""

                when (isValidPhoneNumber) {
                    ValidationUtils.EMPTY_ERROR -> errorMessage = "Vui lòng nhập SĐT"
                    ValidationUtils.PHONE_MISMATCH_ERROR -> errorMessage = "Vui lòng nhập SĐT hợp lệ"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            Toast.makeText(this, "Phone correct", Toast.LENGTH_SHORT).show()

            sendVerificationCodeToUser(phone)
        }

        viewModel.signInSuccess.observe(this) { signInSuccess ->
            if (signInSuccess) {
                navigateToMainScreen()
            } else {
                // Handle sign-in failure
                Log.e("error", "incompleted")
            }
        }

        viewModel.logoutSuccess.observe(this) { logoutSuccess ->
            if (logoutSuccess) {
                // Handle logout success
            } else {
                // Handle logout failure
            }
        }

        binding.btnEmail.setOnClickListener {
            val intent = Intent(this, GoogleLoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }
    }

    private fun sendVerificationCodeToUser(phoneNo: String) {
        val formattedPhoneNumber = formatPhoneNumber(phoneNo)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            formattedPhoneNumber,
            20,
            TimeUnit.SECONDS, // Timeout duration
            this,
            callbacks
        )
    }

    private fun formatPhoneNumber(phoneNo: String): String {
        val digitsOnly = phoneNo.replace("\\D".toRegex(), "")
        val formattedNumber = if (digitsOnly.startsWith("0")) {
            digitsOnly.substring(1)
        } else {
            digitsOnly
        }
        return if (!formattedNumber.startsWith("+84")) {
            "+84$formattedNumber"
        } else {
            formattedNumber
        }
    }

    private fun navigateToMainScreen() {
        // Navigate to the main screen or the next screen after successful login
        intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
    }
}