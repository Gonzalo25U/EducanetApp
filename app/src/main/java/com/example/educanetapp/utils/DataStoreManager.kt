package com.example.educanetapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

object DataStoreManager {

    private val USERS_KEY = stringPreferencesKey("registered_users")
    private val REMINDERS_KEY = stringPreferencesKey("agenda_reminders")

    // ==================== USUARIOS ====================
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

    // ==================== RECORDATORIOS ====================
    suspend fun saveReminders(context: Context, reminders: Map<String, List<String>>) {
        val jsonObject = JSONObject()
        reminders.forEach { (date, notes) ->
            val notesArray = JSONArray()
            notes.forEach { note -> notesArray.put(note) }
            jsonObject.put(date, notesArray)
        }

        context.dataStore.edit { prefs ->
            prefs[REMINDERS_KEY] = jsonObject.toString()
        }
    }

    suspend fun loadReminders(context: Context): Map<String, List<String>> {
        return context.dataStore.data.map { prefs ->
            prefs[REMINDERS_KEY]?.let { json ->
                val jsonObject = JSONObject(json)
                val remindersMap = mutableMapOf<String, List<String>>()
                jsonObject.keys().forEach { date ->
                    val notesArray = jsonObject.getJSONArray(date)
                    val notesList = mutableListOf<String>()
                    for (i in 0 until notesArray.length()) {
                        notesList.add(notesArray.getString(i))
                    }
                    remindersMap[date] = notesList
                }
                remindersMap
            } ?: emptyMap()
        }.first()
    }

    suspend fun clearReminders(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(REMINDERS_KEY)
        }
    }
}
