package com.example.renteaseapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImgurClient {
    private const val BASE_URL = "https://api.imgur.com/3/" // Ensure this is correct

    val retrofit: ImgurService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImgurService::class.java)
    }
}