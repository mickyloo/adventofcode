package day07

import java.io.File
import kotlin.system.measureTimeMillis


fun main() {
    val commands = File("src/main/kotlin/day07/input.example.txt").readLines()
    val fs = buildFilesystem(commands)

    fs.filter {it.isDir }.forEach { println(it.name); println(it.totalSize) }

    val elapsed1 = measureTimeMillis { part1(fs) }
//    val elapsed2 = measureTimeMillis { part2() }
//
//    println("Part1: $elapsed1 ms")
//    println("Part2: $elapsed2 ms")
}

data class Filesystem(val name: String, var size: Int = 0, val children: MutableList<Filesystem> = mutableListOf()) : Sequence<Filesystem> {
    val totalSize: Int by lazy {
        size + children.sumOf { it.totalSize }
    }

    private val isFile: Boolean
        get() = this.children.isEmpty()

    val isDir: Boolean
        get() = !isFile

    override fun iterator(): Iterator<Filesystem> = sequence {
        val fs = this@Filesystem
        println(fs)
        yield(fs)
        yieldAll(fs.children)
    }.iterator()

}

fun buildFilesystem(commands: List<String>): Filesystem {
    val root = Filesystem(name="/")

    val dirStack = ArrayDeque<Filesystem>()
    dirStack.add(root)

    for(command in commands.drop(1)) {
        val currentDir = dirStack.last()
        when {
            command == "$ ls" -> {}
            command.startsWith("dir") -> {}
            command == "$ cd .." -> {
                dirStack.removeLast()
            }
            command.startsWith("$ cd") -> {
                val parts = command.split(" ")
                val newDir = Filesystem(name=parts[2])
                currentDir.children.add(newDir)
                dirStack.add(newDir)
            }
            else -> {
                val parts = command.split(" ")
                currentDir.children.add(Filesystem(name=parts[1], size=parts[0].toInt()))
            }
        }
    }
    return root
}

fun part1(fs: Filesystem) {

}

fun part2() {
    TODO("Not yet implemented")
}
