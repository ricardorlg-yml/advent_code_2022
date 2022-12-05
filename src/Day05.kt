data class CrateStack(var data: String) {
    fun remove(howMany: Int): String {
        return data.takeLast(howMany)
            .also {
                data = data.dropLast(howMany)
            }
    }

    fun add(data: String, crateMover9001: Boolean = false) {
        if (crateMover9001) {
            this.data += data
        } else
            this.data += data.reversed()
    }
}

data class CrateMove(val howMany: Int, val from: Int, val to: Int)

fun main() {

    val moveRegex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
    fun parseInput(input: List<String>): Pair<List<CrateStack>, List<CrateMove>> {
        val (inputMoves, inputCrateStacksData) = input.partition { it.matches(moveRegex) }
        val lastValidInput = inputCrateStacksData.withIndex().last { it.value.isNotBlank() }
        val totalStackCrates = lastValidInput.value.split(" ").filter { it.isNotEmpty() }.size
        val cratesStackInitialStateData = inputCrateStacksData.subList(0, lastValidInput.index + 1)
        val crates = mutableListOf<CrateStack>()
        for (i in 0 until totalStackCrates) {
            cratesStackInitialStateData.fold("") { acc, s ->
                s.getOrNull(i * 4 + 1)
                    .let {
                        if (it == null || !it.isLetter()) acc
                        else it + acc
                    }
            }.apply {
                if (isNotEmpty()) crates.add(CrateStack(this))
            }
        }

        val moves = inputMoves.mapNotNull {
            moveRegex
                .find(it)?.destructured?.toList()
                ?.map { s -> s.toInt() }
                ?.let { (a, b, c) -> CrateMove(a, b, c) }
        }
        return Pair(crates, moves)
    }

    fun part1(input: List<String>): String {
        val (crates, moves) = parseInput(input)
        moves.forEach { move ->
            val dataRemoved = crates[move.from - 1].remove(move.howMany)
            crates[move.to - 1].add(dataRemoved)
        }
        return crates.joinToString("") { it.data.takeLast(1) }
    }

    fun part2(input: List<String>): String {
        val (crates, moves) = parseInput(input)
        moves.forEach { move ->
            val dataRemoved = crates[move.from - 1].remove(move.howMany)
            crates[move.to - 1].add(dataRemoved, true)
        }
        return crates.joinToString("") { it.data.takeLast(1) }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")
    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}