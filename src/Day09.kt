import kotlin.math.sign

class Day09Solver(input: List<String>) {

    data class Knot(val isTail: Boolean, var knotX: Int = 0, var knotY: Int = 0) {
        infix fun moveTo(direction: String) {
            when (direction) {
                "U" -> knotY -= 1
                "D" -> knotY += 1
                "L" -> knotX -= 1
                "R" -> knotX += 1
            }
        }

        infix fun isAwayFrom(other: Knot): Boolean {
            return (other.knotX - knotX).pow(2) + (other.knotY - knotY).pow(2) > 2
        }

        infix fun moveTo(other: Knot) {
            val dx = (other.knotX - knotX).sign
            val dy = (other.knotY - knotY).sign
            knotX += dx
            knotY += dy
        }
    }

    private tailrec fun moveKnots(
        rope: List<Knot>,
        direction: String,
        moveHead: Boolean = true,
        visited: MutableSet<Knot>,
    ) {
        val head = rope[0]
        val next = if (head.isTail) return else rope[1]
        if (moveHead) {
            head moveTo direction
        }
        if (next isAwayFrom head) {
            next moveTo head
            if (next.isTail)
                visited.add(next.copy())
            moveKnots(rope.drop(1), direction, false, visited)
        }
    }

    private val movements = input.map { it.substringBefore(" ") to it.substringAfter(" ").toInt() }

    fun solve(totalKnots: Int): Int {
        val visitedKnotsByTail = hashSetOf<Knot>()
        val rope = (1..totalKnots).map { knot -> Knot(knot == totalKnots) }
        movements.forEach { (direction, times) ->
            repeat(times) {
                moveKnots(rope, direction, visited = visitedKnotsByTail)
            }
        }
        return visitedKnotsByTail.size + 1
    }
}

fun main() {
    val testInputPart1 = readInput("Day09_test")
    val testInputPart2 = readInput("Day09_test2")
    check(Day09Solver(testInputPart1).solve(2).also { println(it) } == 13)
    check(Day09Solver(testInputPart2).solve(10).also { println(it) } == 36)
    val input = readInput("Day09")
    val solver = Day09Solver(input)
    println(solver.solve(2))
    println(solver.solve(10))
}

