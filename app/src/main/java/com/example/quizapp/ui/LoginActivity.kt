package com.example.quizapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.R
import com.example.quizapp.utils.AuthHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var authHelper: AuthHelper
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogleSignIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var tvForgotPassword: TextView
    private lateinit var progressBar: ProgressBar

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            handleGoogleSignIn(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authHelper = AuthHelper(this)
        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnRegister)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
        tvSignUp = findViewById(R.id.tvLogin)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener { handleEmailLogin() }
        btnGoogleSignIn.setOnClickListener { launchGoogleSignIn() }
        tvSignUp.setOnClickListener { navigateToSignUp() }
        tvForgotPassword.setOnClickListener { navigateToForgotPassword() }
    }

    private fun handleEmailLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        progressBar.visibility = View.VISIBLE
        authHelper.loginWithEmail(
            email = email,
            password = password,
            onSuccess = {
                progressBar.visibility = View.GONE
                navigateToMain()
            },
            onFailure = { error ->
                progressBar.visibility = View.GONE
                showError(error)
            }
        )
    }

    private fun launchGoogleSignIn() {
        val signInIntent = authHelper.getGoogleSignInIntent()
        googleSignInLauncher.launch(signInIntent)
        progressBar.visibility = View.VISIBLE
    }

    private fun handleGoogleSignIn(result: androidx.activity.result.ActivityResult) {
        authHelper.handleGoogleSignInResult(
            data = result.data,
            onSuccess = {
                progressBar.visibility = View.GONE
                navigateToMain()
            },
            onFailure = { error ->
                progressBar.visibility = View.GONE
                showError(error)
            }
        )
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun navigateToForgotPassword() {
        startActivity(Intent(this, ForgetPasswordActivity::class.java))
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}