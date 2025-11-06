package com.example.educanetapp.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClassesViewModel : ViewModel() {

    private val _cameraPermissionGranted = MutableStateFlow(false)
    val cameraPermissionGranted = _cameraPermissionGranted.asStateFlow()

    private val _microPermissionGranted = MutableStateFlow(false)
    val microPermissionGranted = _microPermissionGranted.asStateFlow()

    fun checkPermissions(context: Context) {
        _cameraPermissionGranted.value =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        _microPermissionGranted.value =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    fun updateCameraPermission(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }

    fun updateMicroPermission(granted: Boolean) {
        _microPermissionGranted.value = granted
    }
}
