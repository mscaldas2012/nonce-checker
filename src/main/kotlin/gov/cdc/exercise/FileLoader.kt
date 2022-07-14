package gov.cdc.exercise

import java.io.File
import java.io.FileNotFoundException

class FileLoader {

    /**
     * Loads all contents of a nonce file into a List of Pairs (nonce, timestamp)
     */
    fun loadFileFromResource(fileName: String): List<Pair<String, String>> {
        val fileContent = FileLoader::class.java.getResource(fileName).readText()

        val lines = fileContent.split("[\r\n]+".toRegex())
        val result: List<Pair<String, String>> = lines
            //Filter empty lines or lines that don't have both a nonce and timestamp
            .filter { it.isNotEmpty() && it.split("\t").size == 2 }
            //Convert the String into a Pair of strings
            .map {
                it.split("\t").let { s -> Pair(s[0], s[1]) }
            }
        return result
    }

    fun loadFile(filePath: String): List<Pair<String, String>> {
        File(filePath).useLines {
            val result: List<Pair<String, String>> = it
                //Filter empty lines or lines that don't have both a nonce and timestamp
                .filter { it.isNotEmpty() && it.split("\t").size == 2 }
                //Convert the String into a Pair of strings
                .map {
                    it.split("\t").let { s -> Pair(s[0], s[1]) }
                }.toList()
            return result
        }
    }
}