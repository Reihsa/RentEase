package com.example.renteaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AccountSignUpFirstID : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_sign_up_first_id)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accountSignUpFirstID)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnOwnerLogin = findViewById<Button>(R.id.dlIDbtn)
        btnOwnerLogin.setOnClickListener{
            val intent = Intent(this,DriverLicenseUpload::class.java)
            startActivity(intent)
        }
    }
}