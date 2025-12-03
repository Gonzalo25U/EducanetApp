package com.example.educanetapp.utils

import com.example.educanetapp.model.Grade
import kotlin.math.roundToInt

/**
 * Extensiones de utilidad para la clase Grade
 */

// Verifica si la nota es aprobatoria (>= 4.0 en escala chilena)
fun Grade.isApproved(): Boolean = score >= 4.0

// Obtiene la categoría de la nota
fun Grade.getCategory(): String = when {
    score >= 6.0 -> "Excelente"
    score >= 5.0 -> "Bueno"
    score >= 4.0 -> "Suficiente"
    else -> "Insuficiente"
}

// Verifica si la nota es válida (entre 1.0 y 7.0)
fun Grade.isValid(): Boolean = score in 1.0..7.0

// Redondea la nota a un decimal
fun Grade.roundScore(): Grade {
    val rounded = (score * 10).roundToInt() / 10.0
    return copy(score = rounded)
}

// Calcula la diferencia con la nota mínima de aprobación
fun Grade.distanceToApproval(): Double = 4.0 - score