import java.util.*

class Graph<T> {
    private val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()
    fun addEdge(node1: T, node2: T) {
        adjacencyMap.computeIfAbsent(node1) { HashSet() }.add(node2)
    }

    fun shortestPath(start: T, endCondition: (T) -> Boolean): Int {
        val visited = HashSet<T>()
        val queue = PriorityQueue<Pair<T, Int>>(compareBy { it.second })// queue base on distance
        queue.add(Pair(start, 0))
        while (queue.isNotEmpty()) {
            val (node, distance) = queue.poll()
            if (node !in visited) {
                visited.add(node)
                adjacencyMap[node]?.forEach {
                    if (endCondition(it)) {
                        return distance + 1
                    }
                    queue.add(Pair(it, distance + 1))
                }
            }
        }
        throw IllegalStateException("No path found")
    }
}

class Day12Solver(private val gridMap: List<String>) {
    data class MyPoint(val row: Int, val col: Int, val elevation: Int, val isSource: Boolean, val isTarget: Boolean) {

        fun neighbors(grid: List<List<MyPoint>>): List<MyPoint> {
            return listOfNotNull(
                grid.getOrNull(row - 1)?.getOrNull(col),
                grid.getOrNull(row + 1)?.getOrNull(col),
                grid.getOrNull(row)?.getOrNull(col - 1),
                grid.getOrNull(row)?.getOrNull(col + 1)
            )
        }

        fun canMoveTo(other: MyPoint): Boolean {
            return other.elevation - this.elevation <= 1
        }
    }

    private val grid = processInput()
    private lateinit var startPoint: MyPoint
    private lateinit var endPoint: MyPoint


    private fun processInput(): List<List<MyPoint>> {
        return gridMap.mapIndexed { r, line ->
            line.mapIndexed { c, elevation ->
                when (elevation) {
                    'S' -> {
                        MyPoint(row = r, col = c, elevation = 'a' - 'a', isSource = true, isTarget = false)
                            .also {
                                startPoint = it
                            }
                    }

                    'E' -> {
                        MyPoint(row = r, col = c, elevation = 'z' - 'a', isSource = false, isTarget = true)
                            .also {
                                endPoint = it
                            }
                    }

                    else -> {
                        MyPoint(row = r, col = c, elevation = elevation - 'a', isSource = false, isTarget = false)
                    }
                }
            }
        }
    }

    private fun generateGraph(isPart1: Boolean): Graph<MyPoint> {
        val graph = Graph<MyPoint>()
        grid.forEach { p ->
            p.forEach { point ->
                val neighbors = point.neighbors(grid)
                neighbors.forEach { neighbor ->
                    if (point.canMoveTo(neighbor)) {
                        if (isPart1)
                            graph.addEdge(point, neighbor)
                        else {
                            graph.addEdge(neighbor, point)
                        }
                    }
                }
            }
        }
        return graph
    }

    fun solve(part1: Boolean): Int {
        if (part1) {
            return generateGraph(true).shortestPath(startPoint) { it.isTarget }
        }
        return generateGraph(false).shortestPath(endPoint) { it.elevation == 0 }
    }
}

fun main() {
//    val data = readInput("Day12_test")
    val data = readInput("Day12")
    val solver = Day12Solver(data)
//    check(solver.solve(true) == 31)
//    check(solver.solve(false) == 29)
    println(solver.solve(true))
    println(solver.solve(false))
}