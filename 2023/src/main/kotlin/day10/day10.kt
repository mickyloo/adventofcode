package day10

import common.Point
import common.println
import common.readLines
import kotlin.math.abs
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
                    else -> pipes[Point(col, row)] = Type.EMPTY
                    }
                }
            }

    val start = pipes.filter { it.value == Type.START }.keys.first()

    val elapsed1 = measureTimeMillis { part1(start, pipes) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(start, pipes) }
    println("Part2: $elapsed2 ms")
}

private enum class Type {
    VERTICAL, HORIZONTAL, NE, NW, SW, SE, START, EMPTY
}

private typealias PipeMap = MutableMap<Point, Type>

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
    findLoop(start, pipes).run {
        println(size / 2)
    }
}

private fun findLoop(start: Point, pipes: PipeMap): Set<Point> {
    val seen = mutableSetOf(start)
    var next = start
    while (seen.size < pipes.size) {
        val possibleNext = pipes.neighbors(next).filter { !seen.contains(it) }
        if (possibleNext.isEmpty()) {
            break
        } else {
            next = possibleNext.first()
            seen.add(next)
        }
    }
    return seen
}


private fun part2(start: Point, pipes: PipeMap) {
    pipes[start] = Type.VERTICAL

    // for horizontal ray, crossing | F 7 enters loop
    val crossCheck = setOf(Type.VERTICAL, Type.SE, Type.SW)
    val loop = findLoop(start, pipes).toSet()
    val horizontalBoundaries = loop.filter {
        crossCheck.contains(pipes[it]!!)
    }.toSet()

    // use point in polygon
    // cast a horizontal ray from 0  to x and count how many times it cross boundaries
    // cast a vertical ray from 0  to y and count how many times it cross boundaries
    // inside if both horizontal and vertical cross odd number of times
    pipes
        .filter { !loop.contains(it.key) }
        .map { (point, _) ->
            val horizontalRay = (0 until point.x).map { x -> Point(x, point.y) }.toSet()
            val horizontalCross = horizontalBoundaries.intersect(horizontalRay).size
            val outside = horizontalCross % 2 == 0
            if (outside) 0 else 1
        }.sum()
        .println()
}
