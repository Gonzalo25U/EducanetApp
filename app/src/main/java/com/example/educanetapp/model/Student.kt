package com.example.educanetapp.model

data class Student(
    val name: String,
    val email: String,
    val password: String,
    val rut: String,
    val phone: String,
    val photoUrl: String? = null,
    val biography: String? = null, // Nueva biografía
    val photoUri: String? = null   // URI local de la foto (antes de subir al servidor)
)

// Estado de UI para la pantalla de configuración
data class SettingsUiState(
    val isLoading: Boolean = false,
    val student: Student? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)