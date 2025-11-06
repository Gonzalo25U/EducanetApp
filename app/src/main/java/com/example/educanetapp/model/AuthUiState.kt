package com.example.educanetapp.model

data class AuthUiState(
    val students: List<Map<String, String>> = emptyList(),
    val loggedInUser: Map<String, String>? = null,
    val error: String? = null,
    val success: Boolean = false
)
