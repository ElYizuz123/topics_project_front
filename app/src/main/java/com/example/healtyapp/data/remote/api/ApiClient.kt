package com.example.healtyapp.data.remote.api

import com.example.healtyapp.HealtyAppApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.healtyapp.util.TokenStore

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        
        // Verificar si la aplicación está inicializada y obtener token
        val token = if (HealtyAppApplication.isInitialized()) {
            try {
                val ctx = HealtyAppApplication.instance.applicationContext
                TokenStore.getAccess(ctx)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

        val req = if (token != null && token.isNotEmpty()) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }

        chain.proceed(req)
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val http = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}