package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R // Add this line

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Find the Login Link
        val loginLink = findViewById<TextView>(R.id.login_link)

        // Navigate back to MainActivity
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Optional: Register button placeholder
        val registerButton = findViewById<Button>(R.id.register_button)
        registerButton.setOnClickListener {
            // Add registration logic later
        }
    }
}