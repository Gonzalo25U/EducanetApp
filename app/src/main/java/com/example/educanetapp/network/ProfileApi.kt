package com.example.educanetapp.network

import com.example.educanetapp.dto.UserProfileDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApi {

    // Se puede pasar el email como query si el backend lo requiere
    @GET("profile")
    suspend fun getProfile(@Query("email") email: String): UserProfileDTO
}
