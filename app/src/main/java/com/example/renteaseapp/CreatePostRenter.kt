package com.example.renteaseapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreatePostRenter.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatePostRenter : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var itemDescription: EditText
    private lateinit var btnPost: Button
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var db: FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private var imageUri: Uri? = null
    private lateinit var imageName: TextView
    private lateinit var btnPickImage: Button
    private var isImageUploaded: Boolean = false
    private lateinit var btnRemImage: Button
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
        val view = inflater.inflate(R.layout.fragment_create_post_renter, container, false)

        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        itemDescription = view.findViewById(R.id.descriptionet)
        imageName = view.findViewById(R.id.productImagetxt)
        btnRemImage = view.findViewById(R.id.removeProductImage)
        btnPickImage = view.findViewById(R.id.btnaddItemImage)
        btnPost = view.findViewById(R.id.renterPostbtn)

        // Fetch user details and store them to Singleton
        fetchUserDetailsAndStoreToSingleton()

        btnPost.setOnClickListener {
            uploadImageToImgur()
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
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
            imagePickerDialogue()
        }

        itemDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkAllFieldsFilled()
            }
        })

        // Return the inflated view
        return view
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
                imageName.text = "Image is Uploaded!"
                isImageUploaded = true
                btnRemImage.visibility = View.VISIBLE
                btnPickImage.visibility = View.INVISIBLE
                checkAllFieldsFilled()

            }
        }
    }

    private fun imagePickerDialogue(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.camera_gallery_picker)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCamera : Button = dialog.findViewById(R.id.btnCamera)
        val btnGallery : Button = dialog.findViewById(R.id.btnGallery)
        val btnCancel : Button = dialog.findViewById(R.id.btnCancelPicker)

        btnCamera.setOnClickListener{
            imageUri = createImageUri()
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
        val image = File(requireContext().filesDir, "photo.png")
        return FileProvider.getUriForFile(requireContext(), "com.example.renteaseapp.fileprovider", image)

    }

    private fun checkAllFieldsFilled() {
        val allFieldsFilled = itemDescription.text.isNotEmpty() && isImageUploaded

        // Enable the next button if all fields are filled, otherwise disable it
        if(allFieldsFilled){
            btnPost.visibility = View.VISIBLE
        }else{
            btnPost.visibility = View.INVISIBLE
        }
    }


    private fun uploadImageToImgur() {
        val imgurService = ImgurClient.retrofit

        // Check if imageUri is null or invalid
        if (imageUri == null) {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            return
        }

        // Attempt to create an image file
        val imageFile = try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
            if (inputStream == null) {
                Toast.makeText(requireContext(), "Error opening image stream", Toast.LENGTH_SHORT).show()
                return
            }
            try {
                inputStream.readBytes().let { bytes ->
                    File(requireContext().cacheDir, "temp_image").apply {
                        writeBytes(bytes)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error reading image bytes: ${e.message}", Toast.LENGTH_SHORT).show()
                return
            } finally {
                inputStream.close()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error accessing image: ${e.message}", Toast.LENGTH_SHORT).show()
            return
        }

        // Ensure the file is not null and prepare the request
        val requestFile = try {
            imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error creating request body: ${e.message}", Toast.LENGTH_SHORT).show()
            return
        }

        // Wrap the request in a MultipartBody.Part
        val imageBody = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        // Add authorization and make the API call
        val authHeader = "Client-ID 9ae4752cd37b0a5" // Replace with your Imgur client ID
        try {
            imgurService.uploadImage(authHeader, imageBody).enqueue(object :
                Callback<ImgurResponse> {
                override fun onResponse(call: Call<ImgurResponse>, response: Response<ImgurResponse>) {
                    if (response.isSuccessful) {
                        val imgurResponse = response.body()
                        val imageUrl = imgurResponse?.data?.link
                        if (imageUrl != null) {
                            getUserDataAndUploadPost(imageUrl)
                        } else {
                            Toast.makeText(requireContext(), "Failed to get image URL from Imgur", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Toast.makeText(requireContext(), "Imgur API response error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ImgurResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    t.printStackTrace() // Logs the stack trace for debugging
                }
            })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Unexpected error during API call: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace() // Logs the stack trace for debugging
        }

    }



    private fun getUserDataAndUploadPost(imageUrl: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            try {
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val profilePictureUrl = document.getString("profilePicture")
                            val userName = document.getString("username")
                            ownerPosts.itemImageUrl = imageUrl
                            ownerPosts.profilePictureUrl = profilePictureUrl
                            ownerPosts.username = userName
                            ownerPosts.uid = uid
                            uploadPostToFirestore()
                        } else {
                            Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error getting user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An unexpected error occurred while fetching user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPostToFirestore() {
        try {
            renterPosts.description = itemDescription.text.toString()
            renterPosts.date = Timestamp.now()
            renterPosts.type = "renter"

            db.collection("posts").add(ownerPosts)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Post uploaded successfully", Toast.LENGTH_SHORT).show()
                    // Clear the fields after successful upload
                    clearFields()
                    ownerPosts.reset()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to upload post: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        imageName.text = ""
        isImageUploaded = false
        btnRemImage.visibility = View.INVISIBLE
        btnPickImage.isEnabled = true
        btnPickImage.isClickable = true
        btnPickImage.visibility = View.VISIBLE
        btnPost.visibility = View.INVISIBLE
        imageUri = null
    }

    private fun fetchUserDetailsAndStoreToSingleton(onComplete: () -> Unit = {}) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        renterPosts.username = document.getString("username")
                        renterPosts.profilePictureUrl = document.getString("profilePicture")
                        renterPosts.uid = uid
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
            val profilePictureView: ImageView = requireView().findViewById(R.id.createPostProfilePicture)
            val usernameTextView: TextView = requireView().findViewById(R.id.currentUsernametxt)

            // Load the profile picture using Glide
            val profilePictureUrl = renterPosts.profilePictureUrl
            if (!profilePictureUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.profile_circle_svgrepo_com) // Fallback image
                    .error(R.drawable.profile_circle_svgrepo_com) // Error image
                    .into(profilePictureView)
            } else {
                // Set placeholder if URL is null or empty
                profilePictureView.setImageResource(R.drawable.profile_circle_svgrepo_com)
            }

            // Set the username
            val username = renterPosts.username
            usernameTextView.text = if (!username.isNullOrEmpty()) username else "User" // Default text if username is null or empty

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



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreatePost.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreatePost().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}