package gov.cdc.exercise

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class NonceChecker {
    val MAX_DURATION: Int = 5 * 60
    /**
     * Main method to process a file and try to find duplicate nonce.
     *
     * Returns a list of warnings of nonce re-used wihtin 5 minutes.
     */
    fun processFile(filePath: String, nonceTTL: Int = MAX_DURATION): List<String> {
        val loader = FileLoader()
        val elements = loader.loadFile(filePath)
        val dupes =  findDuplicates(elements)
        return getWarnings(dupes, nonceTTL)
    }
    fun processResourceFile(fileName: String, nonceTTL: Int = MAX_DURATION): List<String> {
        val loader = FileLoader()
        val elements = loader.loadFileFromResource(fileName)
        val dupes =  findDuplicates(elements)
        return getWarnings(dupes, nonceTTL)
    }
    /**
     * Helper method to find duplicate nonces...
     * Requirements specify that each duplicate must be flagged as such and identify the most recent.
     * The file is a log file, where timestamps are ordered - so no need to use sort.
     * Simply using a reverse so that we start from the most recent (never a duplicate)
     *
     * Result is a Map, where the key is the nonce and the value is the list of all Timestamps associated with it.
    */
    private fun findDuplicates(nonceList: List<Pair<String, String>>): Map<String, List<LocalDateTime>> {
        val duplicates = mutableMapOf<String, MutableList<LocalDateTime>>()
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")

        //According to Requirements, the file is order by timestamp - we can use this to our advantage to identify the most recent one.
        nonceList.reversed().forEach {
            if (duplicates.containsKey(it.first)) {
                val existingList = duplicates[it.first]!!
                existingList.add( LocalDateTime.parse(it.second, pattern))
            } else {
                duplicates[it.first] = mutableListOf( LocalDateTime.parse(it.second, pattern))
            }
        }
        return duplicates.filter { it.value.size >1 }
    }
    /**
     * Converts the Duplicates into warnings , as long as they are within 5 min of each other!
     *
     * Retruns a List of Warnings for nonces that were reused in less than 5 minutes.
     */
    private fun getWarnings(dupes: Map<String, List<LocalDateTime>>, nonceTTL: Int): List<String> {
        val warnings = mutableListOf<String>()
        dupes.forEach {
            for (idx in 0 until  it.value.size - 1) {
                if (abs(it.value[idx+1].toEpochSecond(ZoneOffset.UTC) - it.value[idx].toEpochSecond(ZoneOffset.UTC)) <= nonceTTL) {
                    warnings.add("Duplicate nonce: ${it.key}: current time ${it.value[idx+1]} last used ${it.value[idx]}")
                }
            }
        }
        return warnings
    }

}

//Siople method to process the existing nonce.txt file in resources folder.
fun main(args: Array<String>) {
    val checker =  NonceChecker()
    if (args.size < 1 || args.size > 2) {
        showUsage()
    }

    val warnings =if (args.size == 2) checker.processFile(args[0], args[1].toInt()*60) else checker.processFile((args[0]))
    warnings.forEach{ println(it)}
}

fun showUsage() {
    println("""
        You must provide a file path as a single parameter to run this code.
        
        Ex.:
            java -jar nonce_checker.jar <FilePath> <nonceTTL>
            
        Where:
            - FIlePath is the path to the file to be processed
            - nonceTTL (Optional:Default 5 min): is the time to live for a nonce. A duplicate within this time period
                  is considered a duplicate. (pass values in minutes)
    """)
    System.exit(1)
}