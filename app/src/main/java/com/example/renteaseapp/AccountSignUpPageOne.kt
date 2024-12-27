package com.example.renteaseapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.renteaseapp.signupInfo
import android.widget.Toast
import android.text.Editable
import android.text.TextWatcher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore

class AccountSignUpPageOne : AppCompatActivity() {
    private lateinit var next: Button
    private lateinit var realName: EditText
    private lateinit var userName: EditText
    private lateinit var emailAddress: EditText
    private lateinit var contactNumber: EditText
    private lateinit var password: EditText
    private lateinit var reenterpassword: EditText
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page_one)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = FirebaseFirestore.getInstance()
        realName = findViewById(R.id.enterNameSignup)
        userName = findViewById(R.id.enterUsernameSignup)
        emailAddress = findViewById(R.id.enterEmailSignup)
        contactNumber = findViewById(R.id.enterContactNumSignup)
        password = findViewById(R.id.enterPasswordSignup)
        reenterpassword = findViewById(R.id.reenterPasswordSignup)
        next = findViewById<Button>(R.id.toSecondSignUpbtn)

        next.setOnClickListener{
            if (validateInput()) {
                // If all fields are valid, proceed with the signup logic
                val name = realName.text.toString()
                val username = userName.text.toString()
                val email = emailAddress.text.toString()
                val contactNo = contactNumber.text.toString()
                val password = password.text.toString()

                // Store the input values in the Singleton (SignupInfo)
                signupInfo.SrealName = name
                signupInfo.Susername = username
                signupInfo.Semail = email
                signupInfo.ScontractNo = contactNo.substring(4).trim()
                signupInfo.Spassword = password
                val intent = Intent(this, AccountSignUpFirstID::class.java)
                startActivity(intent)
            } else {
                // If any validation fails, don't proceed
                Toast.makeText(this, "Please correct the errors in the form", Toast.LENGTH_SHORT).show()
            }
        }
        contactNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !contactNumber.text.toString().startsWith("+63 ")) {
                contactNumber.setText("+63 ")
                contactNumber.setSelection(contactNumber.text.length)
            }
        }

        realName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

        userName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

        emailAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

        contactNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (charSequence != null) {
                    // If the user starts typing and the input doesn't begin with +63, add +63
                    if (!charSequence.startsWith("+63 ") && charSequence.isNotEmpty()) {
                        // Add the +63 only if it's not there already
                        contactNumber.setText("+63 " + charSequence.toString().replaceFirst("^\\+63 ", ""))
                        contactNumber.setSelection(contactNumber.text.length) // Move cursor to the end
                    }

                    // If user tries to erase +63, keep it in place
                    if (charSequence.startsWith("+63") && charSequence.length == 3) {
                        // If user tries to erase the "+63" and just leave "+6", we prevent this
                        contactNumber.setText("+63 ")
                        contactNumber.setSelection(contactNumber.text.length) // Move cursor to the end
                    }
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

        reenterpassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

    }
    private fun validateInput(): Boolean {
        val nameText = realName.text.toString()
        val usernameText = userName.text.toString()
        val passwordText = password.text.toString()
        val reenterPasswordText = reenterpassword.text.toString()
        val contactNumberText = contactNumber.text.toString()
        val emailText = emailAddress.text.toString()

        var isValid = true

        // Validate Username
        if (usernameText.contains(" ")) {
            userName.error = "No Spaces"
            userName.setBackgroundResource(R.drawable.wronginput) // Set background color to red if invalid
            isValid = false
        }else {
            checkIfUsernameExists(usernameText) { exists ->
                if (exists) {
                    password.error ="Username already taken"
                } else {
                    userName.setBackgroundResource(R.drawable.rounded_cornerb_whitebg) // Reset background color if valid
                }
            }
        }

        // Validate Password
        if (!isValidPassword(passwordText) || passwordText.contains(" ")) {
            password.error = "Password must be 8-12 characters long, no spaces, include uppercase, lowercase, numbers, and special characters"
            password.setBackgroundResource(R.drawable.wronginput) // Set background color to red if invalid
            isValid = false
        } else {
            password.setBackgroundResource(R.drawable.rounded_cornerb_whitebg) // Reset background color if valid
        }

        // Validate Reentered Password
        if (passwordText != reenterPasswordText) {
            reenterpassword.error = "Passwords do not match"
            reenterpassword.setBackgroundResource(R.drawable.wronginput) // Set background color to red if invalid
            isValid = false
        } else {
            reenterpassword.setBackgroundResource(R.drawable.rounded_cornerb_whitebg) // Reset background color if valid
        }

        // Validate Contact Number (starts with 9 and valid length)
        if (!isValidContactNumber(contactNumberText)) {
            contactNumber.error = "Contact number must start with 9 and be 10 digits with no spaces."
            contactNumber.setBackgroundResource(R.drawable.wronginput) // Set background color to red if invalid
            isValid = false
        } else {
            contactNumber.setBackgroundResource(R.drawable.rounded_cornerb_whitebg) // Reset background color if valid
        }

        // Validate Email Address
        if (!isValidEmail(emailText)|| emailText.contains(" ")) {
            emailAddress.error = "Invalid email format"
            emailAddress.setBackgroundResource(R.drawable.rounded_cornerb_whitebg) // Set background color to red if invalid
            isValid = false
        } else {
            emailAddress.setBackgroundResource(R.drawable.rounded_cornerb_whitebg) // Reset background color if valid
        }

        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        // Password regex: at least one lowercase, one uppercase, one digit, one special character, length between 8-12
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$"
        return password.matches(Regex(passwordPattern))
    }

    private fun isValidContactNumber(contactNumber: String): Boolean {
        // Check if contact number starts with 9 and is exactly 10 digits
        val numberPart = contactNumber.substring(4).trim() // Extract from the 5th character onwards

        // Validate the remaining part with a regex (must start with 9 and have exactly 10 digits)
        if (numberPart.contains(" "))
            return false
        else{
            return numberPart.matches(Regex("^9\\d{9}$"))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Use Android's built-in EMAIL_ADDRESS pattern to check for a valid email format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun checkAllFieldsFilled() {
        val allFieldsFilled = realName.text.isNotEmpty() &&
                userName.text.isNotEmpty() &&
                emailAddress.text.isNotEmpty() &&
                contactNumber.text.isNotEmpty() &&
                password.text.isNotEmpty() &&
                reenterpassword.text.isNotEmpty()

        // Enable the next button if all fields are filled, otherwise disable it
        if(allFieldsFilled){
            next.visibility = View.VISIBLE
        }else{
            next.visibility = View.INVISIBLE
        }
    }
    private fun checkIfUsernameExists(username: String, callback: (Boolean) -> Unit) {
        db.collection("users").whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                callback(!documents.isEmpty) // Returns true if username exists
            }
            .addOnFailureListener {
                callback(false)
            }
    }

}