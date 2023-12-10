package day10

import common.Point
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val pipes = mutableMapOf<Point, Type>()
    readLines("day10/input.txt")
        .forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                if (char != '.') {
                    pipes[Point(row, col)] = when(char) {
                        '|' -> Type.VERTICAL
                        else -> throw Error("invalid value")
                }
            }
        }

//    val elapsed1 = measureTimeMillis { part1(lines) }
//    println("Part1: $elapsed1 ms")
//
//    val elapsed2 = measureTimeMillis { part2(lines) }
//    println("Part2: $elapsed2 ms")
}

enum class Type {
    VERTICAL, HORIZONTAL, NE, NW, SW, SE, START
}
private data class Pipe(val point: Point, val type: Char)

