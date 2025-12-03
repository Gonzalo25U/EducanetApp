package com.example.educanetapp.repository.profile

import com.example.educanetapp.model.Grade
import com.example.educanetapp.model.Student

class ProfileRepositoryFake(
    private val fakeUsers: List<Student> = listOf(
        Student(
            name = "Juan Pérez",
            email = "test@example.com",
            password = "1234",
            rut = "11.111.111-1",
            phone = "123456789",
            photoUrl = "http://example.com/photo.png",
            biography = "Alumno destacado"
        )
    ),
    private val defaultGrades: List<Grade> = listOf(
        Grade("Matemáticas", 5.8),
        Grade("Lenguaje", 6.3),
        Grade("Historia", 5.5),
        Grade("Ciencias", 6.0)
    )
) : ProfileRepository {

    override suspend fun getProfile(email: String): Student? {
        return fakeUsers.find { it.email == email }
    }

    override suspend fun getGrades(email: String): List<Grade> {
        return defaultGrades
    }
}
