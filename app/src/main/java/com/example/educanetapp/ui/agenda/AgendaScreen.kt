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
    //fecha actual seleccionada
    val selectedDateStr by viewModel.selectedDate.collectAsState()
    //fechas y las notas existentes
    val remindersMap by viewModel.reminders.collectAsState()

    //guarda la nota en una nueva variable local
    var newNote by remember { mutableStateOf("") }

    //guarda los datos para que la pantalla no se recomponga (remember) y recalcula el resultado
    //para obtener una lista actualizada
    val notesForDate by remember(selectedDateStr, remindersMap) {
        derivedStateOf { viewModel.getRemindersForDate(selectedDateStr) }
    }

    //formatea las fechas para que la interna de el usuario coincida con el de base
    val displayFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val backendFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    Scaffold(
        //barra superior con título y botón para volver atrás.
        topBar = {
            TopAppBar(
                title = { Text("Agenda del Estudiante") },
                navigationIcon = {
                    //retrocede a la pantalla anterior
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás")
                    }
                }
            )
        },
        //botón flotante para agregar un nuevo recordatorio.
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (newNote.isNotBlank()) {
                        viewModel.addReminder(newNote.trim())
                        newNote = ""
                    }
                },
                containerColor = if (newNote.isBlank())
                    MaterialTheme.colorScheme.surfaceVariant
                else
                    MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        //Es el contenido principal en este caso lista las fechas y recordatorios.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Calendario
            item {
                Text("Selecciona una fecha:", style = MaterialTheme.typography.titleMedium)
                CalendarRow(
                    selectedDateStr = selectedDateStr,
                    onDateSelected = { viewModel.selectDate(it) },
                    backendFormatter = backendFormatter,
                    displayFormatter = displayFormatter
                )
            }

            // Campo para nueva nota
            item {
                OutlinedTextField(
                    value = newNote,
                    onValueChange = { newNote = it },
                    label = { Text("Nuevo recordatorio") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Título de recordatorios
            item {
                Divider()
                val parsedDate: Date? = try { backendFormatter.parse(selectedDateStr) } catch (e: Exception) { null }
                val readableDate = parsedDate?.let { displayFormatter.format(it) } ?: selectedDateStr
                Text("Recordatorios para $readableDate:", style = MaterialTheme.typography.titleMedium)
            }

            // Lista de recordatorios
            if (notesForDate.isEmpty()) {
                item {
                    Text("No hay recordatorios para esta fecha.")
                }
            } else {
                items(notesForDate) { note ->
                    var isEditing by remember { mutableStateOf(false) }
                    var editedText by remember { mutableStateOf(note) }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        if (isEditing) {
                            //modo edicion
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
                                    //guarda lo editado
                                    Icon(Icons.Default.Check, contentDescription = "Guardar")
                                }
                                    //cancela la edicion
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
                                    //Activa el modo edicion
                                    IconButton(onClick = { isEditing = true }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    //Activa el modo eliminar
                                    IconButton(onClick = { viewModel.deleteReminder(note) }) {
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

//componente que muestra un boton con el dia actual, 3 dias antes y 3 dias despues(una semana)
@Composable
private fun CalendarRow(
    selectedDateStr: String,
    onDateSelected: (String) -> Unit,
    backendFormatter: SimpleDateFormat,
    displayFormatter: SimpleDateFormat
) {
    //trata de convertir el texto de la fecha actual en un objeto(Date) y si falla usa la fecha actual
    val cal = Calendar.getInstance()
    try { cal.time = backendFormatter.parse(selectedDateStr) ?: Date() } catch (_: Exception) { cal.time = Date() }

    //lista de los 7 dias
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
    //muestra la fecha formateada
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { date ->
            val dateStr = backendFormatter.format(date)
            val isSelected = dateStr == selectedDateStr
            //propiedades de el boton
            Button(
                //desde aqui se actualiza el view model
                onClick = { onDateSelected(dateStr) },
                colors = ButtonDefaults.buttonColors(
                    //para cambiar el color de el boton al seleccionar
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(displayFormatter.format(date))
            }
        }
    }
}
