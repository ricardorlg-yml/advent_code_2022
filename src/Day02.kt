fun main() {

    val pointsMapByShape = mapOf(
        "AX" to 4,
        "AY" to 8,
        "AZ" to 3,
        "BX" to 1,
        "BY" to 5,
        "BZ" to 9,
        "CX" to 7,
        "CY" to 2,
        "CZ" to 6
    )

    val pointsMapByExpectedResult = mapOf(
        "AX" to 3,
        "AY" to 4,
        "AZ" to 8,
        "BX" to 1,
        "BY" to 5,
        "BZ" to 9,
        "CX" to 2,
        "CY" to 6,
        "CZ" to 7
    )

    fun part1(input: List<String>): Int {
        return input.sumOf { pointsMapByShape[it.removeSpaces()]!! }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { pointsMapByExpectedResult[it.removeSpaces()]!! }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)
    
    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}