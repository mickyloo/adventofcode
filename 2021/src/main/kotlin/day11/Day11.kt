package day11

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {

    var lines = File("src/main/kotlin/day11/input.txt").readLines()

    val elapsed1 = measureTimeMillis { part1(lines) }
    val elapsed2 = measureTimeMillis { part2(lines) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part2(lines: List<String>) {
    val grid = Grid.create(lines)
    var step = 0
    while (true) {
        step++
        if (grid.step() == 100) break
    }
    println(step)
}

fun part1(lines: List<String>) {
    val grid = Grid.create(lines)

    val flashes = (0 until 100).sumOf {
        grid.step()
    }
    println(flashes)
}

typealias Cell = Pair<Int, Int>

class Grid(private var grid: MutableMap<Cell, Int>) {
    private val neighbors: Map<Cell, List<Cell>> = grid.keys.associateWith { cell ->
        DIRECTIONS.mapNotNull {
            val neighbor = Cell(cell.first + it.first, cell.second + it.second)
            if (grid.containsKey(neighbor)) neighbor else null
        }
    }

    fun step(): Int {
        grid.forEach {
            grid[it.key] = it.value + 1
        }

        var flashed: MutableSet<Cell> = mutableSetOf()
        var queue: ArrayDeque<Cell> = ArrayDeque()

        queue.addAll(grid.filterValues { it > 9 }.keys)
        while (queue.isNotEmpty()) {
            val cell = queue.removeFirst()
            if (cell in flashed) continue
            flashed.add(cell)
            neighbors[cell]?.forEach {
                grid[it] = grid[it]!! + 1
                if (grid[it]!! > 9) {
                    queue.add(it)
                }
            }
        }

        grid.filterValues { it > 9 }
            .forEach { grid[it.key] = 0 }

        return flashed.size
    }

    companion object {
        val DIRECTIONS: List<Pair<Int, Int>> = listOf(
            -1 to 0, 1 to 0, 0 to -1, 0 to 1,
            -1 to -1, -1 to 1, 1 to -1, 1 to 1
        )

        fun create(lines: List<String>): Grid {
            var grid: MutableMap<Cell, Int> = mutableMapOf()
            for (row in lines.indices) {
                for (col in 0 until lines[0].length) {
                    grid += Cell(row, col) to lines[row][col].toString().toInt()
                }
            }
            return Grid(grid)
        }
    }

    fun print() {
        for (row in 0..9) {
            for (col in 0..9) {
                print("${grid[Cell(row, col)]} ")
            }
            println()
        }
    }
}