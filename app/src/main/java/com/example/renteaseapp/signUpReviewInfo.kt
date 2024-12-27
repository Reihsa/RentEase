package com.example.renteaseapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import android.util.Log
import com.example.renteaseapp.signupInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.firestore.toObject
import com.google.firebase.Firebase
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class signUpReviewInfo : AppCompatActivity() {
    private lateinit var btnNext: Button
    private lateinit var realName: TextView
    private lateinit var userName: TextView
    private lateinit var emailAddress: TextView
    private lateinit var contactNumber: TextView
    private lateinit var pageAccount: TextView
    private lateinit var ta1CB: CheckBox
    private lateinit var ta2CB: CheckBox
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_review_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        realName = findViewById(R.id.reviewName)
        userName = findViewById(R.id.reviewUsername)
        emailAddress = findViewById(R.id.reviewEmailAddress)
        contactNumber = findViewById(R.id.reviewContactNumber)
        pageAccount = findViewById(R.id.reviewPageAccount)
        btnNext = findViewById(R.id.toLogin)
        ta1CB = findViewById(R.id.cbTerm1)
        ta2CB = findViewById(R.id.cbTerm2)
        realName.text = signupInfo.SrealName
        userName.text = signupInfo.Susername
        emailAddress.text = signupInfo.Semail
        contactNumber.text = signupInfo.ScontractNo
        pageAccount.text = signupInfo.Spagename

        btnNext.setOnClickListener {
            createAccount()
        }
        ta1CB.setOnCheckedChangeListener { _, _ ->
            checkAllFieldsFilled()
        }

        ta2CB.setOnCheckedChangeListener { _, _ ->
            checkAllFieldsFilled()
        }
    }

    private fun checkAllFieldsFilled() {
        val allFieldsFilled = ta1CB.isChecked && ta2CB.isChecked

        // Enable the next button if all fields are filled, otherwise disable it
        if(allFieldsFilled){
            btnNext.visibility = View.VISIBLE
        }else{
            btnNext.visibility = View.INVISIBLE
        }
    }

    private fun createAccount() {
        val db = Firebase.firestore
        // Gather data from the singleton
        val email = signupInfo.Semail
        val password = signupInfo.Spassword
        val name = signupInfo.SrealName
        val username = signupInfo.Susername
        val contractNo = signupInfo.ScontractNo
        val pageName = signupInfo.Spagename
        val region = signupInfo.Sregion
        val pagePicture = "url_to_page_picture" // Default or selected URL for page picture
        val profilePicture = "url_to_profile_picture" // Default or selected URL for profile picture
        val description = signupInfo.Sdescription // Example description, replace it with user input

        // Check if the required fields are filled
        if (email != null && password != null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User created successfully
                        val userID = auth.currentUser?.uid// Get unique ownerId (Firebase UID)

                        // Check if userID is not null
                        if (userID != null) {
                            // Prepare owner data to store in Firestore
                            val userData = hashMapOf(
                                "accountType" to "owner",
                                "emailaddress" to email,
                                "name" to name,
                                "username" to username,
                                "profilePicture" to profilePicture,
                                "contactNumber" to contractNo,
                                "password" to password
                            )

                            // Prepare page data to store in Firestore
                            val pageData = hashMapOf(
                                "pageName" to pageName,
                                "Location" to region,
                                "Description" to description,
                                "username" to username,
                                "pagePicture" to pagePicture
                            )

                            // Store owner data in Firestore under the 'users' collection
                            db.collection("users")
                                .document(userID) // Using ownerId (Firebase UID) as the document ID
                                .set(userData)
                                .addOnSuccessListener {
                                    // Store page data in Firestore under the 'pages' collection
                                    db.collection("pages")
                                        .document(userID) // Use the same ownerId as the document ID for pages
                                        .set(pageData)
                                        .addOnSuccessListener {
                                            Log.d("Firestore", "Account created successfully")
                                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                            // Navigate to HomeActivity or Dashboard
                                            signupInfo.reset()
                                            val intent = Intent(this, ownerLogin::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            // Error while storing page data in Firestore
                                            Log.e("Firestore", "Error adding page data: ${e.message}")
                                            Toast.makeText(this, "Error storing page data: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .addOnFailureListener { e ->
                                    // Error while storing owner data in Firestore
                                    Toast.makeText(this, "Error storing user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                    Log.e("Firestore", "Error adding user data: ${e.message}")
                                }
                        } else {
                            // Handle the case where userID is null
                            Toast.makeText(this, "Failed to retrieve user ID", Toast.LENGTH_SHORT).show()
                            Log.e("Firestore", "User ID is null")
                        }
                    } else {
                        // Error during account creation
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // Handle empty email or password fields
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
        }
    }

}