package day09

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

data class Point(val x: Int, val y: Int) {
    operator fun plus(delta: Delta): Point = Point(x + delta.dx, y + delta.dy)
}
data class Delta(val dx: Int, val dy: Int)

enum class Direction(val delta: Delta) {
    U(Delta(0, 1)),
    D(Delta(0, -1)),
    L(Delta(-1, 0)),
    R(Delta(1, 0))
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



//private data class Rope(val head: Point, val tail: Point) {
//    infix fun moveTo(dir: Direction): Rope {
//        val newHead = moveHead(dir)
//        val newTail = moveTail(dir, newHead)
//        return Rope(newHead, newTail)
//    }
//
//    fun moveHead(dir: Direction): Point = head + dir.delta
//
//    fun moveTail(dir: Direction, newHead: Point): Point {
//        val dx = abs(tail.x - newHead.x)
//        val dy = abs(tail.y - newHead.y)
//        val newTail = when (dir) {
//            Direction.U -> if (dy > 1) newHead.x to tail.y + 1 else tail
//            Direction.D -> if (dy > 1) newHead.x to tail.y - 1 else tail
//            Direction.L -> if (dx > 1) tail.x - 1 to newHead.y else tail
//            Direction.R -> if (dx > 1) tail.x + 1 to newHead.y else tail
//        }
//        return newTail
//    }
//
//    fun moveTail(newHead: Point) : Point {
//        val dx = tail.x - newHead.x
//        val dy = tail.y - newHead.y
//
//        val newTail = when {
//            dy == 0 && abs(dx) > 1 -> tail.x + (if (dx > 0) -1 else 1) to tail.y
//            dx == 0 && abs(dy) > 1 -> tail.x to tail.y + (if (dy > 0) -1 else 1)
//            dy > 1 -> { newHead.x to tail.y - 1 }
//            dy < -1 -> { newHead.x to tail.y + 1 }
//            dx > 1 -> { tail.x - 1 to newHead.y }
//            dx < -1 -> { tail.x + 1 to newHead.y }
//            else -> tail  // no move
//        }
//
//        return newTail
//    }
//}

fun part1(moves: List<Pair<Direction, Int>>) {
    val rope = Array(2) { _ -> Point(0,0) }
    val tails = mutableSetOf<Point>(rope.last())

    for ((dir, times) in moves) {
        repeat(times) {
            head += dir.delta
            tail += dir.delta
            tails.add(tail)
        }
    }
    println(tails.size)
}

//fun part2(moves: List<Pair<Direction, Int>>) {
//    val tails = mutableListOf<Point>(0 to 0)
//    val segmentedRope = Array(9) { _ -> Rope(0 to 0, 0 to 0) }
//    for ((dir, times) in moves) {
//        repeat(times) {
//            for((i, rope) in segmentedRope.withIndex()) {
//                if (i > 0) {
//                    val newHead = segmentedRope[i-1].tail
//                    segmentedRope[i] = Rope(newHead, rope.moveTail(newHead))
//                } else {
//                    segmentedRope[i] = rope moveTo dir
//                }
//            }
//            tails.add(segmentedRope.last().tail)
//        }
//    }
//    println(tails)
//    println(tails.toSet().size)
//}