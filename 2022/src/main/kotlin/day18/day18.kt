package day18

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val cubes = File("src/main/kotlin/day18/input.example.txt")
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
    val exposed = surfaces - cubes
    println(exposed.size)

    exposed.forEach { println(it) }
}

fun part2(cubes: List<Cube>) {

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