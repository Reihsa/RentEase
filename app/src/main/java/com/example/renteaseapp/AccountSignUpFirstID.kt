package com.example.renteaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.sign

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
        val btnNationalID = findViewById<Button>(R.id.natIDbtn)
        val btnDriverLicenseID= findViewById<Button>(R.id.dlIDbtn)
        val btnUMID = findViewById<Button>(R.id.umIDbtn)
        val btnPostalID = findViewById<Button>(R.id.postalIDbtn)
        val btnSSSID = findViewById<Button>(R.id.sssIDbtn)
        val btnPRCID = findViewById<Button>(R.id.prcIDbtn)
        val btnHDMFID = findViewById<Button>(R.id.hdmfID)

        btnNationalID.setOnClickListener{
            if(signupInfo.NID){
                Toast.makeText(this, "Choose other ID.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this,NationalIDUpload::class.java)
                startActivity(intent)
            }
        }
        btnDriverLicenseID.setOnClickListener{
            val intent = Intent(this,DriverLicenseUpload::class.java)
            startActivity(intent)
        }
        btnUMID.setOnClickListener{
            val intent = Intent(this,UMIDUpload::class.java)
            startActivity(intent)
        }
        btnPostalID.setOnClickListener{
            val intent = Intent(this,PostalIDUpload::class.java)
            startActivity(intent)
        }
        btnSSSID.setOnClickListener{
            val intent = Intent(this,SSSidUpload::class.java)
            startActivity(intent)
        }
        btnPRCID.setOnClickListener{
            val intent = Intent(this,PRCIDUpload::class.java)
            startActivity(intent)
        }
        btnHDMFID.setOnClickListener{
            val intent = Intent(this,HDMFIDUpload::class.java)
            startActivity(intent)
        }
    }
}