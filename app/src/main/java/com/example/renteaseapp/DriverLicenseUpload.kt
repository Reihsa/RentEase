package com.example.renteaseapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DriverLicenseUpload : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var btnPickImage: Button
    private lateinit var fileList: TextView
    private lateinit var btnNext: Button
    private var uploaded: Boolean = false
    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_license_upload)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.driverLicenseUpload)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnNext = findViewById(R.id.toThirdSignUpbtn)
        btnNext.setOnClickListener{
            val intent = Intent(this,AccountSignUpSecondID::class.java)
            startActivity(intent)
        }
    }
}