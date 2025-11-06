package com.example.educanetapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage // Importar para cargar la imagen desde la URI
import com.example.educanetapp.R
import com.example.educanetapp.model.Student
import com.example.educanetapp.model.Grade
import com.example.educanetapp.viewmodel.ProfileViewModel
import com.example.educanetapp.viewmodel.ProfileViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel // MODIFICADO: Recibimos el ViewModel directamente
) {
    // val context = LocalContext.current // YA NO ES NECESARIO AQUÍ
    // val viewModel: ProfileViewModel = viewModel( // ELIMINADO: El ViewModel se crea en NavGraph
    //     factory = ProfileViewModelFactory(context, userEmail)
    // )

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Estudiante") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val student: Student? = uiState.student
                val grades: List<Grade> = uiState.grades ?: emptyList()

                if (student != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Foto de perfil (MODIFICADO para CÁMARA)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Usamos AsyncImage de Coil para cargar la imagen
                            AsyncImage(
                                // Usamos la URI capturada (del UiState)
                                // Si es null, usamos el drawable por defecto
                                model = uiState.capturedImageUri ?: R.drawable.ic_launcher_foreground,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape), // Redondear la imagen
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
                            )

                            // Botón para editar la foto (NUEVO)
                            IconButton(
                                onClick = { navController.navigate("camera") }, // Navega a la ruta de la cámara
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(end = 100.dp) // Ajustar para que quede sobre la foto
                                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.PhotoCamera,
                                    contentDescription = "Cambiar foto de perfil"
                                )
                            }
                        }

                        // Datos del estudiante
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Nombre: ${student.name}", style = MaterialTheme.typography.bodyLarge)
                            Text("RUT: ${student.rut}", style = MaterialTheme.typography.bodyLarge)
                            Text("Teléfono: ${student.phone}", style = MaterialTheme.typography.bodyLarge)
                            Text("Correo: ${student.email}", style = MaterialTheme.typography.bodyLarge)
                        }

                        // Notas
                        if (grades.isNotEmpty()) {
                            Text(
                                text = "Notas",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            grades.forEach { grade ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(grade.subject)
                                        Text(
                                            text = grade.score.toString(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Botones de navegación
                        Button(
                            onClick = { navController.navigate("resources") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver recursos educativos")
                        }

                        Button(
                            onClick = { navController.navigate("agenda") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Abrir agenda")
                        }

                        Button(
                            onClick = {
                                viewModel.logout()
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar sesión", color = MaterialTheme.colorScheme.onError)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    Text(
                        "No se encontró el perfil del estudiante.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}