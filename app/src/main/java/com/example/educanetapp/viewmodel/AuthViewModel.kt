package com.example.educanetapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.model.Student
import com.example.educanetapp.model.AuthUiState
import com.example.educanetapp.utils.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        // Cargar usuarios registrados al inicio
        // Nota: si quieres puedes llamar a loadUsers(context) desde la pantalla con el contexto
    }

    fun loadUsers(context: Context) {
        viewModelScope.launch {
            val users = DataStoreManager.loadUsers(context)
            _uiState.value = _uiState.value.copy(students = users)
        }
    }

    fun register(context: Context, student: Student) {
        viewModelScope.launch {
            // Convertimos Student a Map<String, String>
            val studentMap = mapOf(
                "name" to student.name,
                "email" to student.email,
                "password" to student.password,
                "rut" to student.rut,
                "phone" to student.phone
            )

            val updated = _uiState.value.students + studentMap
            DataStoreManager.saveUsers(context, updated)

            _uiState.value = _uiState.value.copy(
                students = updated,
                success = true,
                error = null
            )
        }
    }

    fun login(context: Context, email: String, password: String) {
        viewModelScope.launch {
            val users = DataStoreManager.loadUsers(context)
            val user = users.find { it["email"] == email && it["password"] == password }

            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    loggedInUser = user,
                    error = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = "Correo o contraseña incorrectos"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
