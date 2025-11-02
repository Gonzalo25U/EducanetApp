package com.example.educanetapp.model

data class ProfileUiState(
    val student: Student? = null,
    val grades: List<Grade> = emptyList(),
    val isLoading: Boolean = true
)
