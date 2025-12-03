package com.example.educanetapp.model
data class Resource(
    val id: Long,
    val title: String,
    val description: String,
    val type: ResourceType,
    val link: String
)
