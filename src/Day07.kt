class Directory(
    val name: String,
    val parent: Directory? = null,
    private val isDirectory: Boolean = true,
    private val fileSize: Int = 0,
    val subDirectories: MutableMap<String, Directory> = mutableMapOf(),
) {
    val size: Int by lazy {
        fileSize + subDirectories.values.sumOf { it.size }
    }

    fun findDirectories(predicate: (Directory) -> Boolean): List<Int> {
        val stack = ArrayDeque<Directory>().apply { add(this@Directory) }
        val response = mutableListOf<Int>()
        while (stack.isNotEmpty()) {
            val node = stack.removeFirst()
            stack.addAll(node.subDirectories.values)
            if (node.isDirectory && predicate(node)) response.add(node.size)
        }
        return response
    }

    override fun toString(): String {
        return "${if (isDirectory) "Directory" else "File"} $name, size: ${if (isDirectory) size else fileSize}"
    }
}

fun main() {
    val totalSize = 70_000_000
    val minSpaceSize = 30_000_000
    fun parseProgram(lines: List<String>): Directory {
        val root = Directory("/")
        //assume that we start from root
        var currentNode: Directory = root
        for (line in lines) {
            when {
                line == "$ ls" -> continue
                line == "$ cd /" -> currentNode = root
                line == "$ cd .." -> currentNode = currentNode.parent!!
                line.startsWith("$ cd") -> {
                    val name = line.substringAfterLast(" ")
                    currentNode = currentNode.subDirectories[name]!!
                }

                line.startsWith("dir") -> {
                    val name = line.substringAfterLast(" ")
                    currentNode.subDirectories[name] = Directory(name = name, parent = currentNode)
                }

                else -> {
                    val name = line.substringAfterLast(" ")
                    val size = line.substringBeforeLast(" ").toInt()
                    currentNode.subDirectories[name] =
                        Directory(name = name, parent = currentNode, isDirectory = false, fileSize = size)
                }
            }
        }
        return root
    }


    fun part1(input: List<String>): Int {
        val rootDirectory = parseProgram(input)
        return rootDirectory.findDirectories { directory -> directory.size <= 100_000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val rootDirectory = parseProgram(input)
        val totalUsedSize = rootDirectory.size
        val currentUnusedSpaceSize = totalSize - totalUsedSize
        val neededSpace = minSpaceSize - currentUnusedSpaceSize
        return rootDirectory.findDirectories { directory -> directory.size >= neededSpace }.min()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)
    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))


}