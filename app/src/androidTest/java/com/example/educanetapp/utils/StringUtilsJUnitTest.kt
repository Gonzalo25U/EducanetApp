package com.example.educanetapp.utils

import junit.framework.TestCase
import org.junit.Test
import kotlin.test.assertEquals

class StringUtilsJUnitTest {

    @Test
    fun testValidEmails() {
        TestCase.assertTrue(StringUtils.isValidEmail("test@example.com"))
        TestCase.assertTrue(StringUtils.isValidEmail("user@domain.co"))
    }

    @Test
    fun testInvalidEmails() {
        TestCase.assertFalse(StringUtils.isValidEmail(""))
        TestCase.assertFalse(StringUtils.isValidEmail("notanemail"))
        TestCase.assertFalse(StringUtils.isValidEmail("@example.com"))
    }

    @Test
    fun testCapitalizeWords() {
        val result = StringUtils.capitalizeWords("hola mundo")
        assertEquals("Hola Mundo", result)
    }

    @Test
    fun testTruncate() {
        val result = StringUtils.truncate("texto muy largo aqui", 10)
        assertEquals(10, result.length)
        TestCase.assertTrue(result.endsWith("..."))
    }
}