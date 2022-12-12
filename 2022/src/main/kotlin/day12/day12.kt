package day12

import java.io.File
import kotlin.system.measureTimeMillis


fun main() {

    val lines = File("src/main/kotlin/day12/input.example.txt").readLines().map { it.toCharArray() }
    var start: Point? = null
    var end: Point? = null
    val map = mutableMapOf<Point, Char>()

    for ((y, row) in lines.withIndex()) {
        for((x, cell) in row.withIndex()) {
            val p = Point(x,y)
            when(cell) {
                'S' -> {
                    start = p
                    map[p] = 'a'
                }
                'E' -> {
                    end = p
                    map[p] = 'z'
                }
                else -> map[p] = cell
            }
        }
    }

    val elapsed1 = measureTimeMillis { part1(map.toMap(), start!!, end!!) }
    val elapsed2 = measureTimeMillis { part2() }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}


data class Point(val x: Int, val y: Int)

private fun Map<Point, Char>.neighbors(p: Point): List<Point> {
    val curr = this[p]!!
    return listOf(
        Point(p.x-1, p.y),
        Point(p.x+1, p.y),
        Point(p.x, p.y-1),
        Point(p.x, p.y+1)
    ).filter {
        val neighbor: Char? = this[it]
        neighbor != null && neighbor >= curr && (neighbor - curr) < 2
    }
}

fun part1(map: Map<Point, Char>, start: Point, end: Point) {
    println(map.neighbors(start)
    )
}

fun part2() {
    TODO("Not yet implemented")
}
