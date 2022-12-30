package day24

import common.Point
import java.io.File
import kotlin.system.measureTimeMillis

enum class Direction(val dx: Int, val dy: Int) {
    U(0, -1),
    D(0,1),
    L(-1, 0),
    R(1,0),
}

fun main() {
    val blizzards = File("src/main/kotlin/day24/input.example.txt")
        .readLines()
        .drop(1).dropLast(1) // skip first and last row, they are walls
        .flatMapIndexed { y, row ->
            row.drop(1).dropLast(1)
                .mapIndexed { x, col ->
                    when(col) {
                        '^' -> Point(x,y) to Direction.U
                        'v' -> Point(x,y) to Direction.D
                        '<' -> Point(x,y) to Direction.L
                        '>' -> Point(x,y) to Direction.R
                        else -> null
                    }
                }.filterNotNull()
        }.toMap()

    val elapsed1 = measureTimeMillis { part1(blizzards) }
    println("Part1: $elapsed1 ms")
}

class Grid(val blizzards: Map<Point, Direction>) {
    private val cols = blizzards.maxOf { it.key.x } + 1
    private val rows = blizzards.maxOf { it.key.y } + 1

    val START = Point(0, -1)
    val END = Point(cols, rows + 1)

    fun isValid(position: Point, minutes: Int): Boolean {
        //println("position $position min $minutes")
        val collisionHorizontal = blizzards
            .filter { it.key.y == position.y && (it.value == Direction.L || it.value == Direction.R) }
            .any {
                val newX = (it.key.x + it.value.dx * minutes).mod(cols)
                newX == position.x
            }

        val collisionVertical = blizzards
            .filter { it.key.x == position.x && (it.value == Direction.U || it.value == Direction.D) }
            .any {
                val newY = (it.key.y + it.value.dy * minutes).mod(rows)
                newY == position.y
            }

        return !collisionVertical && !collisionHorizontal
    }

    fun neighbors(state: State): List<Point> =
        listOf(
            Point(state.position.x + Direction.U.dx, state.position.y + Direction.U.dy),
            Point(state.position.x + Direction.D.dx, state.position.y + Direction.D.dy),
            Point(state.position.x + Direction.L.dx, state.position.y + Direction.L.dy),
            Point(state.position.x + Direction.R.dx, state.position.y + Direction.R.dy)
        ).filter {
            val inGrid = it.x in 0 until cols && it.y in 0 until rows
            (inGrid || it == END) && isValid(it, state.minutes + 1)
        }
}

data class State(val position: Point, val minutes: Int)

fun part1(blizzards: Map<Point, Direction>) {
    val grid = Grid(blizzards)

    var work = ArrayDeque<State>().also { it.add(State(grid.START, 0)) }
    while(work.isNotEmpty()) {
        val state = work.removeLast()

        if (state.position == grid.END) {
            println(state)
            break
        }

        val neighbors = grid.neighbors(state)
        println("state $state, n=$neighbors")
        if (neighbors.isEmpty()) {
            work.add(State(state.position, state.minutes + 1))
        } else {
            neighbors.forEach { work.add(State(it, state.minutes + 1)) }
        }
        println(work.size)
    }
}
