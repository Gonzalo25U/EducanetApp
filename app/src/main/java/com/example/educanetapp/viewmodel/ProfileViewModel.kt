package com.example.educanetapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.model.Grade
import com.example.educanetapp.model.ProfileUiState
import com.example.educanetapp.model.Student
import com.example.educanetapp.utils.DataStoreManager
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

            val users = DataStoreManager.loadUsers(context)
            val studentData = users.find { it["email"] == userEmail }

            if (studentData != null) {
                val student = Student(
                    name = studentData["name"] ?: "",
                    email = studentData["email"] ?: "",
                    password = studentData["password"] ?: "",
                    rut = studentData["rut"] ?: "",
                    phone = studentData["phone"] ?: "",
                    photoUrl = null // TODO: Cargar esto desde DataStore si lo guardas
                )

                val grades = listOf(
                    Grade("Matemáticas", 5.8),
                    Grade("Lenguaje", 6.3),
                    Grade("Historia", 5.5),
                    Grade("Ciencias", 6.0)
                )

                // MODIFICADO: Usamos .copy() para no sobrescribir la capturedImageUri
                _uiState.value = _uiState.value.copy(
                    student = student,
                    grades = grades,
                    isLoading = false
                )
            } else {
                // MODIFICADO: Usamos .copy()
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * NUEVA FUNCIÓN: Llamada desde NavGraph cuando se captura una nueva imagen.
     * Actualiza el estado de la UI con la URI de la imagen.
     */
    fun onProfileImageChanged(uri: Uri) {
        _uiState.value = _uiState.value.copy(capturedImageUri = uri)

        // Opcional: Aquí podrías añadir la lógica para guardar la
        // uri.toString() en tu DataStore de forma permanente.
        // viewModelScope.launch {
        //     DataStoreManager.saveUserPhoto(context, userEmail, uri.toString())
        // }
    }

    fun logout() {
        viewModelScope.launch {
            DataStoreManager.clearUsers(context) // Limpiar datos de usuario
        }
    }

    private fun DataStoreManager.clearUsers(context: Context) {}
}