import Day08Solver.Direction.*

class Day08Solver(private val input: List<List<Int>>, private val size: Int = input.size) {
    private enum class Direction { LEFT, RIGHT, UP, DOWN }

    private fun generateNeighbours(direction: Direction, row: Int, col: Int): Sequence<Int> {
        val range = when (direction) {
            LEFT -> (col - 1 downTo 0)
            RIGHT -> (col + 1 until size)
            UP -> (row - 1 downTo 0)
            DOWN -> (row + 1 until size)
        }
        return sequence {
            for (i in range) {
                yield(
                    when (direction) {
                        LEFT, RIGHT -> input[row][i]
                        UP, DOWN -> input[i][col]
                    }
                )
            }
        }
    }

    fun part1(): Int {
        var visibleTrees = size * size - ((size - 2) * (size - 2))
        for (row in 1 until size - 1) {
            for (col in 1 until size - 1) {
                val treeHeight = input[row][col]
                val treesOnTheLeft = generateNeighbours(LEFT, row, col)
                val treesOnTheRight = generateNeighbours(RIGHT, row, col)
                val treesFromTop = generateNeighbours(UP, row, col)
                val tressFromBottom = generateNeighbours(DOWN, row, col)
                if (treesOnTheLeft.all { treeHeight > it } || treesOnTheRight.all { treeHeight > it } || treesFromTop.all { treeHeight > it } || tressFromBottom.all { treeHeight > it }) {
                    visibleTrees++
                    continue
                }
            }
        }
        return visibleTrees
    }

    fun part2(): Int {
        var result = Int.MIN_VALUE
        for (row in 1 until size - 1) {
            for (col in 1 until size - 1) {
                val treeHeight = input[row][col]
                val treesOnTheLeft = generateNeighbours(LEFT, row, col)
                val treesOnTheRight = generateNeighbours(RIGHT, row, col)
                val treesFromTop = generateNeighbours(UP, row, col)
                val tressFromBottom = generateNeighbours(DOWN, row, col)
                val visibleLeftTrees = countVisibleTrees(treesOnTheLeft, treeHeight)
                val visibleRightTress = countVisibleTrees(treesOnTheRight, treeHeight)
                val visibleTopTrees = countVisibleTrees(treesFromTop, treeHeight)
                val visibleBottomTrees = countVisibleTrees(tressFromBottom, treeHeight)
                val scenicScore = visibleLeftTrees * visibleRightTress * visibleTopTrees * visibleBottomTrees
                if (scenicScore > result) {
                    result = scenicScore
                }
            }
        }
        return result
    }

    private fun countVisibleTrees(trees: Sequence<Int>, current: Int): Int {
        var visibleTrees = 0
        for (tree in trees) {
            if (tree >= current) {
                visibleTrees++
                break
            } else {
                visibleTrees++
            }
        }
        return visibleTrees
    }
}


fun main() {

    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line -> line.map { it.digitToInt() }}
    }

    val testInput = parseInput(readInput("Day08_test"))
    val testSolver = Day08Solver(testInput)
    check(testSolver.part1() == 21)
    check(testSolver.part2() == 8)
    val input = readInput("Day08").run { parseInput(this) }
    val solver = Day08Solver(input)
    println("Part 1: ${solver.part1()}")
    println("Part 2: ${solver.part2()}")
}