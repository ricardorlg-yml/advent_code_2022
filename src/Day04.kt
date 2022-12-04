fun main() {
    data class Section(val x: Int, val y: Int) {
        infix fun fullyContains(other: Section): Boolean {
            return x <= other.x && y >= other.y
        }

        infix fun overlaps(other: Section): Boolean {
            return !(other.x > y || other.y < x)
        }
    }

    fun String.toSection(): Section {
        val (x, y) = split("-").map { it.toInt() }
        return Section(x, y)
    }

    fun part1(input: List<String>): Int {
        return input.count { l ->
            val (a, b) = l.split(",").map { it.toSection() }
            a fullyContains b || b fullyContains a
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { l ->
            val (a, b) = l.split(",").map { it.toSection() }
            a overlaps b || b overlaps a
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)
    val input = readInput("Day04")
    println(part1(input))
    print(part2(input))
}