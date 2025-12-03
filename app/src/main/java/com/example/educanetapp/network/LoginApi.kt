package com.example.educanetapp.network

import com.example.educanetapp.dto.LoginRequest
import com.example.educanetapp.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login") // Cambia según tu endpoint del backend
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
