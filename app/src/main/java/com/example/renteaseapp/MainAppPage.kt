package com.example.renteaseapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import java.lang.Exception

class MainAppPage : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_app_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadFragment(OwnerNewsfeed())
        bottomNavigationView = findViewById(R.id.mainAppNav)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.lender_newsfeed -> loadFragment(OwnerNewsfeed())
                R.id.renter_newsfeed -> loadFragment(RenterNewsfeed())
                R.id.create_post -> ownerOrRenterDialogue()
                R.id.notification -> loadFragment(NotificationPage())
                R.id.profile_newsfeed -> loadFragment(ProfilePage())
            }
            true
        }
    }
    private fun loadFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }

    private fun ownerOrRenterDialogue(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.owner_or_renter_post)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val toRenterPost : Button = dialog.findViewById(R.id.renter_post)
        val toOwnerPost : Button = dialog.findViewById(R.id.owner_post)
        val cancelPicker: Button = dialog.findViewById(R.id.cancelPicker)

        loadFragment(BaitFragment())

        toRenterPost.setOnClickListener{
            loadFragment(CreatePostRenter())
            dialog.dismiss()
        }

        toOwnerPost.setOnClickListener{
            loadFragment(CreatePost())
            dialog.dismiss()
        }

        cancelPicker.setOnClickListener({
            dialog.dismiss()
        })
        dialog.show()
    }
}