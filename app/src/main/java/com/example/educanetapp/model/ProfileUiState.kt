package com.example.educanetapp.model

import android.net.Uri
data class ProfileUiState(
    val isLoading: Boolean = true,
    val student: Student? = null,
    val grades: List<Grade>? = null,
    val capturedImageUri: Uri? = null // Añadido para manejar la foto de perfil
)