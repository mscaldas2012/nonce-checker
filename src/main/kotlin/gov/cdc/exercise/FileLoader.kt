package gov.cdc.exercise

import java.io.File

class FileLoader {
    companion object {
        private val NEW_LINE = "[\r\n]+".toRegex()
        /**
         * Loads all contents of a nonce file into a List of Pairs (nonce, timestamp)
         */
        private fun convertContentToList(content: List<String>): List<Pair<String, String>> {
            val result: List<Pair<String, String>> = content
                //Filter empty lines or lines that don't have both a nonce and timestamp
                .filter { it.isNotEmpty() && it.split("\t").size == 2 }
                //Convert the String into a Pair of strings
                .map {
                    it.split("\t").let { s -> Pair(s[0], s[1]) }
                }
            return result
        }

        fun loadFile(filePath: String): List<Pair<String, String>> {
            val lines = File(filePath).readLines()
            return convertContentToList(lines)
        }
        fun loadFileFromResource(fileName: String): List<Pair<String, String>> {
            FileLoader::class.java.getResource(fileName).apply {
                val lines = this.readText().split(NEW_LINE)
                return convertContentToList(lines)
            }
        }

    }
}