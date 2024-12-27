package com.example.renteaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AccountSignUpSecondID : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_sign_up_second_id)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accountSignUpSecondID)) { v, insets ->
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
            if(signupInfo.DL){
                Toast.makeText(this, "Choose other ID.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this,DriverLicenseUpload::class.java)
                startActivity(intent)
            }
        }
        btnUMID.setOnClickListener{
            if(signupInfo.UMID){
                Toast.makeText(this, "Choose other ID.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this,UMIDUpload::class.java)
                startActivity(intent)
            }
        }
        btnPostalID.setOnClickListener{
            if(signupInfo.PID){
                Toast.makeText(this, "Choose other ID.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this,PostalIDUpload::class.java)
                startActivity(intent)
            }
        }
        btnSSSID.setOnClickListener{
            if(signupInfo.SSSID){
                Toast.makeText(this, "Choose other ID.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this,SSSidUpload::class.java)
                startActivity(intent)
            }
        }
        btnPRCID.setOnClickListener{
            if(signupInfo.PRCID){
                Toast.makeText(this, "Choose other ID.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this,PRCIDUpload::class.java)
                startActivity(intent)
            }
        }
        btnHDMFID.setOnClickListener{
            if(signupInfo.HDMFID){
                Toast.makeText(this, "Choose other ID.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this,HDMFIDUpload::class.java)
                startActivity(intent)
            }
        }
    }
}