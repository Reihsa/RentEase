package com.example.renteaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

class ownerLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_owner_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnOwnerLogin = findViewById<Button>(R.id.ownerLoginBtn)
        email = findViewById(R.id.enterEmailOwner)
        password = findViewById(R.id.enterPasswordOwner)
        val btnOwnerSignUp = findViewById<Button>(R.id.ownerSignUpBtn)
        auth = FirebaseAuth.getInstance()
        btnOwnerSignUp.setOnClickListener{
            val intent = Intent(this,AccountSignUpPageOne::class.java)
            startActivity(intent)
        }

        btnOwnerLogin.setOnClickListener{
            val email = email.text.toString().trim()
            val password = password.text.toString().trim()

            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                // Perform login
                loginUser(email, password)
            }
        }
    }
    private fun loginUser(email: String, password: String) {
        // Firebase Authentication login
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to the next screen
                    val user = auth.currentUser
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // Navigate to another activity after login
                    val intent = Intent(this, MainAppPage::class.java)
                    startActivity(intent)
                    finish()  // Close the login activity
                } else {
                    // Login failed
                    Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
                }
            }
    }
}