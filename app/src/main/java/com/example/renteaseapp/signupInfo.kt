package com.example.renteaseapp

object signupInfo {
    var SrealName: String? = null
    var Susername: String? = null
    var Semail: String? = null
    var ScontractNo: String? = null
    var Spassword: String? = null
    var Spagename: String? = null
    var Sregion: String? = null
    var Sdescription: String? = null
    var firstIDUploaded: Boolean = false
    var secondIDUploaded: Boolean = false
    var NID: Boolean = false
    var DL: Boolean = false
    var UMID: Boolean = false
    var PID: Boolean = false
    var SSSID: Boolean = false
    var PRCID: Boolean = false
    var HDMFID: Boolean = false

    fun reset() {
        SrealName = null
        Susername = null
        Semail = null
        ScontractNo = null
        Spassword = null
        Spagename = null
        Sregion = null
        Sdescription = null
        firstIDUploaded = false
        secondIDUploaded = false
        NID = false
        DL = false
        UMID = false
        PID = false
        SSSID = false
        PRCID = false
        HDMFID = false
    }
}
