private sealed interface Packet : Comparable<Packet> {

    fun add(packet: Packet) {
        throw UnsupportedOperationException()
    }

    companion object {
        fun parse(input: String): Packet {
            val stack = ArrayDeque<Packet>()
            var current: Packet? = null
            var continueOnIndex = -1
            input.forEachIndexed { index, c ->
                if (index <= continueOnIndex) {
                    continueOnIndex = -1
                    return@forEachIndexed
                }
                when (c) {
                    '[' -> {
                        val newPacket = ListPacket()
                        stack.addLast(newPacket)
                        current?.add(newPacket)
                        current = newPacket
                    }

                    ']' -> {
                        if (stack.size > 1) {
                            stack.removeLast()
                            current = stack.last()
                        }
                    }

                    ',' -> return@forEachIndexed
                    else -> {
                        val n = input.substring(index)
                            .takeWhile { it.isDigit() } //a packet number can be more than one digit
                        continueOnIndex = index + n.length - 1 //we need to skip the number we just read, don't ignore last char because it could be an ']'
                        current?.add(IntPacket(n.toInt()))
                    }
                }
            }
            return stack.first()//the first packet is the root
        }
    }
}

private data class IntPacket(val value: Int) : Packet {
    fun toListPacket() = ListPacket(mutableListOf(this))

    override fun compareTo(other: Packet) = when (other) {
        is IntPacket -> value compareTo other.value
        is ListPacket -> this.toListPacket() compareTo other
    }
}

private data class ListPacket(private val packets: MutableList<Packet> = mutableListOf()) : Packet {

    override fun add(packet: Packet) {
        packets.add(packet)
    }

    override fun compareTo(other: Packet) = when (other) {
        is IntPacket -> this compareTo other.toListPacket()
        is ListPacket -> packets
            .zip(other.packets, Packet::compareTo)
            .firstOrNull { it != 0 } ?: packets.size.compareTo(other.packets.size)
    }
}

class Day13Solver(private val data: List<String>) {

    private fun processData() = data
        .asSequence()
        .filter { it.isNotBlank() }
        .map {
            Packet.parse(it)
        }

    fun solvePart1(): Int {
        val packets = processData()
        return packets
            .chunked(2)
            .withIndex()
            .fold(0) { acc, (index, packets) ->
                val (leftPacket, rightPacket) = packets
                (acc + index + 1).takeIf { leftPacket < rightPacket } ?: acc
            }
    }

    fun solvePart2(): Int {
        val packets = processData()
        val firstDividerPacket = Packet.parse("[[2]]")
        val secondDividerPacket = Packet.parse("[[6]]")
        return (packets + firstDividerPacket + secondDividerPacket)
            .sorted()
            .withIndex()
            .fold(1) { acc, (index, packet) ->
                when (packet) {
                    firstDividerPacket, secondDividerPacket -> acc * (index + 1)
                    else -> acc
                }
            }
    }
}


fun main() {
//    val data = readInput("Day13_test")
    val data = readInput("Day13")
    val solver = Day13Solver(data)
//    check(solver.solvePart1() == 13)
//    check(solver.solvePart2() == 140)
    println(solver.solvePart1())
    println(solver.solvePart2())
}