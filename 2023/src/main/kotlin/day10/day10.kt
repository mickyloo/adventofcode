package day10

import common.Point
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val pipes = mutableMapOf<Point, Type>()
    readLines("day10/input.txt")
        .forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                when (char) {
                    '|' -> pipes[Point(col, row)] = Type.VERTICAL
                    '-' -> pipes[Point(col, row)] = Type.HORIZONTAL
                    'L' -> pipes[Point(col, row)] = Type.NE
                    'J' -> pipes[Point(col, row)] = Type.NW
                    '7' -> pipes[Point(col, row)] = Type.SW
                    'F' -> pipes[Point(col, row)] = Type.SE
                    'S' -> pipes[Point(col, row)] = Type.START
                    else -> { // no op , ignore ground }
                    }
                }
            }
        }

    val start = pipes.filter { it.value == Type.START }.keys.first()
    val elapsed1 = measureTimeMillis { part1(start, pipes.toMap()) }
    println("Part1: $elapsed1 ms")

//    val elapsed2 = measureTimeMillis { part2(pipes) }
//    println("Part2: $elapsed2 ms")
}
private enum class Type {
    VERTICAL, HORIZONTAL, NE, NW, SW, SE, START
}

private typealias PipeMap = Map<Point, Type>

private fun PipeMap.neighbors(point: Point) : Set<Point> {
    val neighbors = mutableSetOf<Point>()
    val type = this[point]!!

    if (type == Type.VERTICAL || type == Type.START || type == Type.NE || type == Type.NW) {
        val north = Point(point.x, point.y - 1)
        this[north]?.let {
            if (it == Type.VERTICAL || it == Type.SE || it == Type.SW || it == Type.START) {
                neighbors.add(north)
            }
        }
    }

    if (type == Type.VERTICAL || type == Type.START || type == Type.SE || type == Type.SW) {
        val south = Point(point.x, point.y + 1)
        this[south]?.let {
            if (it == Type.VERTICAL || it == Type.NE || it == Type.NW || it == Type.START) {
                neighbors.add(south)
            }
        }
    }

    if (type == Type.HORIZONTAL || type == Type.START || type == Type.SE || type == Type.NE) {
        val east = Point(point.x + 1, point.y)
        this[east]?.let {
            if (it == Type.HORIZONTAL || it == Type.NW || it == Type.SW || it == Type.START) {
                neighbors.add(east)
            }
        }
    }

    if (type == Type.HORIZONTAL || type == Type.START || type == Type.SW || type == Type.NW) {
        val west = Point(point.x - 1, point.y)
        this[west]?.let {
            if (it == Type.HORIZONTAL || it == Type.NE || it == Type.SE|| it == Type.START) {
                neighbors.add(west)
            }
        }
    }

    return neighbors
}
private fun part1(start: Point, pipes: PipeMap) {
    val seen = mutableSetOf(start)
    var next = start
    while(seen.size < pipes.size) {
        val possibleNext = pipes.neighbors(next).filter { !seen.contains(it) }
        if (possibleNext.isEmpty()) {
            break
        } else {
            next = possibleNext.first()
            seen.add(next)
        }
    }
    println(seen.size / 2)
}