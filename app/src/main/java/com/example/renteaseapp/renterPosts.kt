package com.example.renteaseapp

import com.google.firebase.Timestamp

object renterPosts {
    var description: String? = null
    var itemImageUrl: String? = null
    var profilePictureUrl: String? = null
    var type: String? = null
    var uid: String? = null
    var username: String? = null
    var date: Timestamp? = null

    fun reset() {
        description = null
        itemImageUrl = null
        profilePictureUrl = null
        type = null
        uid = null
        username = null
    }
}