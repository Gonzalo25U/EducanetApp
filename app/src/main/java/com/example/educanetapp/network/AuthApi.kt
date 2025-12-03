package com.example.educanetapp.network

import com.example.educanetapp.dto.UserProfileDTO
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val email: String,
    val password: String
)

interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): UserProfileDTO
}
