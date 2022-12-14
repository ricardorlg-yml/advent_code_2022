import java.lang.Integer.max
import java.lang.Integer.min

class Day14Solver(private val input: List<String>) {

    private var maxCol: Int = -1
    private var maxRow: Int = -1

    data class Path(val point1: Point, val point2: Point) {
        fun range(): List<Point> {
            return if (point1.row == point2.row) {
                (min(point1.col, point2.col)..max(point1.col, point2.col)).map { col ->
                    Point(point1.row, col)
                }
            } else if (point1.col == point2.col) {
                (min(point1.row, point2.row)..max(point1.row, point2.row)).map { row ->
                    Point(row, point1.col)
                }
            } else {
                throw IllegalArgumentException("Invalid path")
            }
        }
    }

    data class Point(val row: Int, val col: Int) {
        fun neighbors(part2: Boolean = false, maxRow: Int = -1): List<Point> {
            return if (part2 && row == maxRow) {
                listOf(Point(row, col), Point(row, col - 1), Point(row, col + 1))
            } else {
                listOf(Point(row + 1, col), Point(row + 1, col - 1), Point(row + 1, col + 1))
            }
        }
    }

    private fun parseInput(infinityFloor: Boolean = false, rowMapper: (Int) -> Int): Array<CharArray> {
        val points = input.map { line ->
            line.split("->")
                .map { p ->
                    val (x, y) = p.trim().split(",")
                    Point(y.toInt(), x.toInt())
                }
        }
        val mc = points.flatten().minBy { it.col }.col
        maxCol = points.flatten().maxBy { it.col }.col + mc
        maxRow = rowMapper(points.flatten().maxBy { it.row }.row)

        val grid =
            Array(maxRow) { i ->
                CharArray(maxCol + 1) {
                    if (infinityFloor) {
                        '.'.takeIf { i < maxRow - 1 } ?: '#'
                    } else
                        '.'
                }
            }
        points
            .flatMap {
                it.zipWithNext().map { (p1, p2) -> Path(p1, p2) }
            }.forEach { path ->
                path.range().forEach { p ->
                    grid[p.row][p.col] = '#'
                }
            }
        return grid
    }

    private tailrec fun nextPoint(grid: Array<CharArray>, point: Point, part2: Boolean): Point {
        val neighbors = point.neighbors(part2, grid.lastIndex)
        if (neighbors.all { kotlin.runCatching { grid[it.row][it.col] }.getOrDefault('#') != '.' }) {
            return point
        }
        val nextPoint = neighbors.first { grid[it.row][it.col] == '.' }
        return nextPoint(grid, nextPoint, part2)
    }

    fun solvePart1(): Int {
        val grid = parseInput { it + 1 }
        var counter = 0
        val start = Point(0, 500)
        while (true) {
            val next = nextPoint(grid, start, false)
            if (next.row >= grid.lastIndex) {
                break
            }
            grid[next.row][next.col] = 'o'
            counter++
        }
        return counter
    }

    fun solvePart2(): Int {
        val grid = parseInput(true) { it + 3 }
        var counter = 0
        val start = Point(0, 500)
        while (true) {
            val next = nextPoint(grid, start, true)
            grid[next.row][next.col] = 'o'
            counter++
            if (next == start)
                break
        }
        return counter
    }
}

fun main() {
//    val data = readInput("Day14_test")
    val data = readInput("Day14")
    val solver = Day14Solver(data)
//    check(solver.solvePart1() == 24)
//    check(solver.solvePart2() == 93)
    println(solver.solvePart1())
    println(solver.solvePart2())
}