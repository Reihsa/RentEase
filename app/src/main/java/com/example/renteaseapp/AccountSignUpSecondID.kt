package com.example.renteaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AccountSignUpSecondID : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_sign_up_second_id)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accountSignUpSecondID)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnOwnerLogin = findViewById<Button>(R.id.sssIDbtn)
        btnOwnerLogin.setOnClickListener{
            val intent = Intent(this,SSSidUpload::class.java)
            startActivity(intent)
        }
    }
}