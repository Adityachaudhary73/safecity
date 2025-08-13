package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the Register Link
        val registerLink = findViewById<TextView>(R.id.register_link)

        // Navigate to RegisterActivity
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Optional: Login button placeholder
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            // Add login logic later (e.g., validate email/password)
        }
    }
}