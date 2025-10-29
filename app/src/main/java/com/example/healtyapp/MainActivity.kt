package com.example.healtyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.healtyapp.ui.login.LoginState
import com.example.healtyapp.ui.login.LoginViewModel
import com.example.healtyapp.ui.patients.PatientsActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPass = findViewById<EditText>(R.id.etPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnLogin.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()
            loginViewModel.login(user, pass)
        }

        lifecycleScope.launch {
            loginViewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Success -> {
                        tvResult.text = "Login successful!"
                        startActivity(Intent(this@MainActivity, PatientsActivity::class.java))
                        finish()
                    }
                    is LoginState.Error -> {
                        tvResult.text = state.message
                        Toast.makeText(applicationContext, state.message, Toast.LENGTH_LONG).show()
                    }
                    is LoginState.Loading -> {
                        tvResult.text = "Loading..."
                    }
                    else -> Unit
                }
            }
        }
    }
}
