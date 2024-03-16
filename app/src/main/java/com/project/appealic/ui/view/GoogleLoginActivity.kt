package com.project.appealic.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.appealic.Activity_welcome
import com.project.appealic.R
import com.project.appealic.databinding.ActivityLoginBinding
import com.project.appealic.ui.viewmodel.AuthViewModel

class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private lateinit var auth :FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: AuthViewModel

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        val task =GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if(task.isSuccessful){
            val account : GoogleSignInAccount? =task.result
            if(account!=null){
                updateUI(account)
            }else{
                Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this,Activity_welcome::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.btnGoogle.setOnClickListener(){
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        fun logOut(){

        }
    }
}