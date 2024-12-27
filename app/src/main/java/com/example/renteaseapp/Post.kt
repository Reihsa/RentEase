package com.example.renteaseapp
import com.google.firebase.Timestamp

data class Post(
    val date: Timestamp? = null, // Match Firestore's timestamp type
    val itemImageUrl: String? = null,
    val itemName: String? = null,
    val location: String? = null,
    val price: Int? = null,
    val profilePictureUrl: String? = null,
    val type: String? = null,
    val uid: String? = null,
    val username: String? = null,
    val description: String? = null
)