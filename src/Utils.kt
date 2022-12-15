import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

const val ANSI_RESET = "\u001B[0m"
const val BLACK_BOLD = "\u001B[1;30m"

val signs = listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun readInputString(name: String) = File("src", "$name.txt")
    .readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun String.removeSpaces() = replace(" ", "")

fun Int.pow(b: Int): Int {
    var _a = this
    var _b = b
    var r = 1
    while (_b > 0) {
        if (_b and 1 == 1) {
            r *= _a
        }
        _b = _b shr 1
        _a *= _a
    }
    return r
}
