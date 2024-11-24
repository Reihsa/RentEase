package com.example.renteaseapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class SSSidUpload : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var btnPickImage: Button
    private lateinit var btnNext: Button
    private var uploaded: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sssid_upload)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageView = findViewById(R.id.uploadedSSSfirst)
        btnPickImage = findViewById(R.id.selectFilesbtn2)
        btnNext = findViewById(R.id.aAccountSignUpRentingPageAccbtn)
        btnPickImage.setOnClickListener{
            pickImage()
            hasUploaded()
        }
        btnNext.setOnClickListener{
            val intent = Intent(this,AccountSignUpRPA::class.java)
            startActivity(intent)
        }
    }
    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    fun pickImage() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101) {
            if (requestCode == 101) {
                val uri = data?.data
                imageView.setImageURI(uri)
                uploaded = true
            }
        }
    }
    fun hasUploaded(){
        if (uploaded == true){
            btnNext.isVisible=true
        }
    }
}