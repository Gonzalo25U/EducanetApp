package com.example.educanetapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResourceViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResourceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResourceViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
