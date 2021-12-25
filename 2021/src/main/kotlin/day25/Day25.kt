package day25

import java.io.File
import kotlin.system.measureTimeMillis

val lines = File("src/main/kotlin/day25/input.txt")
    .readLines()

val ROWS = lines.size
val COLS = lines[0].length
const val EAST = '>'
const val SOUTH = 'v'

fun main() {
    var herd = mutableMapOf<Cell, Char>()
    for ((row, line) in lines.withIndex()) {
        for ((col, char) in line.withIndex()) {
            if (char != '.') {
                herd[Cell(col, row)] = char
            }
        }
    }

    val elapsed1 = measureTimeMillis { part1(herd) }
    println("Part1: Took $elapsed1 ms")
}

data class Cell(val x: Int, val y: Int)
typealias Grid = MutableMap<Cell, Char>

fun part1(grid: Grid) {
    var curGrid = grid
    var newGrid: MutableMap<Cell, Char>
    var i = 0
    do {
        newGrid = curGrid.step()
        val stable = newGrid == curGrid
        curGrid = newGrid
        i++
    } while (!stable)
    println(i)
}

private fun Grid.step(): Grid {
    val eastMigrated = mutableMapOf<Cell, Char>()
    this.forEach {
        if (it.value == SOUTH) {
            eastMigrated[it.key] = SOUTH
        } else {
            val newCol = (it.key.x + 1).mod(COLS)
            val newCell = Cell(newCol, it.key.y)
            if (newCell in this) {
                eastMigrated[it.key] = EAST
            } else {
                eastMigrated[newCell] = EAST
            }
        }
    }

    val southMigrated = mutableMapOf<Cell, Char>()
    eastMigrated.forEach {
        if (it.value == EAST) {
            southMigrated[it.key] = EAST
        } else {
            val newRow = (it.key.y + 1).mod(ROWS)
            val newCell = Cell(it.key.x, newRow)
            if (newCell in eastMigrated) {
                southMigrated[it.key] = SOUTH
            } else {
                southMigrated[newCell] = SOUTH
            }
        }
    }
    return southMigrated
}