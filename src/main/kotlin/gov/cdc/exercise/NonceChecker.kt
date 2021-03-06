package gov.cdc.exercise

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.system.exitProcess

class NonceChecker {
    companion object {
        val DEFAULT_MAX_DURATION: Int = 5 * 60
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    }
    /**
     * Main method to process a file and try to find duplicate nonce.
     *
     * Returns a list of warnings of nonce re-used within nonceTTL minutes.
     */
    fun processFile(filePath: String, nonceTTL: Int = DEFAULT_MAX_DURATION): List<String> {
        val elements = FileLoader.loadFile(filePath)
        val dupes =  findDuplicates(elements)
        return generateWarnings(dupes, nonceTTL)
    }
    /**
     * Helper method to find duplicate nonces...
     * Requirements specify that each duplicate must be flagged as such and identify the most recent.
     * The file is a log file, where timestamps are ordered - so no need to  sort.
     * Simply using a reverse so that we start from the most recent
     *
     * Result is a Map, where the key is the nonce and the value is the list of all timestamps associated with it.
    */
    private fun findDuplicates(nonceList: List<Pair<String, String>>): Map<String, List<LocalDateTime>> {
        val duplicates = mutableMapOf<String, MutableList<LocalDateTime>>()

        //According to Requirements, the file is order by timestamp - we can use this to our advantage to identify
        // the most recent one.
        nonceList.reversed().forEach {
            if (duplicates.containsKey(it.first)) {
                val existingList = duplicates[it.first]!!
                existingList.add( it.second.to_date(pattern))
            } else {
                duplicates[it.first] = mutableListOf( it.second.to_date(pattern))
            }
        }
        return duplicates.filter { it.value.size >1 }
    }
    /**
     * Converts the Duplicates into warnings , as long as they are within "nonceTTL" min. of each other!
     */
    private fun generateWarnings(dupes: Map<String, List<LocalDateTime>>, nonceTTL: Int): List<String> {
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
//Extension function to transform a String into LocalDateTime based on given pattern.
fun String.to_date(pattern: DateTimeFormatter): LocalDateTime {
    return LocalDateTime.parse(this, pattern)
}

//Simple method to process files passed as arguments via command line
fun main(args: Array<String>) {
    val checker =  NonceChecker()
    if (args.size < 1 || args.size > 2)
        showUsage()
    try {
        val startTime = System.currentTimeMillis()
        val warnings =
            if (args.size == 2) checker.processFile(args[0], args[1].toInt() * 60)
            else checker.processFile((args[0]))
        val endTime = System.currentTimeMillis()
        warnings.forEach { println(it) }
        println("Duplication identification process took ${endTime - startTime} milliseconds")
    } catch (e: NumberFormatException) {
        showUsage()

    }
}

fun showUsage() {
    println("""
        You must provide a file path as a single parameter to run this code.
        
        Ex.:
            java -jar nonce_checker.jar <FilePath> <nonceTTL>
            
        Where:
            - FIlePath is the path to the file to be processed
            - nonceTTL (Optional:Default 5 min): is the time to live for a nonce in minutes. A duplicate within 
                   this time period is considered an error. 
    """)
    exitProcess(1)
}
