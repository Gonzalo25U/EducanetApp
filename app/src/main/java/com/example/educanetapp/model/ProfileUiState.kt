package com.example.educanetapp.model

data class ProfileUiState(
    //contiene la informacion de el estudiante actualmente logeado
    val student: Student? = null,

    //guarda la lista de las calificaciones
    val grades: List<Grade> = emptyList(),

    //indica si se estan mostrando los datos
    val isLoading: Boolean = true
)
