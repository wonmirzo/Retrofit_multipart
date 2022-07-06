package com.wonmirzo.api

import com.wonmirzo.BuildConfig
import com.wonmirzo.helper.MyStrings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "https://api.thecatapi.com/v1/"

    private val client = buildClient()
    private val retrofit = buildRetrofit(client)


    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .addNetworkInterceptor(Interceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                builder.addHeader("Accept", "application/json")
                request = builder.build()
                chain.proceed(request)
            })
        if (BuildConfig.DEBUG) {
            // Debug holatdan keyin o`chirish kerak!!!!!!!!!!!!!!!!!!!!!!!
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    fun <T> createServiceWithAuth(service: Class<T>): T {
        val newClient = client.newBuilder().addInterceptor(Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()
            builder.addHeader("x-api-key", MyStrings.accessKey)
            request = builder.build()
            chain.proceed(request)
        }).build()
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }
}