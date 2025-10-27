package com.example.educanetapp.model

data class Student(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val rut: String = "",
    val phone: String = "",
    val photoUrl: String? = null
)