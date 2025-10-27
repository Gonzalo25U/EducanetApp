package com.example.educanetapp.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

val Context.dataStore by preferencesDataStore("educanet_prefs")

object DataStoreManager {

    private val USERS_KEY = stringPreferencesKey("registered_users")

    suspend fun saveUsers(context: Context, users: List<Map<String, String>>) {
        val jsonArray = JSONArray()
        users.forEach { user ->
            val obj = JSONObject()
            user.forEach { (key, value) ->
                obj.put(key, value)
            }
            jsonArray.put(obj)
        }

        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = jsonArray.toString()
        }
    }

    suspend fun loadUsers(context: Context): List<Map<String, String>> {
        return context.dataStore.data.map { prefs ->
            prefs[USERS_KEY]?.let { json ->
                val arr = JSONArray(json)
                List(arr.length()) { i ->
                    val obj = arr.getJSONObject(i)
                    obj.keys().asSequence().associateWith { obj.getString(it) }
                }
            } ?: emptyList()
        }.first()
    }
}