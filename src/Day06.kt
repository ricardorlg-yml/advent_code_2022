fun main() {

    fun solve(input: String, validSize: Int): Int {
        var l = 0
        var r = validSize
        while (r < input.length) {
            if (input.substring(l, r).toSet().size == validSize) {
                return r
            }
            l++
            r++
        }
        throw NoSuchElementException("no such index that satisfies the condition was found")
    }

    fun part1(input: String): Int {
        return solve(input, 4)
    }

    fun part2(input: String): Int {
        return solve(input, 14)
    }

    val testInput = readInputString("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)
    val input = readInputString("Day06")
    println(part1(input))
    println(part2(input))
}
