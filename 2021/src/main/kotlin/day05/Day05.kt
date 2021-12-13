package day05

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day05/input.txt")
        .readLines()

    val elapsed1 = measureTimeMillis { run(lines, useDiagonals = false) }
    val elapsed2 = measureTimeMillis { run(lines, useDiagonals = true) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun run(lines: List<String>, useDiagonals: Boolean) {
    val vents = lines.map { line ->
        val (start, end) = line.split(" -> ").map { Point.parse(it) }
        val deltaX = when {
            start.x < end.x -> 1
            start.x > end.x -> -1
            else -> 0
        }
        val deltaY = when {
            start.y < end.y -> 1
            start.y > end.y -> -1
            else -> 0
        }
        val length = max(abs(start.x - end.x), abs(start.y - end.y))

        if ((deltaX == 0) || (deltaY == 0) || useDiagonals) {
            (0..length).map {
                Point(start.x + it * deltaX, start.y + it * deltaY)
            }
        } else {
            listOf()
        }
    }

    val overlaps = vents.flatten().groupBy { it }.map { it.value.count() }.count { it > 1 }
    println(overlaps)
}

data class Point(val x: Int, val y: Int) {
    companion object {
        fun parse(s: String): Point {
            val (x, y) = s.split(",").map { it.toInt() }
            return Point(x, y)
        }
    }
}