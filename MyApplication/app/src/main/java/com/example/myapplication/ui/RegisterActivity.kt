package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Find UI elements
        val loginLink = findViewById<TextView>(R.id.login_link)
        val registerButton = findViewById<Button>(R.id.register_button)
        val emailEditText = findViewById<EditText>(R.id.email_input)
        val passwordEditText = findViewById<EditText>(R.id.password_input)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirm_password_input)
        val sendOtpButton = findViewById<Button>(R.id.send_otp_button)
        val otpInput = findViewById<EditText>(R.id.otp_input)

        // Navigate back to MainActivity (Login Screen)
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Send OTP button (placeholder for future OTP implementation)
        sendOtpButton.setOnClickListener {
            Toast.makeText(this, "OTP functionality not implemented yet", Toast.LENGTH_SHORT).show()
            // TODO: Implement OTP sending logic (e.g., Firebase email verification or third-party SMS)
        }

        // Register button click
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            val otp = otpInput.text.toString().trim()

            if (email.isEmpty()) {
                emailEditText.error = "Email is required"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                confirmPasswordEditText.error = "Confirm password is required"
                confirmPasswordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordEditText.error = "Passwords do not match"
                confirmPasswordEditText.requestFocus()
                confirmPasswordEditText.text.clear()
                return@setOnClickListener
            }

            // Optional: Skip OTP check for now (add OTP verification logic later if needed)
            if (otp.isEmpty()) {
                otpInput.error = "OTP is required"
                otpInput.requestFocus()
                return@setOnClickListener
            }

            // Firebase Authentication: Create User
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("RegisterActivity", "createUserWithEmail:success")
                        val user = auth.currentUser
                        val db = Firebase.firestore

                        // Get FCM token
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
                            val fcmToken = if (tokenTask.isSuccessful) tokenTask.result else ""

                            // Create user document in Firestore
                            val userDoc = hashMapOf(
                                "email" to email,
                                "contacts" to arrayListOf<String>(), // Empty contacts array
                                "fcmToken" to fcmToken // Save token here or update on login
                            )

                            user?.uid?.let { uid ->
                                db.collection("users").document(uid).set(userDoc)
                                    .addOnSuccessListener {
                                        Log.d("RegisterActivity", "User document created for UID: $uid")
                                        Toast.makeText(
                                            this,
                                            "Registration successful",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Navigate to MainActivity (Login screen)
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("RegisterActivity", "Firestore error: ${e.message}", e)
                                        Toast.makeText(
                                            this,
                                            "Registration successful, but failed to save user data: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        // Proceed to login screen
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                            } ?: run {
                                Log.e("RegisterActivity", "User is null after successful auth task")
                                Toast.makeText(
                                    this,
                                    "Registration successful, but couldn't retrieve user details",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this,
                            "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}