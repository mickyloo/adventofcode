package day14

import common.println
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day14/input.txt")

    val elapsed1 = measureTimeMillis { part1(Grid(lines)) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(Grid(lines)) }
    println("Part2: $elapsed2 ms")
}

private fun part1(grid: Grid) {
    grid.tiltNorth()
    val load = grid.grid.mapIndexed { rowIndex, row ->
        row.count { it is Rock } * (grid.rows - rowIndex)
    }.sum()
    println(load)
}

private fun part2(grid: Grid) {
    val rocks = grid.grid
        .flatten()
        .filterIsInstance<Rock>()
        .associateWith { mutableListOf<Int>() }

    repeat(50_000) {
        grid.cycle()
        grid.grid.mapIndexed { rowIndex, cells ->
            cells.filterIsInstance<Rock>()
                .forEach {
                    rocks[it]!!.add(grid.rows - rowIndex)
                }
        }
    }
    println("completed cycles")
    val offset = 10000
    rocks.map { entry ->
        val sequence = entry.value.joinToString("")
        val search = sequence.substring(offset..offset + 10000)
        val modulo = sequence.indexOf(search, offset + 1) - offset

        if (modulo < 0) {
            println("foooooo")
            throw Error()
        } else {
            println(modulo)
        }
        val loadIndex = (1000000000 - offset - 1) % modulo
        entry.value[offset + loadIndex]
    }.sum().println()
}


private sealed interface Cell
private data class Rock(val num: Int) : Cell {
    override fun toString() = "O"

}

private data object Barrier : Cell {
    override fun toString() = "#"
}

private data object Space : Cell {
    override fun toString() = "."
}

private class Grid(val lines: List<String>) {
    val rows = lines.size
    val cols = lines[0].length

    val grid: Array<Array<Cell>> =
        lines.mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                when (c) {
                    'O' -> Rock(row * 10_000 + col)
                    '#' -> Barrier
                    else -> Space
                }
            }.toTypedArray()
        }.toTypedArray()

    fun cycle() {
        tiltNorth()
        tiltWest()
        tiltSouth()
        tiltEast()
    }

    private fun tiltCol(
        init: Int = 0,
        rowIter: IntRange.() -> IntProgression,
        step: Int.() -> Int,
        compare: (Int, Int) -> Boolean
    ) {
        for (col in grid[0].indices) {
            var freeSpace = init
            for (row in grid.indices.rowIter()) {
                when (grid[row][col]) {
                    Barrier -> freeSpace = row.step()
                    is Rock -> {
                        if (compare(row, freeSpace)) {
                            grid[freeSpace][col] = grid[row][col] // swap current Rock with free space
                            grid[row][col] = Space
                            freeSpace = freeSpace.step()
                        } else {
                            freeSpace = row.step()
                        }
                    }
                    Space -> {} // no op
                }
            }
        }
    }

    fun tiltNorth() {
        tiltCol(init = 0, rowIter = { this }, step = { this + 1 }) { row, freeSpace -> row > freeSpace }
    }

    fun tiltSouth() {
        tiltCol(init = rows - 1, rowIter = { this.reversed() }, step = { this - 1 }) { row, freeSpace -> row < freeSpace }
    }

    private fun tiltRow(
        init: Int = 0,
        colIter: IntRange.() -> IntProgression,
        step: Int.() -> Int,
        compare: (Int, Int) -> Boolean
    ) {
        for (row in grid.indices) {
            var freeSpace = init // col
            for (col in grid[0].indices.colIter()) {
                when (grid[row][col]) {
                    Barrier -> freeSpace = col.step()
                    is Rock -> {
                        if (compare(col, freeSpace)) {
                            grid[row][freeSpace] = grid[row][col] // swap current Rock with free space
                            grid[row][col] = Space
                            freeSpace = step(freeSpace)
                        } else {
                            freeSpace = step(col)
                        }
                    }
                    Space -> {} // no op
                }
            }
        }
    }

    fun tiltWest() {
        tiltRow(init = 0, colIter = { this }, step = { this + 1 }) { col, freeSpace -> col > freeSpace }
    }

    fun tiltEast() {
        tiltRow(init = cols - 1, colIter = { this.reversed() }, step = { this - 1 }) { col, freeSpace -> col < freeSpace }
    }

    fun print() {
        grid.forEach { row ->
            row.map { print(it) }
            kotlin.io.println()
        }
    }
}