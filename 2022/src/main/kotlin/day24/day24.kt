package day24

import common.Point
import java.io.File
import kotlin.system.measureTimeMillis

enum class Direction(val dx: Int, val dy: Int) {
    U(0, -1),
    D(0, 1),
    L(-1, 0),
    R(1, 0),
}

fun main() {
    val blizzards = File("src/main/kotlin/day24/input.txt")
        .readLines()
        .drop(1).dropLast(1) // skip first and last row, they are walls
        .flatMapIndexed { y, row ->
            row.drop(1).dropLast(1)
                .mapIndexed { x, col ->
                    when (col) {
                        '^' -> Point(x, y) to Direction.U
                        'v' -> Point(x, y) to Direction.D
                        '<' -> Point(x, y) to Direction.L
                        '>' -> Point(x, y) to Direction.R
                        else -> null
                    }
                }.filterNotNull()
        }.toMap()

    val elapsed1 = measureTimeMillis { part1(blizzards) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(blizzards) }
    println("Part2: $elapsed2 ms")
}

class Grid(private val blizzards: Map<Point, Direction>) {
    private val cols = blizzards.entries.groupBy { it.key.x }.toMap()
    private val rows = blizzards.entries.groupBy { it.key.y }.toMap()

    val numCols = cols.size
    val numRows = rows.size

    val START = Point(0, -1)
    val END = Point(numCols - 1, numRows)
    private fun isValid(position: Point, minutes: Int): Boolean {
        val collisionHorizontal = rows.getOrDefault(position.y, emptyList())
            .filter { it.value == Direction.L || it.value == Direction.R }
            .any {
                val newX = (it.key.x + it.value.dx * minutes).mod(numCols)
                newX == position.x
            }

        val collisionVertical = cols.getOrDefault(position.x, emptyList())
            .filter { it.value == Direction.U || it.value == Direction.D }
            .any {
                val newY = (it.key.y + it.value.dy * minutes).mod(numRows)
                newY == position.y
            }

        return !collisionVertical && !collisionHorizontal
    }

    private fun neighbors(state: State): List<Point> =
        listOf(
            Point(state.position.x, state.position.y),
            Point(state.position.x + Direction.U.dx, state.position.y + Direction.U.dy),
            Point(state.position.x + Direction.D.dx, state.position.y + Direction.D.dy),
            Point(state.position.x + Direction.L.dx, state.position.y + Direction.L.dy),
            Point(state.position.x + Direction.R.dx, state.position.y + Direction.R.dy)
        ).filter {
            val inGrid = it.x in 0 until numCols && it.y in 0 until numRows
            (inGrid && isValid(it, state.minutes + 1)) || it == END || it == START
        }

    fun findPathTime(start: Point, end: Point, startTime: Int = 0): Int {
        var work = ArrayDeque<State>().also { it.add(State(start, startTime)) }
        var visited = mutableSetOf<State>()

        var minTime = startTime + 1000
        while (work.isNotEmpty()) {
            val state = work.removeLast()

            if (state.position == end) {
                minTime = minOf(minTime, state.minutes)
                continue
            }

            if (state in visited) {
                continue
            }
            if (state.minutes > minTime) {
                continue
            }

            val neighbors = this.neighbors(state)
            neighbors.forEach { work.add(State(it, state.minutes + 1)) }

            visited.add(state)
        }

        return minTime
    }
}

data class State(val position: Point, val minutes: Int)


fun part1(blizzards: Map<Point, Direction>) {
    val grid = Grid(blizzards)
    println(grid.findPathTime(grid.START, grid.END))
}

fun part2(blizzards: Map<Point, Direction>) {
    val grid = Grid(blizzards)
    val firstTrip = grid.findPathTime(grid.START, grid.END)
    val returnTrip = grid.findPathTime(grid.END, grid.START, startTime = firstTrip)
    val finalTrip = grid.findPathTime(grid.START, grid.END, startTime = returnTrip)
    println(finalTrip)
}
