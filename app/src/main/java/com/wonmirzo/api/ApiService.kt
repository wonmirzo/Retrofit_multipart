package com.wonmirzo.api

import com.wonmirzo.model.ImageResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("images/upload")
    fun uploadPhoto(@Body body: RequestBody): Call<ImageResponse>
}