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


class RenterLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_renter_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnRenterLogin = findViewById<Button>(R.id.renterLoginBtn)
        email = findViewById(R.id.enterEmailRenter)
        password = findViewById(R.id.enterPasswordRenter)
        val btnRenterSignUp = findViewById<Button>(R.id.renterSignUpBtn)
        auth = FirebaseAuth.getInstance()

        btnRenterSignUp.setOnClickListener{
            val intent = Intent(this,AccountSignUpPageOneRenter::class.java)
            startActivity(intent)
        }


    }
}