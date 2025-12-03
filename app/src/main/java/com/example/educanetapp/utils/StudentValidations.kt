package com.example.educanetapp.utils

import com.example.educanetapp.model.Student


  //Validaciones para Student


// Valida formato de email
fun Student.hasValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    return email.matches(emailRegex.toRegex())
}

// Valida formato de RUT chileno (básico: XX.XXX.XXX-X)
fun Student.hasValidRut(): Boolean {
    val rutRegex = "^\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK]$"
    return rut.matches(rutRegex.toRegex())
}

// Valida teléfono (9 dígitos)
fun Student.hasValidPhone(): Boolean {
    return phone.matches("^\\d{9}$".toRegex())
}

// Verifica si el perfil está completo (con foto y biografía)
fun Student.hasCompleteProfile(): Boolean {
    return !photoUrl.isNullOrBlank() && !biography.isNullOrBlank()
}

// Valida que la contraseña tenga mínimo 4 caracteres
fun Student.hasValidPassword(): Boolean {
    return password.length >= 4
}

// Verifica si todos los campos obligatorios están presentes
fun Student.isValid(): Boolean {
    return name.isNotBlank() &&
            hasValidEmail() &&
            hasValidRut() &&
            hasValidPhone() &&
            hasValidPassword()
}