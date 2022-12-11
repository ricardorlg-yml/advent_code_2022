class Day10Solver(private val input: List<String>) {

    enum class Command(private val _name: String, val cost: Int) {
        NOOP("noop", 1),
        ADD("addx", 2);

        companion object {
            fun fromName(name: String) = values().first { it._name == name }
        }
    }

    data class Instruction(val command: Command, val value: Int)

    private fun parseInput(): List<Instruction> = input.map {
        val line = it.split(" ")
        val command = Command.fromName(line[0])
        val value = line.getOrNull(1)?.toInt() ?: 0
        Instruction(command, value)
    }

    private fun loadRegistry(program: List<Instruction>, registry: MutableMap<Int, Int>) {
        var x = 1
        var cycle = 1
        registry[cycle] = x
        program.forEach { instruction ->
            cycle += instruction.command.cost
            registry[cycle - 1] = x
            x += instruction.value
            registry[cycle] = x
        }
    }

    fun solvePart1(): Long {
        val cycles = (20..220 step 40)
        val instructions = parseInput()
        val registry = mutableMapOf<Int, Int>()
        loadRegistry(instructions, registry)
        return cycles.sumOf {
            registry[it]!! * it.toLong()
        }
    }

    fun solvePart2() {
        val instructions = parseInput()
        val registry = mutableMapOf<Int, Int>()
        loadRegistry(instructions, registry)
        (0 until 240)
            .chunked(40)
            .joinToString("\n") {
                it.joinToString("") { i ->
                    val sp = registry[i + 1]!!
                    val crtPos = i % 40
                    "#".takeIf { crtPos >= sp - 1 && crtPos <= sp + 1 } ?: " "
                }
            }.also {
                println(BLACK_BOLD + it + ANSI_RESET)
            }
    }
}

fun main() {
//    val input = readInput("Day10_test")
    val input = readInput("Day10")
    val solver = Day10Solver(input)
    //check(solver.solvePart1().also { println(it) } == 13140L)
    println(solver.solvePart1())
    solver.solvePart2()
}