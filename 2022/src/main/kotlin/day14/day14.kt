package day14

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {

    val lines = File("src/main/kotlin/day14/input.example.txt").readLines()
        .map { line -> line.split(" -> ").map { it.toPoint() } }

    val cave = mutableMapOf<Point, Cave>()
    lines.forEach { rocks ->
        rocks.windowed(2).map {
            val (start, end) = it
            start.path(end).forEach { cell ->
                cave[cell] = Cave.ROCK
            }
        }
    }
    println(cave.size)

    println(cave)

    val elapsed1 = measureTimeMillis { part1(cave) }
//    val elapsed2 = measureTimeMillis { part2(blocks) }
//
    println("Part1: $elapsed1 ms")
//    println("Part2: $elapsed2 ms")
}

fun part1(cave: MutableMap<Point, Cave>) {
    val maxY = cave.keys.maxOf { it.y } + 1
    val dirs = listOf((0 to 1), (-1 to 1), (1 to 1))
    while(true) {
        var sand = Point(500, 0)


    }
}

enum class Cave {
    ROCK, SAND
}

private fun String.toPoint(): Point {
    val (x,y) = this.split(",")
    return Point(x.toInt(), y.toInt())
}

data class Point(val x: Int, val y: Int) {
     fun path(other: Point): List<Point> =
         when {
             (x == other.x) && (y < other.y) -> (y..other.y).map { Point(x, it) }
             (x == other.x) && (y > other.y) -> (y downTo other.y).map { Point(x, it) }
             (y == other.y) && (x < other.x) -> (x..other.x).map { Point(it, y) }
             (y == other.y) && (x > other.x) -> (x downTo other.x).map { Point(it, y) }
             else -> throw Exception("parse error")
         }
 }