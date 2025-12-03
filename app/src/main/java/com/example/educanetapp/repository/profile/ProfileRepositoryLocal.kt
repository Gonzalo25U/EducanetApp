package com.example.educanetapp.repository.profile

import android.content.Context
import com.example.educanetapp.model.Grade
import com.example.educanetapp.model.Student
import com.example.educanetapp.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryLocal(
    private val context: Context
) : ProfileRepository {

    override suspend fun getProfile(email: String): Student? = withContext(Dispatchers.IO) {
        val users = DataStoreManager.loadUsers(context)
        val studentData = users.find { it["email"] == email } ?: return@withContext null

        Student(
            name = studentData["name"] ?: "",
            email = studentData["email"] ?: "",
            password = studentData["password"] ?: "",
            rut = studentData["rut"] ?: "",
            phone = studentData["phone"] ?: "",
            photoUrl = studentData["photoUrl"],
            biography = studentData["biography"]
        )
    }

    override suspend fun getGrades(email: String): List<Grade> = withContext(Dispatchers.Default) {
        // Por ahora datos locales estáticos — luego esto vendrá del backend
        listOf(
            Grade("Matemáticas", 5.8),
            Grade("Lenguaje", 6.3),
            Grade("Historia", 5.5),
            Grade("Ciencias", 6.0)
        )
    }
}
