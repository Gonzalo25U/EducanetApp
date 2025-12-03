package com.example.educanetapp.dto

import com.example.educanetapp.model.Grade

data class UserProfileDTO(
    val nombre: String,
    val email: String,
    val rut: String,
    val phone: String,
    val grades: List<Grade>
)
