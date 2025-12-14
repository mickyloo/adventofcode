package dev.mickyloo

import common.readLines
import common.toNumbers
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis


fun main() {
    val points = readLines("inputs/day09.txt")
        .map { line ->
            val nums = line.toNumbers()
            nums[0].toLong() to nums[1].toLong()
        }

    val elapsed1 = measureTimeMillis { part1(points) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(points) }
    println("Part2: $elapsed2 ms")
}

private fun part1(points: List<Pair<Long, Long>>) {
    var rectMax = 0L
    for (i in points.indices) {
        for (j in (i + 1) until points.size) {
            val area = Rect(points[i], points[j]).area
            if (area > rectMax) {
                rectMax = area
            }
        }
    }
    println(rectMax)
}

private fun part2(points: List<Pair<Long, Long>>) {
    val rects = mutableListOf<Rect>()
    for (i in points.indices) {
        for (j in (i + 1) until points.size) {
            rects.add(Rect(points[i], points[j]))
        }
    }
    rects.sortByDescending { it.area }

    val lines = points.zip(points.drop(1) + points.last())
        .map {
            Line(it.first, it.second)
        }.sortedByDescending { line -> line.length }

    for (rect in rects) {
        if (!lines.intersect(rect)) {
            println(rect.area)
            break
        }
    }
}

private data class Rect(val p1: Pair<Long, Long>, val p2: Pair<Long, Long>) {
    val area = (abs(p1.first - p2.first) + 1) * (abs(p1.second - p2.second) + 1)

    val x1 = min(p1.first, p2.first)
    val y1 = min(p1.second, p2.second)
    val x2 = max(p1.first, p2.first)
    val y2 = max(p1.second, p2.second)
}

private data class Line(val p1: Pair<Long, Long>, val p2: Pair<Long, Long>) {
    init {
        require((p1.first - p2.first == 0L) || (p1.second - p2.second == 0L))
    }

    val length = (abs(p1.first - p2.first)) + (abs(p1.second - p2.second))
    val x1 = min(p1.first, p2.first)
    val y1 = min(p1.second, p2.second)
    val x2 = max(p1.first, p2.first)
    val y2 = max(p1.second, p2.second)
}

private fun List<Line>.intersect(r: Rect): Boolean = this.any {
    (it.x1 < r.x2) && (it.y1 < r.y2) && (it.x2 > r.x1) && (it.y2 > r.y1)
}