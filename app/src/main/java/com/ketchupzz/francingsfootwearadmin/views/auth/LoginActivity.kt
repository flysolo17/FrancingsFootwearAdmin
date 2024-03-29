package com.ketchupzz.francingsfootwearadmin.views.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ketchupzz.francingsfootwearadmin.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.utils.UiState
import com.ketchupzz.francingsfootwearadmin.MainActivity
import com.ketchupzz.francingsfootwearadmin.databinding.ActivityLoginBinding
import com.ketchupzz.francingsfootwearadmin.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var loadingDialog : LoadingDialog
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(binding.root.context)
        setContentView(binding.root)
        binding.buttonLogin.setOnClickListener {
            val  email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            if (email.isEmpty()) {
                binding.layoutEmail.error ="enter email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.layoutPassword.error = "enter password"
                return@setOnClickListener
            }
            login(email,password)
        }

        binding.buttonForgotPassword.setOnClickListener {
            val fragment = ForgotPasswordFragment()
            if (!fragment.isAdded) {
                fragment.show(supportFragmentManager,"Forgot password")
            }
        }
    }

    private fun login(email :String,password  : String) {
        authViewModel.login(email,password) {
            when(it) {
                is UiState.FAILED -> {


                }
                is UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {

                    Toast.makeText(binding.root.context,"Successfully Logged in", Toast.LENGTH_SHORT).show()
                    updateUI(it.data)

                }
            }
        }
    }

    private fun updateUI(user : FirebaseUser) {
        authViewModel.setCurrentUser(user)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            updateUI(user)
        }
    }
}