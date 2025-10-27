package com.example.educanetapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class AgendaViewModel : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Fecha seleccionada
    private val _selectedDate = MutableStateFlow(dateFormat.format(Date()))
    val selectedDate: StateFlow<String> = _selectedDate

    // Recordatorios por fecha
    private val _reminders = MutableStateFlow<Map<String, MutableList<String>>>(mutableMapOf())
    val reminders: StateFlow<Map<String, MutableList<String>>> = _reminders

    /** Cambia la fecha seleccionada **/
    fun selectDate(dateString: String) {
        _selectedDate.value = dateString
    }

    /** Crea un nuevo recordatorio **/
    fun addReminder(note: String) {
        val dateKey = _selectedDate.value
        _reminders.update { map ->
            val newMap = map.toMutableMap()
            val notes = (newMap[dateKey] ?: mutableListOf()).toMutableList()
            notes.add(note)
            newMap[dateKey] = notes
            newMap
        }
    }

    /** Lee los recordatorios de una fecha **/
    fun getRemindersForDate(dateString: String = _selectedDate.value): List<String> {
        return _reminders.value[dateString] ?: emptyList()
    }

    /** Actualiza un recordatorio existente **/
    fun updateReminder(oldNote: String, newNote: String) {
        val dateKey = _selectedDate.value
        _reminders.update { map ->
            val newMap = map.toMutableMap()
            val notes = (newMap[dateKey] ?: mutableListOf()).toMutableList()
            val index = notes.indexOf(oldNote)
            if (index != -1) notes[index] = newNote
            newMap[dateKey] = notes
            newMap
        }
    }

    /** Elimina un recordatorio **/
    fun deleteReminder(note: String) {
        val dateKey = _selectedDate.value
        _reminders.update { map ->
            val newMap = map.toMutableMap()
            val notes = (newMap[dateKey] ?: mutableListOf()).toMutableList()
            notes.remove(note)
            if (notes.isEmpty()) newMap.remove(dateKey)
            else newMap[dateKey] = notes
            newMap
        }
    }
}
