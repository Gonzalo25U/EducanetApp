package com.example.educanetapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educanetapp.R
import com.example.educanetapp.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Estudiante") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
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
                uiState.student?.let { student ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // Foto del estudiante (opcional)
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (student.photoUrl == null) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier.size(120.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    // En un futuro podrías cargar desde URL con Coil
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Nombre: ${student.name}", style = MaterialTheme.typography.bodyLarge)
                                Text("RUT: ${student.rut}", style = MaterialTheme.typography.bodyLarge)
                                Text("Teléfono: ${student.phone}", style = MaterialTheme.typography.bodyLarge)
                                Text("Correo: ${student.email}", style = MaterialTheme.typography.bodyLarge)
                            }
                        }

                        item {
                            Text(
                                text = "Notas",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        items(uiState.grades) { grade ->
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

                        // --- Botón para acceder a la pantalla de recursos ---
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { navController.navigate("resources") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Ver recursos educativos")
                            }
                        }

                        // --- Botón para acceder a la pantalla de agenda ---
                        item {
                            Button(
                                onClick = { navController.navigate("agenda") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Abrir agenda")
                            }
                        }
                    }
                }
            }
        }
    }
}
