data class Monkey(
    val id: Int,
    private val itemOperation: (Long) -> Long,
    private val reliefOperation: (Long) -> Long,
    val divisibleBy: Int,
    private val nextMonkeyIdOnTrue: Int,
    private val nextMonkeyIdOnFalse: Int,
    private val items: MutableList<Long>,
) {

    private fun addItem(item: Long) {
        items.add(item)
    }

    fun processItems(monkeys: List<Monkey>, includeRelief: Boolean, upperLimit: Int): Int {
        var counter = 0
        while (items.isNotEmpty()) {
            counter += 1
            var worryLevel = items.removeFirst()
            worryLevel = itemOperation(worryLevel)
            worryLevel = (if (includeRelief) reliefOperation(worryLevel) else worryLevel) % upperLimit
            if (worryLevel % divisibleBy == 0L) {
                monkeys[nextMonkeyIdOnTrue].addItem(worryLevel)
            } else {
                monkeys[nextMonkeyIdOnFalse].addItem(worryLevel)
            }
        }
        return counter
    }

    override fun toString(): String {
        return "Monkey $id: ${items.joinToString(", ")}"
    }
}

class Day11Solver(private val data: List<List<String>>) {

    fun solvePart1(printSteps: Boolean): Long {
        return solve(rounds = 20, includeBoring = true, printSteps)
    }

    fun solvePart2(printSteps: Boolean): Long {
        return solve(rounds = 10_000, includeBoring = false, printSteps)
    }

    private fun solve(rounds: Int, includeBoring: Boolean, printSteps: Boolean = false): Long {
        val monkeys = processInput()
        val roundsToPrint = listOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000)
        val maxDiv = monkeys.fold(1) { acc, monkey -> acc * monkey.divisibleBy }
        val inspectedItemsByMonkey = monkeys.associate { it.id to 0 }.toMutableMap()
        (1..rounds).forEach { round ->
            if (printSteps)
                println("Round $round")
            monkeys.forEach { monkey ->
                val inspected = monkey.processItems(monkeys, includeBoring, maxDiv)
                inspectedItemsByMonkey[monkey.id] = inspectedItemsByMonkey[monkey.id]!! + inspected
            }
            if (printSteps) {
                monkeys.forEach { monkey ->
                    println(monkey)
                }
            }
            if (printSteps) {
                if (round in roundsToPrint) {
                    println("Inspected items by monkey: after round $round")
                    inspectedItemsByMonkey.forEach { (monkeyId, inspectedItems) ->
                        println("Monkey $monkeyId: inspected items $inspectedItems times")
                    }
                }
            }
        }

        return inspectedItemsByMonkey
            .values
            .sorted()
            .takeLast(2)
            .map { it.toLong() }
            .reduce(Long::times)
    }

    private fun processInput(): List<Monkey> {
        return data.mapIndexed { index, monkeyData ->
            val items = monkeyData[1].substringAfter(":").split(",").map { it.trim().toLong() }
            Monkey(
                id = index,
                itemOperation = getOperation(monkeyData[2].substringAfterLast("=").trim()),
                reliefOperation = { it / 3 },
                divisibleBy = monkeyData[3].substringAfter("by").trim().toInt(),
                nextMonkeyIdOnTrue = monkeyData[4].substringAfterLast(" ").trim().toInt(),
                nextMonkeyIdOnFalse = monkeyData[5].substringAfterLast(" ").trim().toInt(),
                items = items.toMutableList(),
            )
        }
    }

    private fun getOperation(operation: String): (Long) -> Long {
        if (operation.contains("+")) {
            val (a, b) = operation.split("+").map { it.trim() }
            return {
                (a.toLongOrNull() ?: it) + (b.toLongOrNull() ?: it)
            }
        }
        if (operation.contains("*")) {
            val (a, b) = operation.split("*").map { it.trim() }
            return {
                (a.toLongOrNull() ?: it) * (b.toLongOrNull() ?: it)
            }
        }
        if (operation.contains("/")) {
            val (a, b) = operation.split("/").map { it.trim() }
            return {
                (a.toLongOrNull() ?: it) / (b.toLongOrNull() ?: it)
            }
        }
        if (operation.contains("-")) {
            val (a, b) = operation.split("-").map { it.trim() }
            return {
                (a.toLongOrNull() ?: it) - (b.toLongOrNull() ?: it)
            }
        }
        throw IllegalStateException("Unable to parse operation: $operation")
    }
}

fun main() {
//    val data = readInputString("Day11_test").split("\n\n").map { it.split("\n") }
//    check(Day11Solver(data).solvePart1(false) == 10605L)
//    check(Day11Solver(data).solvePart2(false) == 2713310158L)
    val data = readInputString("Day11").split("\n\n").map { it.split("\n") }
    println(Day11Solver(data).solvePart1(false))
    println(Day11Solver(data).solvePart2(false))
}