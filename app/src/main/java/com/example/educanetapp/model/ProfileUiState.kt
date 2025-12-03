package com.example.educanetapp.model

data class ProfileUiState(
    val isLoading: Boolean = false,
    val student: Student? = null,
    val grades: List<Grade> = emptyList(),
    val error: String? = null
)
