package com.example.quizapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.R
import com.example.quizapp.utils.AuthHelper

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var authHelper: AuthHelper
    private lateinit var etEmail: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var tvBackToLogin: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        authHelper = AuthHelper(this)
        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupClickListeners() {
        btnResetPassword.setOnClickListener {
            sendPasswordResetEmail()
        }

        tvBackToLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun sendPasswordResetEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnResetPassword.isEnabled = false

        authHelper.sendPasswordResetEmail(
            email = email,
            onSuccess = {
                progressBar.visibility = View.GONE
                btnResetPassword.isEnabled = true
                Toast.makeText(
                    this,
                    "Password reset link sent to $email",
                    Toast.LENGTH_LONG
                ).show()
                navigateToLogin()
            },
            onFailure = { error ->
                progressBar.visibility = View.GONE
                btnResetPassword.isEnabled = true
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}