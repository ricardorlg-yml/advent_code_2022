fun main() {

    val priorities = ('a'..'z')
        .plus('A'..'Z')
        .mapIndexed { index, c -> c to index + 1 }
        .toMap()

    fun intersect(strings: List<String>): Set<Char> {
        return strings
            .map(String::toSet)
            .reduce { acc, set -> acc.intersect(set) }
    }

    fun part1(input: List<String>): Int {
        return input
            .map { it.chunked(it.length / 2) }
            .sumOf { priorities[intersect(it).single()]!! }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .sumOf { priorities[intersect(it).single()]!! }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)
    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}