package day12

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    var system: MutableMap<Cave, MutableList<Cave>> = mutableMapOf()
    File("src/main/kotlin/day12/input.txt")
        .readLines()
        .forEach { line ->
            val (n1, n2) = line.split("-").map(String::toCave)
            system.computeIfAbsent(n1) { mutableListOf() }.add(n2)
            system.computeIfAbsent(n2) { mutableListOf() }.add(n1)
        }

    val elapsed1 = measureTimeMillis {
        run(system) { path, cave -> cave !in path }
    }
    val elapsed2 = measureTimeMillis {
        run(system) { path, cave -> (cave !in path) || !path.hasVisitedSmallCaveTwice }
    }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

private fun String.toCave() =
    when {
        this == "start" -> Start
        this == "end" -> End
        this[0].isUpperCase() -> BigCave(this)
        else -> SmallCave(this)
    }

sealed class Cave(open val name: String)
data class SmallCave(override val name: String) : Cave(name)
data class BigCave(override val name: String) : Cave(name)
object Start : Cave("start")
object End : Cave("end")

data class Path private constructor(val path: List<Cave>, val hasVisitedSmallCaveTwice: Boolean) {
    operator fun contains(cave: Cave): Boolean = cave in this.path
    fun last(): Cave = this.path.last()

    // Initial implementation used an extension function on List<Cave> to calculate hasVisitedSmallCaveTwice at runtime
    // Optimization to calculate hasVisitedSmallCaveTwice at initialization time instead
    // we can also copy the value from the previous path if it is true
    companion object {
        fun new(current: Path, new: Cave) =
            if (current.hasVisitedSmallCaveTwice) {
                Path(current.path + new, hasVisitedSmallCaveTwice = true)
            } else {
                Path(current.path + new, hasVisitedSmallCaveTwice = (new is SmallCave) && (new in current.path))
            }

        fun new(new: Cave) =
            Path(listOf(Start, new), false)
    }
}

fun run(system: Map<Cave, MutableList<Cave>>, shouldVisitSmallCave: (Path, Cave) -> Boolean) {
    var deque: ArrayDeque<Path> = ArrayDeque()
    system[Start]?.forEach { deque.add(Path.new(it)) }

    var paths: MutableList<Path> = mutableListOf()

    while (deque.isNotEmpty()) {
        val path = deque.removeLast()
        system[path.last()]?.forEach { cave ->
            if (cave is End) {
                paths.add(Path.new(path, End))
            } else if ((cave is BigCave) or (cave is SmallCave && shouldVisitSmallCave(path, cave))) {
                deque.add(Path.new(path, cave))
            }
        }
    }
    println(paths.size)
}