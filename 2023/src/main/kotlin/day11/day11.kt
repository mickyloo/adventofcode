package day11

import common.Point
import common.combinations
import common.println
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val galaxyMap = readLines("day11/input.txt")
        .mapIndexed { row, line ->
            line.mapIndexed { col, char ->
                when (char) {
                    '#' -> Point(col, row)
                    else -> null
                }
            }.filterNotNull()
        }
        .flatten()
        .let {
            GalaxyMap(it)
        }

    val elapsed1 = measureTimeMillis { part1(galaxyMap) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(galaxyMap) }
    println("Part2: $elapsed2 ms")
}

private fun part1(galaxyMap: GalaxyMap) {
    galaxyMap.galaxies
        .combinations()
        .map { galaxyMap.distance(it.first, it.second) }
        .sum()
        .println()
}

private fun part2(galaxyMap: GalaxyMap) {
    galaxyMap.galaxies
        .combinations()
        .map { galaxyMap.distance(it.first, it.second, expansion = 1_000_000) }
        .sum()
        .println()
}

private data class GalaxyMap(val galaxies: List<Point>) {
    val galaxyRows = galaxies.map { it.y }.toSet()
    val galaxyCols = galaxies.map { it.x }.toSet()

    fun distance(src: Point, dest: Point, expansion: Long = 2): Long {
        val (yStart, yEnd) = if (src.y < dest.y) {
            src.y to dest.y
        } else {
            dest.y to src.y
        }

        val (xStart, xEnd) = if (src.x < dest.x) {
            src.x to dest.x
        } else {
            dest.x to src.x
        }

        return (yStart until yEnd).sumOf { row ->
            if (galaxyRows.contains(row)) 1 else expansion
        } + (xStart until xEnd).sumOf { col ->
            if (galaxyCols.contains(col)) 1 else expansion
        }
    }
}