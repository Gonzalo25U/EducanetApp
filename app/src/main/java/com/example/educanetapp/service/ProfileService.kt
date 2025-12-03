package com.example.educanetapp.service

import com.example.educanetapp.model.Grade
import com.example.educanetapp.model.Student
import com.example.educanetapp.repository.profile.ProfileRepository

class ProfileService(private val repository: ProfileRepository) {

    // Devuelve el perfil de un estudiante por email
    suspend fun fetchStudentProfile(email: String): Student? {
        return repository.getProfile(email)
    }

    // Devuelve las notas de un estudiante por email
    suspend fun fetchStudentGrades(email: String): List<Grade> {
        return repository.getGrades(email)
    }
}
