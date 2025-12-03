package com.example.educanetapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.dto.UserProfileDTO
import com.example.educanetapp.model.ProfileUiState
import com.example.educanetapp.model.Student
import com.example.educanetapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val context: Context,
    private val userEmail: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Usar el Context para obtener la instancia correcta de Retrofit
                val dto: UserProfileDTO = RetrofitInstance.getProfileApi(context).getProfile(userEmail)

                val student = Student(
                    name = dto.nombre,
                    email = dto.email,
                    password = "", // nunca devolver contraseña
                    rut = dto.rut,
                    phone = dto.phone,
                    photoUrl = null,
                    biography = null
                )

                _uiState.value = ProfileUiState(
                    student = student,
                    grades = dto.grades,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    error = "Error al cargar perfil: ${e.message}"
                )
            }
        }
    }

    fun refreshProfile() {
        loadProfile()
    }

    fun logout() {
        // Limpieza de sesión si es necesario
    }
}
