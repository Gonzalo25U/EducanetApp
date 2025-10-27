package com.example.educanetapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.model.AuthUiState
import com.example.educanetapp.model.Student
import com.example.educanetapp.utils.DataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private var registeredStudents = mutableListOf<Student>()

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun register(context: Context, student: Student) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            delay(500)

            val stored = DataStoreManager.loadUsers(context)
            registeredStudents = stored.map {
                Student(
                    name = it["name"] ?: "",
                    email = it["email"] ?: "",
                    password = it["password"] ?: "",
                    rut = it["rut"] ?: "",
                    phone = it["phone"] ?: "",
                    photoUrl = null
                )
            }.toMutableList()

            if (registeredStudents.any { it.email == student.email }) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ya existe un usuario con este correo"
                )
            } else {
                registeredStudents.add(student)
                saveUsers(context)
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            }
        }
    }

    fun login(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            delay(500)

            val stored = DataStoreManager.loadUsers(context)
            registeredStudents = stored.map {
                Student(
                    name = it["name"] ?: "",
                    email = it["email"] ?: "",
                    password = it["password"] ?: "",
                    rut = it["rut"] ?: "",
                    phone = it["phone"] ?: "",
                    photoUrl = null
                )
            }.toMutableList()

            val email = _uiState.value.email
            val password = _uiState.value.password

            val found = registeredStudents.any { it.email == email && it.password == password }

            if (found) {
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Correo o contraseña incorrectos"
                )
            }
        }
    }

    private fun saveUsers(context: Context) {
        viewModelScope.launch {
            val userMaps = registeredStudents.map {
                mapOf(
                    "name" to it.name,
                    "email" to it.email,
                    "password" to it.password,
                    "rut" to it.rut,
                    "phone" to it.phone
                )
            }
            DataStoreManager.saveUsers(context, userMaps)
        }
    }
}
