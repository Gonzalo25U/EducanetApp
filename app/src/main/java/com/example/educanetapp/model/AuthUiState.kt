package com.example.educanetapp.model

data class AuthUiState(
    //Guarda los estudiantes en una lista
    val students: List<Map<String, String>> = emptyList(),

    //Guarda los datos de el usuario actualmente logeado
    val loggedInUser: Map<String, String>? = null,

    //Guarda un mensaje de error si ocurre un problema durante el login
    val error: String? = null,

    //Indica si el login y/o el registro fue exitoso
    val success: Boolean = false
)
