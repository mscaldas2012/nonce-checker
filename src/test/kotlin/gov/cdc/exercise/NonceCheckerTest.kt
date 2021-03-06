package gov.cdc.exercise

import org.junit.jupiter.api.Test


internal class NonceCheckerTest {
    @Test
    fun testProcessFile() {
        val checker = NonceChecker()
        val warnings = checker.processFile("./src/test/resources/nonce_test.txt")
        warnings.forEach  {println(it)}
        assert (warnings.size == 5)
    }
}