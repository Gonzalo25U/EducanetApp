package com.example.educanetapp.repository.profile

import com.example.educanetapp.model.Grade
import com.example.educanetapp.model.Student

interface ProfileRepository {
    suspend fun getProfile(email: String): Student?
    suspend fun getGrades(email: String): List<Grade>
}
