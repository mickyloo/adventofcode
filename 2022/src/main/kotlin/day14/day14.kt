package day14

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {

    val lines = readLines("day14/input.txt")
        .map { line -> line.split(" -> ").map { it.toPoint() } }

    val elapsed1 = measureTimeMillis { part1(lines.toCave()) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines.toCave()) }
    println("Part2: $elapsed2 ms")
}

private fun List<List<Point>>.toCave(): MutableMap<Point, Cave> {
    val cave = mutableMapOf<Point, Cave>()
    forEach { rocks ->
        rocks.windowed(2).map {
            val (start, end) = it
            start.path(end).forEach { cell ->
                cave[cell] = Cave.ROCK
            }
        }
    }
    return cave
}

fun part1(cave: MutableMap<Point, Cave>) {
    val maxY = cave.keys.maxOf { it.y }
    var sand = Point(500, 0)
    var count = 0
    while (true) {
        if (sand.y > maxY)
            break

        val down = Point(sand.x, sand.y + 1)
        val left = Point(sand.x - 1, sand.y + 1)
        val right = Point(sand.x + 1, sand.y + 1)

        sand = when {
            !cave.containsKey(down) -> down
            !cave.containsKey(left) -> left
            !cave.containsKey(right) -> right
            else -> {
                cave[sand] = Cave.SAND
                count++
                Point(500, 0)
            }
        }
    }
    println(count)
}

fun part2(cave: MutableMap<Point, Cave>) {
    val maxY = cave.keys.maxOf { it.y }
    val floor = maxY + 2

    val origin = Point(500, 0)
    var sand = origin
    var count = 0
    while (true) {
        if (cave.containsKey(origin))
            break

        val down = Point(sand.x, sand.y + 1)
        val left = Point(sand.x - 1, sand.y + 1)
        val right = Point(sand.x + 1, sand.y + 1)

        sand = when {
            !cave.containsKey(down) && down.y != floor -> down
            !cave.containsKey(left) && left.y != floor -> left
            !cave.containsKey(right) && right.y != floor -> right
            else -> {
                cave[sand] = Cave.SAND
                count++
                Point(500, 0)
            }
        }
    }
    println(count)
}

enum class Cave {
    ROCK, SAND
}

private fun String.toPoint(): Point {
    val (x, y) = this.split(",")
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