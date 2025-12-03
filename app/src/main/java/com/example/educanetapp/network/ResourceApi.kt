package com.example.educanetapp.network

import com.example.educanetapp.model.Resource
import com.example.educanetapp.model.ResourceType
import retrofit2.http.GET
import retrofit2.http.Path

interface ResourceApi {

    @GET("resources")
    suspend fun getAllResources(): List<Resource>

    @GET("resources/type/{type}")
    suspend fun getResourcesByType(@Path("type") type: String): List<Resource>
}

