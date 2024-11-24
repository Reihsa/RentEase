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

        imageView = findViewById(R.id.uploadedDLfirst)
        btnPickImage = findViewById(R.id.selectFilesbtn)
        btnNext = findViewById(R.id.toThirdSignUpbtn)
        fileList = findViewById(R.id.dlFilesList)
        btnPickImage.setOnClickListener{
            pickImage()
            hasUploaded()
        }
        btnNext.setOnClickListener{
            val intent = Intent(this,AccountSignUpSecondID::class.java)
            startActivity(intent)
        }
    }
    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    fun pickImage() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        startActivityForResult(intent, 101)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101) {
            if (requestCode == 101) {
                val uri = data?.data
                imageView.setImageURI(uri)
                fileList.text = uri.toString()
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