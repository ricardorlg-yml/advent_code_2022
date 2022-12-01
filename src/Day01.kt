fun main() {
    fun parseInput(input: List<String>): List<Long> {
        val tot = mutableListOf<Long>()
        var accum = 0L
        input.forEach { v ->
            if (v.isNotBlank()) {
                accum += v.toLong()
            } else {
                tot.add(accum)
                accum = 0L
            }
        }
        tot.add(accum)//add last one
        return tot.sorted()
    }

    fun part1(input: List<String>): Long {
        return parseInput(input).last()
    }

    fun part2(input: List<String>): Long {
        return parseInput(input)
            .takeLast(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000L)
    check(part2(testInput) == 45000L)
    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
