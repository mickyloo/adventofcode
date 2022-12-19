package day18

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val cubes = File("src/main/kotlin/day18/input.txt")
        .readLines()
        .map {
            val (x, y, z) = it.split(",")
            Cube(x.toInt(), y.toInt(), z.toInt())
        }

    val elapsed1 = measureTimeMillis { part1(cubes) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(cubes) }
    println("Part2: $elapsed1 ms")
}


fun part1(cubes: List<Cube>) {
    val surfaces = cubes.flatMap { it.neighbors() }.toList()
    val exposed = surfaces - cubes.toSet()
    println(exposed.size)
}

fun part2(cubes: List<Cube>) {
    // find interior cells
    val setCubes = cubes.toSet()

    val xRange = (cubes.minOf { it.x } - 1)..(cubes.maxOf { it.x } + 1)
    val yRange = (cubes.minOf { it.y } - 1)..(cubes.maxOf { it.y } + 1)
    val zRange = (cubes.minOf { it.z } - 1)..(cubes.maxOf { it.z } + 1)

    val waterQueue = mutableListOf(
        Cube(xRange.first, yRange.first, zRange.first),
    )

    val exteriors = mutableListOf<Cube>()
    val visited = waterQueue.toMutableSet()
    while(waterQueue.isNotEmpty()) {
        val current = waterQueue.removeLast()
        val neighbors = current.neighbors().filter {
            it.x in xRange && it.y in yRange && it.z in zRange && it !in visited
        }

        neighbors.forEach {
            if (it in setCubes) {
                exteriors.add(it)
            } else {
                waterQueue.add(it)
                visited.add(it)
            }
        }
    }
    println(exteriors.size)
}

data class Cube(val x: Int, val y: Int, val z: Int) {
    fun neighbors(): Set<Cube> =
        setOf(
            Cube(x - 1, y, z),
            Cube(x + 1, y, z),
            Cube(x, y - 1, z),
            Cube(x, y + 1, z),
            Cube(x, y, z - 1),
            Cube(x, y, z + 1)
        )

    override fun toString(): String = "$x,$y,$z"
}