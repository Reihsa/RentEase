package com.example.renteaseapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Region
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class AccountSignUpRPA : AppCompatActivity() {
    private lateinit var btnNext: Button
    private lateinit var pageName: EditText
    private lateinit var pageDescription: EditText
    private lateinit var region: AutoCompleteTextView
    private lateinit var imageUri: Uri
    private lateinit var imageName: TextView
    private lateinit var btnPickImage: Button
    private var isImageUploaded: Boolean = false
    private lateinit var btnRemImage: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_sign_up_rpa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accountSignUPRPA)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnPickImage = findViewById(R.id.btnaddprofilephoto)
        region = findViewById(R.id.Regiontb)
        val regions = resources.getStringArray(R.array.region)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdownitem, regions)
        region.setAdapter(arrayAdapter)
        pageName = findViewById(R.id.pageNametxt)
        pageDescription = findViewById(R.id.pageDescriptionmtb)
        imageName = findViewById(R.id.profileimagename)
        btnRemImage = findViewById(R.id.removeImage)
        btnPickImage = findViewById(R.id.btnaddprofilephoto)
        imageUri = createImageUri()
        btnNext = findViewById(R.id.toReviewinfo)

        btnNext.setOnClickListener {
            signupInfo.Spagename = pageName.text.toString()
            signupInfo.Sregion = region.text.toString()
            signupInfo.Sdescription = pageDescription.text.toString()
            val intent = Intent(this, signUpReviewInfo::class.java)
            startActivity(intent)
            finish()
        }

        btnRemImage.setOnClickListener {
            imageName.text = ""
            isImageUploaded = false
            btnRemImage.visibility = View.INVISIBLE
            btnPickImage.isEnabled = true
            btnPickImage.isClickable = true
            btnPickImage.visibility = View.VISIBLE
            checkAllFieldsFilled()
        }

        btnPickImage.setOnClickListener {
            imagePickerDialogue()
        }

        pageName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

        region.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })
    }
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if(uri != null){
            if(!isImageUploaded){
                imageName.text = "Image is Uploaded!"
                isImageUploaded = true
                btnRemImage.visibility = View.VISIBLE
                btnPickImage.visibility = View.INVISIBLE
                checkAllFieldsFilled()

            }
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (imageUri != null) {
            if (!isImageUploaded) {
                imageName.text = "First Image is Uploaded!"
                isImageUploaded = true
                btnRemImage.visibility = View.VISIBLE
                btnPickImage.visibility = View.INVISIBLE
                checkAllFieldsFilled()

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

    private fun createImageUri(): Uri {
        val image = File(applicationContext.filesDir, "photo.png")
        return FileProvider.getUriForFile(applicationContext, "com.example.renteaseapp.fileprovider", image)

    }

    private fun checkAllFieldsFilled() {
        val allFieldsFilled = pageName.text.isNotEmpty() &&
                region.text.isNotEmpty() && isImageUploaded


        // Enable the next button if all fields are filled, otherwise disable it
        if(allFieldsFilled){
            btnNext.visibility = View.VISIBLE
        }else{
            btnNext.visibility = View.INVISIBLE
        }
    }
}