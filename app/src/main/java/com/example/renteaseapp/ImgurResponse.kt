package com.example.renteaseapp

import com.squareup.moshi.Json

data class ImgurResponse(
    val data: ImgurData,
    val success: Boolean,
    val status: Int
)

data class ImgurData(
    val id: String,
    val link: String,
    @Json(name = "deletehash") val deleteHash: String
)