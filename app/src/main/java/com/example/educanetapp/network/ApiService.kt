package com.example.educanetapp.network

import com.example.educanetapp.dto.UserProfileDTO
import retrofit2.http.GET

interface ApiService {

    // Obtiene el perfil del usuario logueado
    @GET("profile")
    suspend fun getProfile(): UserProfileDTO
}
