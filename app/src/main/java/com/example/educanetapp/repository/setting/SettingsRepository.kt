package com.example.educanetapp.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.educanetapp.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    companion object {
        val BIO_KEY = stringPreferencesKey("local_bio")
        val PHOTO_KEY = stringPreferencesKey("local_photo")
    }

    val bio: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[BIO_KEY] ?: ""
    }

    val photo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PHOTO_KEY] ?: ""
    }

    suspend fun saveBio(biography: String) {
        context.dataStore.edit { prefs ->
            prefs[BIO_KEY] = biography
        }
    }

    suspend fun savePhoto(photoUri: String) {
        context.dataStore.edit { prefs ->
            prefs[PHOTO_KEY] = photoUri
        }
    }
}
