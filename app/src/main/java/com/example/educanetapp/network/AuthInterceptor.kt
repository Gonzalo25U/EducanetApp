package com.example.educanetapp.network

import android.content.Context
import com.example.educanetapp.utils.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Obtener token desde DataStore
        val token = runBlocking { TokenManager.getToken(context) }

        val newRequest = if (!token.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else request

        return chain.proceed(newRequest)
    }
}
