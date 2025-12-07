package dev.mickyloo

import common.Point
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("inputs/day07.txt")

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

private fun part1(lines: List<String>) {
    val start = Point(lines[0].indexOf('S'), 0)
    var points = setOf(start)
    var count = 0
    lines.drop(1).forEach { _ ->
        points = points.map {
            if (lines[it.y + 1][it.x] == '^') {
                count++
                setOf(
                    Point(it.x - 1, it.y + 1),
                    Point(it.x + 1, it.y + 1)
                )
            } else {
                setOf(Point(it.x, it.y + 1))
            }
        }.flatten().toSet()
    }
    println(count)
}

private fun part2(lines: List<String>) {

}