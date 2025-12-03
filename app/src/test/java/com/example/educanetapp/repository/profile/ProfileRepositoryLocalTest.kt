package com.example.educanetapp.repository.profile

import android.content.Context
import com.example.educanetapp.model.Student
import com.example.educanetapp.utils.DataStoreManager
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Test con MockK para ProfileRepositoryLocal
 *
 * MockK permite crear "mocks" (simulaciones) de dependencias como Context y DataStoreManager
 * para testear la lógica sin necesidad de Android real.
 */
class ProfileRepositoryLocalTest {

    // Mocks - Simulaciones de las dependencias
    private lateinit var mockContext: Context
    private lateinit var repository: ProfileRepositoryLocal

    @BeforeEach
    fun setup() {
        // Crear un mock de Context (no necesitamos Android real)
        mockContext = mockk(relaxed = true)

        // Crear el repository con el mock
        repository = ProfileRepositoryLocal(mockContext)

        // MockK necesita mockear métodos estáticos con mockkObject
        mockkObject(DataStoreManager)
    }

    @AfterEach
    fun tearDown() {
        // Limpiar todos los mocks después de cada test
        unmockkAll()
    }

    @Test
    fun `getProfile returns student when user exists`() = runTest {
        // Arrange (Preparar)
        val email = "test@example.com"
        val fakeUsers = listOf(
            mapOf(
                "name" to "Juan Pérez",
                "email" to "test@example.com",
                "password" to "1234",
                "rut" to "11.111.111-1",
                "phone" to "123456789",
                "photoUrl" to "http://example.com/photo.png",
                "biography" to "Estudiante destacado"
            )
        )

        // Configurar el mock: cuando se llame a loadUsers, retornar fakeUsers
        coEvery { DataStoreManager.loadUsers(mockContext) } returns fakeUsers

        // Act (Ejecutar)
        val result = repository.getProfile(email)

        // Assert (Verificar)
        assertNotNull(result, "El resultado no debería ser null")
        assertEquals("Juan Pérez", result.name)
        assertEquals("test@example.com", result.email)
        assertEquals("11.111.111-1", result.rut)
        assertEquals("Estudiante destacado", result.biography)

        // Verificar que se llamó al método loadUsers exactamente 1 vez
        coVerify(exactly = 1) { DataStoreManager.loadUsers(mockContext) }
    }

    @Test
    fun `getProfile returns null when user does not exist`() = runTest {
        // Arrange
        val email = "noexiste@example.com"
        val fakeUsers = listOf(
            mapOf(
                "name" to "Juan Pérez",
                "email" to "otro@example.com",
                "password" to "1234",
                "rut" to "11.111.111-1",
                "phone" to "123456789"
            )
        )

        coEvery { DataStoreManager.loadUsers(mockContext) } returns fakeUsers

        // Act
        val result = repository.getProfile(email)

        // Assert
        assertNull(result, "El resultado debería ser null cuando el usuario no existe")
        coVerify(exactly = 1) { DataStoreManager.loadUsers(mockContext) }
    }

    @Test
    fun `getProfile returns null when users list is empty`() = runTest {
        // Arrange
        val email = "test@example.com"
        coEvery { DataStoreManager.loadUsers(mockContext) } returns emptyList()

        // Act
        val result = repository.getProfile(email)

        // Assert
        assertNull(result, "El resultado debería ser null cuando no hay usuarios")
    }

    @Test
    fun `getProfile handles missing optional fields`() = runTest {
        // Arrange - Usuario sin photoUrl ni biography
        val email = "test@example.com"
        val fakeUsers = listOf(
            mapOf(
                "name" to "María López",
                "email" to "test@example.com",
                "password" to "5678",
                "rut" to "22.222.222-2",
                "phone" to "987654321"
                // Sin photoUrl ni biography
            )
        )

        coEvery { DataStoreManager.loadUsers(mockContext) } returns fakeUsers

        // Act
        val result = repository.getProfile(email)

        // Assert
        assertNotNull(result)
        assertEquals("María López", result.name)
        assertNull(result.photoUrl, "photoUrl debería ser null")
        assertNull(result.biography, "biography debería ser null")
    }

    @Test
    fun `getGrades returns hardcoded grades list`() = runTest {
        // Arrange
        val email = "cualquier@email.com"

        // Act
        val result = repository.getGrades(email)

        // Assert
        assertEquals(4, result.size, "Debería retornar 4 materias")
        assertEquals("Matemáticas", result[0].subject)
        assertEquals(5.8, result[0].score)
        assertEquals("Lenguaje", result[1].subject)
        assertEquals(6.3, result[1].score)
    }
}