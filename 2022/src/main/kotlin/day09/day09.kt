package day09

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val moves = File("src/main/kotlin/day09/input.txt")
        .readLines()
        .map {
            val parts = it.split(" ")
            Direction.valueOf(parts[0]) to parts[1].toInt()
        }

    val elapsed1 = measureTimeMillis { solution(moves) }
    val elapsed2 = measureTimeMillis { solution(moves, 10) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

data class Delta(val dx: Int, val dy: Int)

val STAY = setOf(
    Delta(0, 0),
    Delta(1, 0),
    Delta(-1, 0),
    Delta(0, 1),
    Delta(0, -1),
    Delta(1, 1),
    Delta(1, -1),
    Delta(-1, 1),
    Delta(-1, -1)
)

enum class Direction(val delta: Delta) {
    U(Delta(0, 1)),
    D(Delta(0, -1)),
    L(Delta(-1, 0)),
    R(Delta(1, 0))
}

data class Point(val x: Int, val y: Int) {

    operator fun plus(delta: Delta): Point = Point(x + delta.dx, y + delta.dy)
    operator fun minus(other: Point): Delta = Delta(x - other.x, y - other.y)

    infix fun follow(head: Point): Point {
        val delta = head - this
        return when {
            delta in STAY -> this
            delta.dx == 0 -> this + if (delta.dy > 0) Direction.U.delta else Direction.D.delta
            delta.dy == 0 -> this + if (delta.dx > 0) Direction.R.delta else Direction.L.delta
            else -> when {
                delta.dx > 0 && delta.dy > 0 -> this + Direction.U.delta + Direction.R.delta
                delta.dx > 0 && delta.dy < 0 -> this + Direction.D.delta + Direction.R.delta
                delta.dx < 0 && delta.dy > 0 -> this + Direction.U.delta + Direction.L.delta
                delta.dx < 0 && delta.dy < 0 -> this + Direction.D.delta + Direction.L.delta
                else -> throw Exception("error")
            }
        }
    }
}

fun solution(moves: List<Pair<Direction, Int>>, size: Int = 2) {
    val rope = Array(size) { Point(0, 0) }
    val tails = mutableSetOf(rope.last())

    for ((dir, times) in moves) {
        repeat(times) {
            for ((i, point) in rope.withIndex()) {
                if (i == 0) {
                    rope[i] = point + dir.delta
                } else {
                    rope[i] = point follow rope[i - 1]
                }
            }
            tails.add(rope.last())
        }
    }
    println(tails.size)
}

private fun Array<Point>.print() {
    val m = this.withIndex().associate { (i, point) ->
        point to i
    }

    (5 downTo 0).forEach { y ->
        (0..5).forEach { x ->
            if (x == 0 && y == 0) {
                print('x')
            } else {
                print(m.getOrDefault(Point(x, y), "."))
            }
        }
        println()
    }
}