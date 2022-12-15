import kotlin.math.abs


val numberRegex = """(-?\d+)""".toRegex()

class Day15Solver(private val data: List<String>) {

    private var minX = 0L
    private var maxX = 0L

    private val sensors = parseInput()


    class Sensor(
        val sensorX: Long,
        val sensorY: Long,
        private val closestBeaconX: Long,
        private val closetBeaconY: Long,
    ) {
        val distance = abs(closestBeaconX - sensorX) + abs(closetBeaconY - sensorY)
        fun distanceTo(x: Long, y: Long) = abs(sensorX - x) + abs(sensorY - y)
        fun isClosestBeacon(x: Long, y: Long) = closestBeaconX == x && closetBeaconY == y

        override fun toString(): String {
            return "Sensor location: $sensorX, $sensorY Distance: $distance"
        }
    }

    private fun isNotAvailableSpot(sensor: Sensor, x: Long, y: Long): Boolean {
        return !sensor.isClosestBeacon(x, y) && sensor.distance >= sensor.distanceTo(x, y)
    }

    private fun validSpot(x: Long, y: Long): Boolean {
        return sensors.none { it.distanceTo(x, y) <= it.distance }
    }


    private fun parseInput(): List<Sensor> {
        var maxDistance = Long.MIN_VALUE
        var minBeaconX = Long.MAX_VALUE
        var maxBeaconX = Long.MIN_VALUE
        return data.map { line ->
            val (sx, sy, bx, by) = numberRegex.findAll(line).map { it.groupValues[1].toLong() }.toList()
            val sensor = Sensor(sx, sy, bx, by)
            minBeaconX = minOf(minBeaconX, bx)
            maxBeaconX = maxOf(maxBeaconX, bx)
            maxDistance = maxOf(maxDistance, sensor.distance)
            minX = minBeaconX - maxDistance
            maxX = maxBeaconX + maxDistance
            sensor
        }
    }


    fun solvePart1(row: Long): Long {
        return (minX..maxX).fold(0L) { unavailableSpots, col ->
            if (sensors.any { isNotAvailableSpot(it, col, row) })
                unavailableSpots + 1
            else
                unavailableSpots
        }
    }

    /**
     * given that only 1 beacon is available then it should be at distance + 1 from any sensor
     * so just iterate over all points that are at distance + 1 from any sensor and check if the position is available
     */
    fun solvePart2(max: Long): Long {
        val multiplier = 4_000_000L
        for (sensor in sensors) {
            for (xDistance in 0..sensor.distance) {
                val yDistance = sensor.distance + 1 - xDistance
                //check all possible combinations of x and y distance which means sensor x plus or minus new distance and sensor y plus or minus new distance
                signs.map { (signX, signY) ->
                    sensor.sensorX + xDistance * signX to sensor.sensorY + yDistance * signY
                }.filter { (beaconX, beaconY) ->
                    beaconX in 0..max && beaconY in 0..max
                }.firstOrNull { (beaconX, beaconY) ->
                    validSpot(beaconX, beaconY)
                }?.let { (beaconX, beaconY) ->
                    return beaconX * multiplier + beaconY
                }
            }
        }
        throw IllegalStateException("No valid spot found")
    }

}

fun main() {
//    val data = readInput("Day15_test")
    val data = readInput("Day15")
    val solver = Day15Solver(data)
//    println(solver.solvePart1(10))
//    println(solver.solvePart2(20))
    println(solver.solvePart1(2_000_000))
    println(solver.solvePart2(4_000_000))
}