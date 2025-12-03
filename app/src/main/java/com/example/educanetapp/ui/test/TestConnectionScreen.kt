package com.example.educanetapp.ui.test

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.educanetapp.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun TestConnectionScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var resultText by remember { mutableStateOf("Presiona el botón para probar la conexión") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(text = resultText)

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        resultText = "Conectando..."
                        try {
                            // Llamada al backend usando el Context
                            val response = RetrofitInstance.getResourceApi(context).getAllResources()
                            resultText = "Conexión exitosa! Recursos recibidos: ${response.size}"
                        } catch (e: Exception) {
                            resultText = "Error de conexión: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(text = if (isLoading) "Cargando..." else "Probar conexión")
            }
        }
    }
}
