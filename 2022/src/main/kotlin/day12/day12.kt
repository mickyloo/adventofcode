package day12

import common.readLines
import kotlin.system.measureTimeMillis


fun main() {

    val lines = readLines("day12/input.txt").map { it.toCharArray() }
    var start: Point? = null
    var end: Point? = null
    val heights = mutableMapOf<Point, Int>()

    for ((y, row) in lines.withIndex()) {
        for ((x, cell) in row.withIndex()) {
            val p = Point(x, y)
            when (cell) {
                'S' -> {
                    start = p
                    heights[p] = 0
                }

                'E' -> {
                    end = p
                    heights[p] = 'z' - 'a'
                }

                else -> heights[p] = cell - 'a'
            }
        }
    }

    val elapsed1 = measureTimeMillis { part1(heights.toMap(), start!!, end!!) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(heights.toMap(), end!!) }
    println("Part2: $elapsed2 ms")
}

fun part2(heights: Map<Point, Int>, end: Point) {
    val minDist = heights
        .filter { it.value == 0 }
        .map { search(heights, it.key, end) }
        .min()

    println(minDist)
}

fun part1(heights: Map<Point, Int>, start: Point, end: Point) {
    val shortestPath = search(heights, start, end)
    println(shortestPath)
}


data class Point(val x: Int, val y: Int)

private fun Map<Point, Int>.neighbors(p: Point): List<Point> {
    val curr = this[p]!!
    return listOf(
        Point(p.x - 1, p.y),
        Point(p.x + 1, p.y),
        Point(p.x, p.y - 1),
        Point(p.x, p.y + 1)
    ).filter {
        val neighbor: Int? = this[it]
        neighbor != null && curr + 1 >= neighbor
    }
}


fun search(heights: Map<Point, Int>, start: Point, end: Point): Int {
    val visited = mutableSetOf(start)
    val queue = mutableListOf(start to 0)

    while (queue.isNotEmpty()) {
        val (current, dist) = queue.removeLast()
        if (current == end) {
            return dist
        }
        for (neighbor in heights.neighbors(current)) {
            if (neighbor !in visited) {
                queue.add(neighbor to dist + 1)
                visited.add(neighbor)
            }
        }
        queue.sortBy { -it.second }
    }

    return Int.MAX_VALUE
}