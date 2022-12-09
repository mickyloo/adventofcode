package day09

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

enum class Direction {
    U, D, L, R
}

fun main() {
    val moves = File("src/main/kotlin/day09/input.txt")
        .readLines()
        .map {
            val parts = it.split(" ")
            Direction.valueOf(parts[0]) to parts[1].toInt()
        }

    val elapsed1 = measureTimeMillis { part1(moves) }
    //val elapsed2 = measureTimeMillis { part2(moves) }

    println("Part1: $elapsed1 ms")
    //println("Part2: $elapsed2 ms")
}

typealias Point = Pair<Int, Int>


private data class State(val head: Point, val tail: Point) {
    infix fun moveTo(dir: Direction): State {
        val newHead = when (dir) {
            Direction.U -> head.first to head.second + 1
            Direction.D -> head.first to head.second - 1
            Direction.L -> head.first - 1 to head.second
            Direction.R -> head.first + 1 to head.second
        }

        val dx = abs(tail.first - newHead.first)
        val dy = abs(tail.second - newHead.second)
        val newTail = when (dir) {
            Direction.U -> if (dy > 1) newHead.first to tail.second + 1 else tail
            Direction.D -> if (dy > 1) newHead.first to tail.second - 1 else tail
            Direction.L -> if (dx > 1) tail.first - 1 to newHead.second else tail
            Direction.R -> if (dx > 1) tail.first + 1 to newHead.second else tail
        }

        return State(newHead, newTail)
    }
}


fun part1(moves: List<Pair<Direction, Int>>) {
    val points = mutableSetOf<Point>(0 to 0)
    var state = State(0 to 0, 0 to 0)
    for ((dir, times) in moves) {
        repeat(times) {
            state = state moveTo dir
            points.add(state.tail)
        }
    }
    println(points.size)
}

fun part2(moves: List<Pair<Direction, Int>>) {
    TODO("Not yet implemented")
}
