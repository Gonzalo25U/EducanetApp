package com.example.educanetapp.ui.resources

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educanetapp.model.Resource
import com.example.educanetapp.model.ResourceType
import com.example.educanetapp.viewmodel.ResourceViewModel
import com.example.educanetapp.viewmodel.ResourceViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesScreen(navController: NavController) {

    val context = LocalContext.current

    // ✔ CORRECCIÓN: ahora tu VM sí recibe context
    val viewModel: ResourceViewModel = viewModel(
        factory = ResourceViewModelFactory(context)
    )

    val resources by viewModel.resources.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<ResourceType?>(null) }

    // ✔ Cargar recursos correctamente
    LaunchedEffect(selectedType) {
        if (selectedType == null) {
            viewModel.loadResources()
        } else {
            viewModel.loadResourcesByType(selectedType!!)
        }
    }

    val filteredResources = remember(resources, searchQuery) {
        if (searchQuery.isBlank()) resources
        else resources.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    (it.description?.contains(searchQuery, ignoreCase = true) == true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Recursos Educativos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = error ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                filteredResources.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se encontraron recursos")
                    }
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {


                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ResourceType.values().forEach { type ->
                                    FilterChip(
                                        selected = selectedType == type,
                                        onClick = {
                                            selectedType =
                                                if (selectedType == type) null else type
                                        },
                                        label = { Text(type.name) }
                                    )
                                }
                            }
                        }

                        items(filteredResources) { resource ->
                            ResourceCard(resource) {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(resource.link))
                                context.startActivity(intent)
                            }
                        }

                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResourceCard(resource: Resource, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                imageVector = getTypeIcon(resource.type),
                contentDescription = null
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(resource.title, fontWeight = FontWeight.Bold)
                Text(resource.description ?: "Sin descripción", maxLines = 2)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun getTypeIcon(type: ResourceType): ImageVector = when (type) {
    ResourceType.LIBRO -> Icons.Default.Book
    ResourceType.ARTICULO -> Icons.Default.Article
    ResourceType.VIDEO -> Icons.Default.PlayCircle
}