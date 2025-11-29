package com.example.educanetapp.model

import java.util.Date

/**
 * Modelo de datos para una clase virtual
 */
data class VirtualClass(
    val id: String,
    val subject: String,
    val teacher: String,
    val date: Date,
    val duration: Int, // Duración en minutos
    val roomId: String,
    val isLive: Boolean = false,
    val participantsCount: Int = 0,
    val description: String = "",
    val meetingLink: String = "" // Para integrar con Zoom, Jitsi, etc.
)

/**
 * Estado de UI para la pantalla de clases virtuales
 */
data class VirtualClassesUiState(
    val isLoading: Boolean = false,
    val classes: List<VirtualClass> = emptyList(),
    val error: String? = null
) {
    val liveClasses: List<VirtualClass>
        get() = classes.filter { it.isLive }

    val scheduledClasses: List<VirtualClass>
        get() = classes.filter { !it.isLive }

    val hasClasses: Boolean
        get() = classes.isNotEmpty()
}