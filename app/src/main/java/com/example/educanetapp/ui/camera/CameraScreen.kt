package com.example.educanetapp.ui.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    onImageCaptured: (Uri) -> Unit,
    onNavigateBack: () -> Unit

    ) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1. Gestión de Permisos usando Accompanist Permissions
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    LaunchedEffect(cameraPermissionState.status) {
        // Lanza la solicitud de permiso si no está concedido
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Capturar Imagen") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        // Icono para volver
                        Icon(Icons.Default.Close, contentDescription = "Cerrar Cámara")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                // Si se concede el permiso, mostramos el Composable de la cámara
                cameraPermissionState.status.isGranted -> {
                    CameraXPreview(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        onImageCaptured = onImageCaptured
                    )
                }
                // Si el permiso es denegado o no solicitado, mostramos un mensaje
                else -> {
                    Text(
                        text = "Permiso de cámara es necesario para esta funcionalidad.",
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text("Solicitar Permiso")
                    }
                }
            }
        }
    }
}

/**
 * Composable que contiene la vista previa de CameraX y el botón de captura.
 */
@Composable
fun CameraXPreview(
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    onImageCaptured: (Uri) -> Unit
) {
    // Caso de uso para la captura de imagen
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Contenedor principal de la cámara
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. AndroidView para mostrar el PreviewView (Vista previa de la cámara)
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    // Configuración del caso de uso de vista previa
                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    // Selecciona la cámara trasera por defecto
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        // Desvincula los casos de uso anteriores
                        cameraProvider.unbindAll()

                        // Vincula los casos de uso (vista previa y captura) al ciclo de vida
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (exc: Exception) {
                        Log.e("CAMERA_SCREEN", "El enlace de CameraX falló", exc)
                        Toast.makeText(context, "Error al iniciar cámara", Toast.LENGTH_SHORT).show()
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )

        // 2. Botón de Captura de Foto
        FloatingActionButton(
            onClick = {
                takePhoto(context, imageCapture, onImageCaptured)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Camera, contentDescription = "Tomar Foto")
        }
    }
}

/**
 * Función para ejecutar la captura de la foto.
 *
 * @param context Contexto de la aplicación.
 * @param imageCapture Caso de uso de captura de imagen de CameraX.
 * @param onImageCaptured Callback al terminar la captura.
 */
private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri) -> Unit
) {
    // Crea un archivo temporal con un timestamp para el nombre
    val photoFile = File(
        context.filesDir,
        "educanet_profile_${System.currentTimeMillis()}.jpg"
    )

    // Opciones de salida para el archivo
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    // Ejecutor donde se realiza el callback
    val executor: Executor = ContextCompat.getMainExecutor(context)

    // Inicia la captura de imagen
    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e("CAMERA_SCREEN", "Error al guardar foto: ${exc.message}", exc)
                Toast.makeText(context, "Error al tomar foto", Toast.LENGTH_SHORT).show()
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                Log.d("CAMERA_SCREEN", "Foto guardada en: $savedUri")
                Toast.makeText(context, "¡Foto capturada con éxito!", Toast.LENGTH_SHORT).show()
                // Llama al callback para notificar que la imagen fue guardada
                onImageCaptured(savedUri)
            }
        }
    )
}