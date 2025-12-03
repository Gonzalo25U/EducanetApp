package com.example.educanetapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserLocalPreferences {

    private val KEY_NAME = stringPreferencesKey("local_name")
    private val KEY_BIO = stringPreferencesKey("local_bio")
    private val KEY_PHOTO = stringPreferencesKey("local_photo")

    fun getName(context: Context): Flow<String?> =
        context.dataStore.data.map { it[KEY_NAME] }

    fun getBio(context: Context): Flow<String?> =
        context.dataStore.data.map { it[KEY_BIO] }

    fun getPhoto(context: Context): Flow<String?> =
        context.dataStore.data.map { it[KEY_PHOTO] }

    suspend fun saveName(context: Context, name: String) {
        context.dataStore.edit { it[KEY_NAME] = name }
    }

    suspend fun saveBio(context: Context, bio: String) {
        context.dataStore.edit { it[KEY_BIO] = bio }
    }

    suspend fun savePhoto(context: Context, photoUri: String) {
        context.dataStore.edit { it[KEY_PHOTO] = photoUri }
    }
}
