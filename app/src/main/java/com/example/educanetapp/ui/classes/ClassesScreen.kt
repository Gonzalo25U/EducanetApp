package com.example.educanetapp.ui.classes

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educanetapp.viewmodel.ClassesViewModel

@Composable
fun ClassesScreen(navController: NavController, viewModel: ClassesViewModel = viewModel()) {
    val context = LocalContext.current

    val cameraGranted by viewModel.cameraPermissionGranted.collectAsState()
    val micGranted by viewModel.microPermissionGranted.collectAsState()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> viewModel.updateCameraPermission(granted) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> viewModel.updateMicroPermission(granted) }

    LaunchedEffect(Unit) {
        viewModel.checkPermissions(context)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Clases virtuales",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = "Video",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (cameraGranted && micGranted) {
                Text("Permisos concedidos ✅")
                Text("Puedes acceder a tu clase virtual.")
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { navController.navigate("clasesVirtuales") }) {
                    Text("Entrar a clase")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 🔹 Botón de retroceso
                Button(onClick = { navController.popBackStack() }) {
                    Text("Volver")
                }

            } else {
                Text("Necesitamos permisos para la cámara y el micrófono.")
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }) {
                    Text("Conceder permisos")
                }
            }
        }
    }
}

