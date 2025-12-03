package com.example.educanetapp.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Esta es la única definición de dataStore para toda la app
val Context.dataStore by preferencesDataStore(name = "educanet_prefs")
