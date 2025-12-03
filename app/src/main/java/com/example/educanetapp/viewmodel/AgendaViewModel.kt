package com.example.educanetapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educanetapp.utils.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AgendaViewModel(private val context: Context) : ViewModel() {

    private val _selectedDate = MutableStateFlow(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    )
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _reminders = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val reminders: StateFlow<Map<String, List<String>>> = _reminders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadReminders()
    }

    /**
     * Carga los recordatorios desde DataStore
     */
    private fun loadReminders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val savedReminders = DataStoreManager.loadReminders(context)
                _reminders.value = savedReminders
            } catch (e: Exception) {
                // Si hay error, mantener el mapa vacío
                _reminders.value = emptyMap()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Guarda los recordatorios en DataStore
     */
    private suspend fun saveReminders() {
        try {
            DataStoreManager.saveReminders(context, _reminders.value)
        } catch (e: Exception) {
            // Manejar error si es necesario
            e.printStackTrace()
        }
    }

    /**
     * Selecciona una fecha
     */
    fun selectDate(dateStr: String) {
        _selectedDate.value = dateStr
    }

    /**
     * Obtiene los recordatorios de una fecha específica
     */
    fun getRemindersForDate(dateStr: String): List<String> {
        return _reminders.value[dateStr] ?: emptyList()
    }

    /**
     * Agrega un nuevo recordatorio
     */
    fun addReminder(note: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val currentNotes = _reminders.value[date]?.toMutableList() ?: mutableListOf()
            currentNotes.add(note)

            // Actualizar el estado
            _reminders.value = _reminders.value + (date to currentNotes)

            // Guardar en DataStore
            saveReminders()
        }
    }

    /**
     * Actualiza un recordatorio existente
     */
    fun updateReminder(oldNote: String, newNote: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val currentNotes = _reminders.value[date]?.toMutableList() ?: return@launch
            val index = currentNotes.indexOf(oldNote)

            if (index != -1) {
                currentNotes[index] = newNote

                // Actualizar el estado
                _reminders.value = _reminders.value + (date to currentNotes)

                // Guardar en DataStore
                saveReminders()
            }
        }
    }

    /**
     * Elimina un recordatorio
     */
    fun deleteReminder(note: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val currentNotes = _reminders.value[date]?.toMutableList() ?: return@launch
            currentNotes.remove(note)

            // Actualizar el estado
            _reminders.value = if (currentNotes.isEmpty()) {
                // Si no quedan notas, remover la fecha del mapa
                _reminders.value - date
            } else {
                _reminders.value + (date to currentNotes)
            }

            // Guardar en DataStore
            saveReminders()
        }
    }

    /**
     * Refresca los recordatorios desde DataStore
     */
    fun refreshReminders() {
        loadReminders()
    }
}