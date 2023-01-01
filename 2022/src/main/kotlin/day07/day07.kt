package day07

import common.readLines
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun main() {
    val commands = readLines("day07/input.txt")

    val (fs, setupTime) = measureTimedValue { buildFilesystem(commands) }
    println("Setup: ${setupTime.inWholeMilliseconds} ms")

    val elapsed1 = measureTimeMillis { part1(fs) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(fs) }
    println("Part2: $elapsed2 ms")

}

fun part1(fs: Filesystem) {
    val sum = fs
        .filter { it.isDir && it.totalSize < 100000 }
        .sumOf { it.totalSize }

    println(sum)
}

fun part2(fs: Filesystem) {
    val freeSpace = 70_000_000 - fs.totalSize
    val needToDelete = 30_000_000 - freeSpace

    val dirToDelete = fs
        .filter { it.isDir && it.totalSize >= needToDelete }
        .sortedBy { it.totalSize }
        .first()

    println(dirToDelete.totalSize)
}

data class Filesystem(val name: String, var size: Int = 0, val children: MutableList<Filesystem> = mutableListOf()) :
    Sequence<Filesystem> {
    val totalSize: Int by lazy {
        size + children.sumOf { it.totalSize }
    }

    val isDir: Boolean
        get() = this.children.isNotEmpty()

    override fun iterator(): Iterator<Filesystem> = sequence {
        val fs = this@Filesystem
        yield(fs)
        for (child in fs.children) {
            yieldAll(child.iterator())
        }
    }.iterator()
}

fun buildFilesystem(commands: List<String>): Filesystem {
    val root = Filesystem(name = "/")

    val dirStack = ArrayDeque<Filesystem>()
    dirStack.add(root)

    for (command in commands.drop(1)) {
        val currentDir = dirStack.last()
        when {
            command == "$ ls" -> {}
            command.startsWith("dir") -> {}
            command == "$ cd .." -> {
                dirStack.removeLast()
            }

            command.startsWith("$ cd") -> {
                val parts = command.split(" ")
                val newDir = Filesystem(name = parts[2])
                currentDir.children.add(newDir)
                dirStack.add(newDir)
            }

            else -> {
                val parts = command.split(" ")
                currentDir.children.add(Filesystem(name = parts[1], size = parts[0].toInt()))
            }
        }
    }
    return root
}