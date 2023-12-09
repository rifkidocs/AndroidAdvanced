package com.rifki.AndroidAdvanced.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface ApiEndPoint {

    @GET("photos")
    fun getPhotos(): Call<List<HomeModel>>
}