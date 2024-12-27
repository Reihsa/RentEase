package com.example.renteaseapp
import com.google.firebase.Timestamp
import com.example.renteaseapp.signupInfo.DL
import com.example.renteaseapp.signupInfo.HDMFID
import com.example.renteaseapp.signupInfo.NID
import com.example.renteaseapp.signupInfo.PID
import com.example.renteaseapp.signupInfo.PRCID
import com.example.renteaseapp.signupInfo.SSSID
import com.example.renteaseapp.signupInfo.ScontractNo
import com.example.renteaseapp.signupInfo.Sdescription
import com.example.renteaseapp.signupInfo.Semail
import com.example.renteaseapp.signupInfo.Spagename
import com.example.renteaseapp.signupInfo.Spassword
import com.example.renteaseapp.signupInfo.SrealName
import com.example.renteaseapp.signupInfo.Sregion
import com.example.renteaseapp.signupInfo.Susername
import com.example.renteaseapp.signupInfo.UMID
import com.example.renteaseapp.signupInfo.firstIDUploaded
import com.example.renteaseapp.signupInfo.secondIDUploaded

object ownerPosts {
    var itemName: String? = null
    var price: Int? = null
    var location: String? = null
    var description: String? = null
    var itemImageUrl: String? = null
    var profilePictureUrl: String? = null
    var type: String? = null
    var uid: String? = null
    var username: String? = null
    var date: Timestamp? = null

    fun reset() {
        itemName = null
        price = null
        location = null
        description = null
        itemImageUrl = null
        profilePictureUrl = null
        type = null
        uid = null
        username = null
    }
}