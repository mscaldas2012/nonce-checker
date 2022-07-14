package gov.cdc.exercise

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.FileNotFoundException

internal class FileLoaderTest {
    @Test
    fun testLoadFileFromResource() {
        val loader = FileLoader()
        val elements = loader.loadFileFromResource("/nonce_test.txt")
        assertEquals(elements.size , 14)
    }

    @Test
    fun testLoadFile() {
        val loader = FileLoader()
        val elements = loader.loadFile("./src/test/resources/nonce_test.txt")
        assertEquals(elements.size , 14)
    }

    @Test
    fun testLoadInvalidFile() {
        runCatching {
            val loader = FileLoader()
            loader.loadFile("not_a_valid_file.txt")
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
            val loader = FileLoader()
            loader.loadFileFromResource("/not_a_valid_file.txt")
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