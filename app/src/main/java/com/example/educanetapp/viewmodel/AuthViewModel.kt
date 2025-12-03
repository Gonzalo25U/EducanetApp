package com.example.educanetapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.dto.LoginRequest
import com.example.educanetapp.model.AuthUiState
import com.example.educanetapp.network.RetrofitInstance
import com.example.educanetapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    /**
     * Inicia sesión usando el backend remoto.
     * Guarda el token en DataStore si el login es exitoso.
     */
    fun login(context: Context, email: String, password: String) {
        viewModelScope.launch {
            try {
                // Usar RetrofitInstance con contexto para obtener LoginApi
                val loginApi = RetrofitInstance.getLoginApi(context)
                val response = loginApi.login(LoginRequest(email, password))

                // Verificar si se obtuvo token
                if (!response.token.isNullOrEmpty()) {
                    // Guardar token en DataStore
                    TokenManager.saveToken(context, response.token)

                    // Actualizar UIState con usuario logueado
                    _uiState.value = _uiState.value.copy(
                        loggedInUser = mapOf("email" to email),
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        loggedInUser = null,
                        error = "Correo o contraseña incorrectos"
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loggedInUser = null,
                    error = "Error al iniciar sesión: ${e.message}"
                )
            }
        }
    }

    /**
     * Limpiar errores en la UI
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
