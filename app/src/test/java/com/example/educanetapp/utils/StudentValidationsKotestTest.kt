package com.example.educanetapp.utils

import com.example.educanetapp.model.Student
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.collections.shouldContainAll

/**
 * Test con Kotest Assertions avanzadas para StudentValidations
 *
 * Usamos DescribeSpec que permite agrupar tests relacionados con describe/it
 * similar a Jasmine/Mocha en JavaScript.
 */
class StudentValidationsKotestTest : DescribeSpec({

    // Student válido para reutilizar en tests
    val validStudent = Student(
        name = "Juan Pérez",
        email = "juan.perez@educanet.cl",
        password = "1234",
        rut = "12.345.678-9",
        phone = "987654321",
        photoUrl = "https://example.com/photo.jpg",
        biography = "Estudiante de ingeniería"
    )

    describe("hasValidEmail()") {

        it("debería validar emails con formato correcto") {
            val student = validStudent.copy(email = "test@example.com")
            student.hasValidEmail() shouldBe true
        }

        it("debería validar emails con dominios chilenos") {
            val student = validStudent.copy(email = "alumno@educanet.cl")
            student.hasValidEmail() shouldBe true
        }

        it("debería validar emails con puntos en el nombre") {
            val student = validStudent.copy(email = "juan.perez@gmail.com")
            student.hasValidEmail() shouldBe true
        }

        it("debería rechazar emails sin @") {
            val student = validStudent.copy(email = "testexample.com")
            student.hasValidEmail() shouldBe false
        }

        it("debería rechazar emails sin dominio") {
            val student = validStudent.copy(email = "test@")
            student.hasValidEmail() shouldBe false
        }

        it("debería rechazar emails vacíos") {
            val student = validStudent.copy(email = "")
            student.hasValidEmail() shouldBe false
        }

        it("debería rechazar emails con espacios") {
            val student = validStudent.copy(email = "test @example.com")
            student.hasValidEmail() shouldBe false
        }
    }

    describe("hasValidRut()") {

        it("debería validar RUT con formato correcto XX.XXX.XXX-X") {
            val student = validStudent.copy(rut = "12.345.678-9")
            student.hasValidRut() shouldBe true
        }

        it("debería validar RUT con dígito verificador K") {
            val student = validStudent.copy(rut = "12.345.678-K")
            student.hasValidRut() shouldBe true
        }

        it("debería validar RUT con dígito verificador k minúscula") {
            val student = validStudent.copy(rut = "12.345.678-k")
            student.hasValidRut() shouldBe true
        }

        it("debería validar RUT corto (1 dígito inicial)") {
            val student = validStudent.copy(rut = "1.234.567-8")
            student.hasValidRut() shouldBe true
        }

        it("debería rechazar RUT sin puntos") {
            val student = validStudent.copy(rut = "12345678-9")
            student.hasValidRut() shouldBe false
        }

        it("debería rechazar RUT sin guión") {
            val student = validStudent.copy(rut = "12.345.6789")
            student.hasValidRut() shouldBe false
        }

        it("debería rechazar RUT con formato incorrecto") {
            val student = validStudent.copy(rut = "123456789")
            student.hasValidRut() shouldBe false
        }
    }

    describe("hasValidPhone()") {

        it("debería validar teléfono de 9 dígitos") {
            val student = validStudent.copy(phone = "987654321")
            student.hasValidPhone() shouldBe true
        }

        it("debería rechazar teléfono con menos de 9 dígitos") {
            val student = validStudent.copy(phone = "12345678")
            student.hasValidPhone() shouldBe false
        }

        it("debería rechazar teléfono con más de 9 dígitos") {
            val student = validStudent.copy(phone = "9876543210")
            student.hasValidPhone() shouldBe false
        }

        it("debería rechazar teléfono con letras") {
            val student = validStudent.copy(phone = "98765432a")
            student.hasValidPhone() shouldBe false
        }

        it("debería rechazar teléfono con espacios") {
            val student = validStudent.copy(phone = "987 654 321")
            student.hasValidPhone() shouldBe false
        }

        it("debería rechazar teléfono con guiones") {
            val student = validStudent.copy(phone = "987-654-321")
            student.hasValidPhone() shouldBe false
        }
    }

    describe("hasCompleteProfile()") {

        it("debería considerar completo un perfil con foto y biografía") {
            val student = validStudent
            student.hasCompleteProfile() shouldBe true
        }

        it("debería considerar incompleto si falta la foto") {
            val student = validStudent.copy(photoUrl = null)
            student.hasCompleteProfile() shouldBe false
        }

        it("debería considerar incompleto si falta la biografía") {
            val student = validStudent.copy(biography = null)
            student.hasCompleteProfile() shouldBe false
        }

        it("debería considerar incompleto si la foto está vacía") {
            val student = validStudent.copy(photoUrl = "")
            student.hasCompleteProfile() shouldBe false
        }

        it("debería considerar incompleto si la biografía está vacía") {
            val student = validStudent.copy(biography = "   ")
            student.hasCompleteProfile() shouldBe false
        }

        it("debería considerar incompleto si faltan ambos campos") {
            val student = validStudent.copy(photoUrl = null, biography = null)
            student.hasCompleteProfile() shouldBe false
        }
    }

    describe("hasValidPassword()") {

        it("debería validar contraseña de 4 caracteres (mínimo)") {
            val student = validStudent.copy(password = "1234")
            student.hasValidPassword() shouldBe true
        }

        it("debería validar contraseña más larga") {
            val student = validStudent.copy(password = "contraseñaSegura123")
            student.hasValidPassword() shouldBe true
        }

        it("debería rechazar contraseña de 3 caracteres") {
            val student = validStudent.copy(password = "123")
            student.hasValidPassword() shouldBe false
        }

        it("debería rechazar contraseña vacía") {
            val student = validStudent.copy(password = "")
            student.hasValidPassword() shouldBe false
        }
    }

    describe("isValid() - validación completa") {

        it("debería validar student con todos los campos correctos") {
            validStudent.isValid() shouldBe true
        }

        it("debería rechazar student con nombre vacío") {
            val student = validStudent.copy(name = "")
            student.isValid() shouldBe false
        }

        it("debería rechazar student con email inválido") {
            val student = validStudent.copy(email = "emailinvalido")
            student.isValid() shouldBe false
        }

        it("debería rechazar student con RUT inválido") {
            val student = validStudent.copy(rut = "123456789")
            student.isValid() shouldBe false
        }

        it("debería rechazar student con teléfono inválido") {
            val student = validStudent.copy(phone = "123")
            student.isValid() shouldBe false
        }

        it("debería rechazar student con contraseña corta") {
            val student = validStudent.copy(password = "12")
            student.isValid() shouldBe false
        }

        it("student válido debería tener nombre no vacío") {
            validStudent.name shouldNotBe ""
            validStudent.name.shouldNotBeBlank()
        }

        it("student válido debería tener email con @") {
            validStudent.email shouldContain "@"
        }

        it("student válido debería tener RUT con formato correcto") {
            validStudent.rut shouldMatch "^\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK]$"
        }
    }

    describe("Casos extremos y edge cases") {

        it("debería manejar emails con múltiples puntos") {
            val student = validStudent.copy(email = "test.user.name@example.com")
            student.hasValidEmail() shouldBe true
        }

        it("debería manejar nombres con espacios") {
            val student = validStudent.copy(name = "Juan Carlos Pérez")
            student.isValid() shouldBe true
        }

        it("debería rechazar multiple validaciones incorrectas a la vez") {
            val invalidStudent = Student(
                name = "",
                email = "invalido",
                password = "12",
                rut = "invalido",
                phone = "123"
            )
            invalidStudent.isValid() shouldBe false
            invalidStudent.hasValidEmail() shouldBe false
            invalidStudent.hasValidRut() shouldBe false
            invalidStudent.hasValidPhone() shouldBe false
            invalidStudent.hasValidPassword() shouldBe false
        }
    }
})