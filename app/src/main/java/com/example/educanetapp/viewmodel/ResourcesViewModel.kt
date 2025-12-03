package com.example.educanetapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.model.Resource
import com.example.educanetapp.model.ResourceType
import com.example.educanetapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResourceViewModel(private val context: Context) : ViewModel() {

    private val resourceApi = RetrofitInstance.getResourceApi(context)

    private val _resources = MutableStateFlow<List<Resource>>(emptyList())
    val resources: StateFlow<List<Resource>> get() = _resources

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        loadResources()
    }

    fun loadResources() {
        Log.d("RESOURCES", "Cargando recursos...")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val data = resourceApi.getAllResources()
                Log.d("RESOURCES", "Respuesta: $data")
                _resources.value = data
            } catch (e: Exception) {
                Log.e("RESOURCES_ERROR", "Error: ${e.message}")
                _error.value = "Error cargando recursos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadResourcesByType(type: ResourceType) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = resourceApi.getResourcesByType(type.name)
                _resources.value = response
            } catch (e: Exception) {
                _error.value = "Error filtrando recursos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}