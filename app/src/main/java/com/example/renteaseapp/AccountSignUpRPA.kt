package com.example.renteaseapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AccountSignUpRPA : AppCompatActivity() {
    private lateinit var btnNext: Button
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_sign_up_rpa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accountSignUPRPA)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val regions = resources.getStringArray(R.array.region)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdownitem, regions)
        autoCompleteTextView.setAdapter(arrayAdapter)

        btnNext = findViewById(R.id.toMainMenu)
        btnNext.setOnClickListener{
            val intent = Intent(this,AccountSignUpSecondID::class.java)
            startActivity(intent)
        }
    }
}