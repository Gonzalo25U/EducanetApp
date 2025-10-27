package com.example.educanetapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.model.Grade
import com.example.educanetapp.model.Student
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val student: Student? = null,
    val grades: List<Grade> = emptyList(),
    val isLoading: Boolean = true
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            // Simulamos una carga de datos (por ejemplo, desde Supabase o Room)
            delay(1200)

            val student = Student(
                name = "Gonzalo Sebastián",
                email = "estudiante@educanet.cl",
                password = "",
                rut = "20.456.789-0",
                phone = "+56 9 1234 5678",
                photoUrl = null // podrías agregar una URL real más adelante
            )

            val grades = listOf(
                Grade("Matemáticas", 5.8),
                Grade("Lenguaje", 6.3),
                Grade("Historia", 5.5),
                Grade("Ciencias", 6.0)
            )

            _uiState.value = ProfileUiState(
                student = student,
                grades = grades,
                isLoading = false
            )
        }
    }
}