package gov.cdc.exercise

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.FileNotFoundException

internal class FileLoaderTest {
    @Test
    fun testLoadFileFromResource() {
        val elements = FileLoader.loadFileFromResource("/nonce_test.txt")
        assertEquals(elements.size , 14)
    }

    @Test
    fun testLoadFile() {
        val elements = FileLoader.loadFile("./src/test/resources/nonce_test.txt")
        assertEquals(elements.size , 14)
    }

    @Test
    fun testLoadInvalidFile() {
        runCatching {
            FileLoader.loadFile("not_a_valid_file.txt")
        }.onSuccess {
            assertTrue(false, "Exception must be thrown")
        }.onFailure {
            when (it) {
                is FileNotFoundException -> assertTrue(true, "Exception properly thrown")
                else -> assertTrue(false, "Not the appropraite exception was thrown -> $it")
            }

        }
    }

    @Test
    fun testLoadInvalidResourceFile() {
        runCatching {
            FileLoader.loadFileFromResource("/not_a_valid_file.txt")
        }.onSuccess {
            assertTrue(false, "Exception must be thrown")
        }.onFailure {
            when (it) {
                is NullPointerException -> assertTrue(true, "Exception properly thrown")
                else -> assertTrue(false, "Not the appropraite exception was thrown -> $it")
            }

        }
    }


}