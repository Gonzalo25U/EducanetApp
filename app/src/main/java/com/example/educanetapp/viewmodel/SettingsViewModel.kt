package com.example.educanetapp.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Extensión para inicializar DataStore
private val Context.dataStore by preferencesDataStore(name = "user_settings")

class SettingsViewModel(private val context: Context) : ViewModel() {

    // Keys para DataStore
    private object Keys {
        val NAME = stringPreferencesKey("name")
        val BIO = stringPreferencesKey("bio")
        val PHOTO = stringPreferencesKey("photo")
    }

    // Flows para observar los valores
    val name: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.NAME] ?: ""
    }

    val bio: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.BIO] ?: ""
    }

    val photo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.PHOTO] ?: ""
    }

    // Funciones para actualizar los valores
    fun setName(newName: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[Keys.NAME] = newName
            }
        }
    }

    fun setBio(newBio: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[Keys.BIO] = newBio
            }
        }
    }

    fun setPhoto(newPhoto: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[Keys.PHOTO] = newPhoto
            }
        }
    }

    // Función para borrar todos los datos de usuario
    fun clearSettings() {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs.clear()
            }
        }
    }
}
