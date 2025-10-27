package com.example.educanetapp.ui.agenda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educanetapp.viewmodel.AgendaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(navController: NavController, viewModel: AgendaViewModel = viewModel()) {
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    val remindersMap by viewModel.reminders.collectAsState()

    var newNote by remember { mutableStateOf("") }
    val notesForDate = viewModel.getRemindersForDate(selectedDateStr)

    // Formatters
    val displayFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val backendFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agenda del Estudiante") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        },
        floatingActionButton = {
            if (newNote.isNotBlank()) {
                FloatingActionButton(onClick = {
                    viewModel.addReminder(newNote.trim())
                    newNote = ""
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Selecciona una fecha:", style = MaterialTheme.typography.titleMedium)

            CalendarRow(
                selectedDateStr = selectedDateStr,
                onDateSelected = { dateStr -> viewModel.selectDate(dateStr) },
                backendFormatter = backendFormatter,
                displayFormatter = displayFormatter
            )

            OutlinedTextField(
                value = newNote,
                onValueChange = { newNote = it },
                label = { Text("Nuevo recordatorio") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            val parsedDate: Date? = try {
                backendFormatter.parse(selectedDateStr)
            } catch (e: Exception) {
                null
            }
            val readableDate = parsedDate?.let { displayFormatter.format(it) } ?: selectedDateStr

            Text(
                "Recordatorios para $readableDate:",
                style = MaterialTheme.typography.titleMedium
            )

            if (notesForDate.isEmpty()) {
                Text("No hay recordatorios para esta fecha.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(notesForDate) { note ->
                        var isEditing by remember { mutableStateOf(false) }
                        var editedText by remember { mutableStateOf(note) }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            if (isEditing) {
                                Row(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = editedText,
                                        onValueChange = { editedText = it },
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = {
                                        viewModel.updateReminder(note, editedText)
                                        isEditing = false
                                    }) {
                                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                                    }
                                    IconButton(onClick = { isEditing = false }) {
                                        Icon(Icons.Default.Close, contentDescription = "Cancelar")
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(note, style = MaterialTheme.typography.bodyLarge)
                                    Row {
                                        IconButton(onClick = { isEditing = true }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                                        }
                                        IconButton(onClick = {
                                            viewModel.deleteReminder(note)
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarRow(
    selectedDateStr: String,
    onDateSelected: (String) -> Unit,
    backendFormatter: SimpleDateFormat,
    displayFormatter: SimpleDateFormat
) {
    val cal = Calendar.getInstance()
    try {
        cal.time = backendFormatter.parse(selectedDateStr) ?: Date()
    } catch (_: Exception) {
        cal.time = Date()
    }

    val days = remember(selectedDateStr) {
        val list = mutableListOf<Date>()
        for (i in -3..3) {
            val c = Calendar.getInstance()
            c.time = cal.time
            c.add(Calendar.DAY_OF_MONTH, i)
            list.add(c.time)
        }
        list
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { date ->
            val dateStr = backendFormatter.format(date)
            val isSelected = dateStr == selectedDateStr
            Button(
                onClick = { onDateSelected(dateStr) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
            ) {
                Text(displayFormatter.format(date))
            }
        }
    }
}
