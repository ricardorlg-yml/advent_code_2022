import kotlin.math.sign

class Day09Solver(input: List<String>) {

    data class Knot(val isTail: Boolean, var knotX: Int = 0, var knotY: Int = 0) {
        fun moveKnot(direction: String) {
            when (direction) {
                "U" -> knotY -= 1
                "D" -> knotY += 1
                "L" -> knotX -= 1
                "R" -> knotX += 1
            }
        }

        fun touchKnot(other: Knot): Boolean {
            return (other.knotX - knotX).pow(2) + (other.knotY - knotY).pow(2) <= 2
        }

        fun moveTo(other: Knot) {
            val dx = (other.knotX - knotX).sign
            val dy = (other.knotY - knotY).sign
            knotX += dx
            knotY += dy
        }
    }

    private tailrec fun move(
        knots: List<Knot>,
        direction: String,
        moveHead: Boolean = true,
        visited: MutableSet<Knot>,
    ) {
        val head = knots[0]
        val next = if(head.isTail) return else knots[1]
        if (moveHead) {
            head.moveKnot(direction)
        }
        if (!head.touchKnot(next)) {
            next.moveTo(head)
            if (next.isTail)
                visited.add(next.copy())
            move(knots.drop(1), direction, false, visited)
        }
    }

    private val motions = input.map {
        it.split(" ")
            .run {
                get(0) to get(1).toInt()
            }
    }

    fun solve(totalKnots: Int): Int {
        val response = hashSetOf<Knot>()
        val knots = (1..totalKnots).map { knot -> Knot(knot == totalKnots) }
        motions.forEach { (dir, amount) ->
            repeat(amount) {
                move(knots, dir, visited = response)
            }
        }
        return response.size + 1
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

