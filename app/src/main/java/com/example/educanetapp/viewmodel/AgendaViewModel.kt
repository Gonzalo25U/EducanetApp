package com.example.educanetapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AgendaViewModel : ViewModel() {

    private val _selectedDate = MutableStateFlow(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _reminders = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val reminders: StateFlow<Map<String, List<String>>> = _reminders.asStateFlow()

    fun selectDate(dateStr: String) {
        _selectedDate.value = dateStr
    }

    fun getRemindersForDate(dateStr: String): List<String> {
        return _reminders.value[dateStr] ?: emptyList()
    }

    fun addReminder(note: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val currentNotes = _reminders.value[date]?.toMutableList() ?: mutableListOf()
            currentNotes.add(note)
            _reminders.value = _reminders.value + (date to currentNotes)
        }
    }

    fun updateReminder(oldNote: String, newNote: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val currentNotes = _reminders.value[date]?.toMutableList() ?: return@launch
            val index = currentNotes.indexOf(oldNote)
            if (index != -1) {
                currentNotes[index] = newNote
                _reminders.value = _reminders.value + (date to currentNotes)
            }
        }
    }

    fun deleteReminder(note: String) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val currentNotes = _reminders.value[date]?.toMutableList() ?: return@launch
            currentNotes.remove(note)
            _reminders.value = _reminders.value + (date to currentNotes)
        }
    }
}
