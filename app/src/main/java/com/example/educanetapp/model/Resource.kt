package com.example.educanetapp.model
enum class ResourceType { LIBRO, ARTICULO, VIDEO }

data class Resource(
    val id: Int,
    val title: String,
    val description: String,
    val type: ResourceType,
    val link: String
)
