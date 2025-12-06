package dev.mickyloo

import common.Point
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("inputs/day04.txt")

    val elapsed1 = measureTimeMillis { part1(Grid(lines)) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(Grid(lines)) }
    println("Part2: $elapsed2 ms")
}

private fun part1(grid: Grid) {
    var total = 0

    for (col in grid.bounds) {
        for (row in grid.bounds) {
            val cell = Point(col, row)

            if (grid[cell] == '.')
                continue

            val count = cell
                .neighbors8()
                .count { grid.contains(it, '@') }

            if (count < 4)
                total++
        }
    }
    println(total)
}

private fun part2(grid: Grid) {
    var removed = 0

    do {
        val state = grid.toString()
        for (col in grid.bounds) {
            for (row in grid.bounds) {
                val cell = Point(col, row)
                if (grid[cell] == '.')
                    continue

                val count = cell
                    .neighbors8()
                    .count { grid.contains(it, '@') }

                if (count < 4) {
                    removed++
                    grid.remove(cell)
                }
            }
        }
    } while (state != grid.toString())

    println(removed)
}

private class Grid {
    val grid: MutableList<String>
    val bounds: IntRange

    constructor(grid: List<String>) {
        this.grid = grid.toMutableList()
        this.bounds = 0 until grid.size
    }

    fun contains(p: Point, value: Char): Boolean {
        if (p.y !in bounds || p.x !in bounds) {
            return false
        }
        return get(p) == value
    }

    operator fun get(p: Point): Char = grid[p.y][p.x]

    fun remove(p: Point) {
        val row = grid[p.y].toCharArray()
        row[p.x] = '.'
        grid[p.y] = String(row)
    }

    override fun toString() = grid.toString()
}
