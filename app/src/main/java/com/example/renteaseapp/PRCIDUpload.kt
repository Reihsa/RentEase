package com.example.renteaseapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class PRCIDUpload : AppCompatActivity() {
    private lateinit var btnPickImage: Button
    private lateinit var btnNext: Button
    private lateinit var imageUri: Uri
    private lateinit var btnRemImage1: Button
    private lateinit var btnRemImage2: Button
    private lateinit var imageName1 : TextView
    private lateinit var imageName2 : TextView
    private var isFirstImageUploaded: Boolean = false
    private var isSecondImageUploaded: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prcidupload)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageName1 = findViewById(R.id.dlFilesList1)
        imageName2 = findViewById(R.id.dlFilesList2)
        btnRemImage1 = findViewById(R.id.removeImage1)
        btnRemImage2 = findViewById(R.id.removeImage2)
        btnNext = findViewById(R.id.toThirdSignUpbtn)
        btnPickImage = findViewById(R.id.selectFilesbtn)
        imageUri = createImageUri()
        btnNext.setOnClickListener{
            val firstIDUploaded: Boolean = signupInfo.firstIDUploaded
            val secondIDUpload:Boolean = signupInfo.secondIDUploaded
            if(!firstIDUploaded){
                signupInfo.PRCID= true
                signupInfo.firstIDUploaded = true
                val intent = Intent(this,AccountSignUpSecondID::class.java)
                startActivity(intent)
            }else if(!secondIDUpload){
                signupInfo.PRCID= true
                signupInfo.secondIDUploaded = true
                val intent = Intent(this,AccountSignUpRPA::class.java)
                startActivity(intent)
            }
        }
        btnPickImage.setOnClickListener{
            imagePickerDialogue()
        }

        btnRemImage1.setOnClickListener{
            imageName1.text = "No pictures yet."
            isFirstImageUploaded = false
            btnRemImage1.visibility = View.INVISIBLE
            btnPickImage.isEnabled = true
            btnPickImage.isClickable = true
            btnNext.visibility = View.INVISIBLE
        }

        btnRemImage2.setOnClickListener{
            imageName2.text = "No pictures yet."
            isSecondImageUploaded = false
            btnRemImage2.visibility = View.INVISIBLE
            btnPickImage.isEnabled = true
            btnPickImage.isClickable = true
            btnNext.visibility = View.INVISIBLE
        }


    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if(uri != null){
            if(!isFirstImageUploaded){
                imageName1.text = "First Image is Uploaded!"
                isFirstImageUploaded = true
                btnRemImage1.visibility = View.VISIBLE
            }else if(!isSecondImageUploaded){
                imageName2.text = "Second Image is Uploaded!"
                isSecondImageUploaded = true
                btnRemImage2.visibility = View.VISIBLE
            }
            if(isFirstImageUploaded && isSecondImageUploaded){
                btnPickImage.isEnabled = false
                btnPickImage.isClickable = false
                btnNext.visibility = View.VISIBLE

            }
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (imageUri != null) {
            if (!isFirstImageUploaded) {
                imageName1.text = "First Image is Uploaded!"
                isFirstImageUploaded = true
                btnRemImage1.visibility = View.VISIBLE
            } else if (!isSecondImageUploaded) {
                imageName2.text = "Second Image is Uploaded!"
                isSecondImageUploaded = true
                btnRemImage2.visibility = View.VISIBLE
            }
            if(isFirstImageUploaded && isSecondImageUploaded){
                btnPickImage.isEnabled = false
                btnPickImage.isClickable = false
                btnNext.visibility = View.VISIBLE
            }
        }
    }

    private fun imagePickerDialogue(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.camera_gallery_picker)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCamera : Button = dialog.findViewById(R.id.btnCamera)
        val btnGallery : Button = dialog.findViewById(R.id.btnGallery)
        val btnCancel : Button = dialog.findViewById(R.id.btnCancelPicker)

        btnCamera.setOnClickListener{
            takePicture.launch(imageUri)
            dialog.dismiss()
        }

        btnGallery.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            dialog.dismiss()
        }

        btnCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createImageUri():Uri{
        val image = File(applicationContext.filesDir, "photo.png")
        return FileProvider.getUriForFile(applicationContext, "com.example.renteaseapp.fileprovider", image)

    }
}