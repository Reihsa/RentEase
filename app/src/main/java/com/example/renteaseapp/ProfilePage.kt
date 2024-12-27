package com.example.renteaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilePage : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var db: FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private lateinit var logout: Button
    private var usernamecurr: String? = null
    private var profilePicturecurr: String? = null
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        logout = view.findViewById(R.id.logOutbtn)
        fetchUserDetailsAndStoreToSingleton()

        logout.setOnClickListener {
            logoutUser()
        }

        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun fetchUserDetailsAndStoreToSingleton(onComplete: () -> Unit = {}) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        usernamecurr= document.getString("username")
                        profilePicturecurr = document.getString("profilePicture")
                        loadUserProfileAndUsername() // Trigger the next action, e.g., uploading the post
                    } else {
                        Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadUserProfileAndUsername() {
        try {
            // Reference to the ImageView and TextView
            val profilePictureView: ImageView = requireView().findViewById(R.id.profilePicture)
            val usernameTextView: TextView = requireView().findViewById(R.id.username)

            // Load the profile picture using Glide
            if (!profilePicturecurr.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(profilePicturecurr)
                    .placeholder(R.drawable.profile_circle_svgrepo_com) // Fallback image
                    .error(R.drawable.profile_circle_svgrepo_com) // Error image
                    .into(profilePictureView)
            } else {
                // Set placeholder if URL is null or empty
                profilePictureView.setImageResource(R.drawable.profile_circle_svgrepo_com)
            }

            // Set the username
            usernameTextView.text = if (!usernamecurr.isNullOrEmpty()) usernamecurr else "User" // Default text if username is null or empty

        } catch (e: Exception) {
            // Handle any exceptions that occur during loading
            Toast.makeText(requireContext(), "Error loading profile information: ${e.message}", Toast.LENGTH_SHORT).show()

            // Set default image and username in case of error
            val profilePictureView: ImageView = requireView().findViewById(R.id.createPostProfilePicture)
            profilePictureView.setImageResource(R.drawable.profile_circle_svgrepo_com)
            val usernameTextView: TextView = requireView().findViewById(R.id.currentUsernametxt)
            usernameTextView.text = "User"
        }
    }

    private fun logoutUser() {
        auth.signOut() // Clear Firebase authentication session
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate to ChooseOwnerRenter activity
        val intent = Intent(requireContext(), ChooseOwnerRenter::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
        startActivity(intent)
    }
}