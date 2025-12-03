package com.example.educanetapp.utils

import com.example.educanetapp.model.Grade
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.string.shouldContain

/**
 * Test con Kotest para GradeExtensions
 *
 * Kotest ofrece un estilo más expresivo y legible que JUnit tradicional.
 * Usamos FunSpec que es compatible con JUnit Platform.
 */
class GradeExtensionsKotestTest : FunSpec({

    // ==================== isApproved() ====================

    test("Grade con nota 4.0 debería estar aprobada") {
        val grade = Grade("Matemáticas", 4.0)
        grade.isApproved() shouldBe true
    }

    test("Grade con nota 5.5 debería estar aprobada") {
        val grade = Grade("Historia", 5.5)
        grade.isApproved() shouldBe true
    }

    test("Grade con nota 3.9 NO debería estar aprobada") {
        val grade = Grade("Lenguaje", 3.9)
        grade.isApproved() shouldBe false
    }

    test("Grade con nota 1.0 NO debería estar aprobada") {
        val grade = Grade("Física", 1.0)
        grade.isApproved() shouldBe false
    }

    // ==================== getCategory() ====================

    test("Grade con nota 6.5 debería ser categoría Excelente") {
        val grade = Grade("Química", 6.5)
        grade.getCategory() shouldBe "Excelente"
    }

    test("Grade con nota 6.0 exacto debería ser categoría Excelente") {
        val grade = Grade("Biología", 6.0)
        grade.getCategory() shouldBe "Excelente"
    }

    test("Grade con nota 5.5 debería ser categoría Bueno") {
        val grade = Grade("Inglés", 5.5)
        grade.getCategory() shouldBe "Bueno"
    }

    test("Grade con nota 4.5 debería ser categoría Suficiente") {
        val grade = Grade("Arte", 4.5)
        grade.getCategory() shouldBe "Suficiente"
    }

    test("Grade con nota 3.5 debería ser categoría Insuficiente") {
        val grade = Grade("Música", 3.5)
        grade.getCategory() shouldBe "Insuficiente"
    }

    test("Categoría Excelente debería contener la palabra Excelente") {
        val grade = Grade("Test", 6.8)
        grade.getCategory() shouldContain "Excelente"
    }

    // ==================== isValid() ====================

    test("Grade con nota 1.0 debería ser válida (límite inferior)") {
        val grade = Grade("Materia", 1.0)
        grade.isValid() shouldBe true
    }

    test("Grade con nota 7.0 debería ser válida (límite superior)") {
        val grade = Grade("Materia", 7.0)
        grade.isValid() shouldBe true
    }

    test("Grade con nota 4.5 debería ser válida (valor medio)") {
        val grade = Grade("Materia", 4.5)
        grade.isValid() shouldBe true
    }

    test("Grade con nota 0.9 NO debería ser válida (bajo límite)") {
        val grade = Grade("Materia", 0.9)
        grade.isValid() shouldBe false
    }

    test("Grade con nota 7.1 NO debería ser válida (sobre límite)") {
        val grade = Grade("Materia", 7.1)
        grade.isValid() shouldBe false
    }

    test("Grade con nota negativa NO debería ser válida") {
        val grade = Grade("Materia", -1.0)
        grade.isValid() shouldBe false
    }

    // ==================== roundScore() ====================

    test("Grade con nota 5.567 debería redondearse a 5.6") {
        val grade = Grade("Matemáticas", 5.567)
        val rounded = grade.roundScore()
        rounded.score shouldBe 5.6
    }

    test("Grade con nota 4.01 debería redondearse a 4.0") {
        val grade = Grade("Historia", 4.01)
        val rounded = grade.roundScore()
        rounded.score shouldBe 4.0
    }

    test("Grade redondeada debería mantener el subject original") {
        val grade = Grade("Química", 6.789)
        val rounded = grade.roundScore()
        rounded.subject shouldBe "Química"
    }

    // ==================== distanceToApproval() ====================

    test("Grade con nota 3.5 debería estar a 0.5 puntos de aprobar") {
        val grade = Grade("Física", 3.5)
        grade.distanceToApproval() shouldBe 0.5
    }

    test("Grade con nota 2.0 debería estar a 2.0 puntos de aprobar") {
        val grade = Grade("Inglés", 2.0)
        grade.distanceToApproval() shouldBe 2.0
    }

    test("Grade con nota 4.0 debería estar a 0.0 puntos de aprobar") {
        val grade = Grade("Arte", 4.0)
        grade.distanceToApproval() shouldBe 0.0
    }

    test("Grade con nota 5.0 debería tener distancia negativa (ya aprobó)") {
        val grade = Grade("Música", 5.0)
        grade.distanceToApproval() shouldBeLessThan 0.0
    }

    test("Grade con nota 6.5 debería estar por sobre la aprobación") {
        val grade = Grade("Deportes", 6.5)
        grade.distanceToApproval() shouldBeLessThan 0.0
    }

    test("Grade con nota 1.0 debería estar lejos de aprobar") {
        val grade = Grade("Test", 1.0)
        grade.distanceToApproval() shouldBeGreaterThan 2.0
    }
})