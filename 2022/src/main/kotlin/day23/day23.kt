package day23

import common.Point
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val elves = readLines("day23/input.txt")
        .mapIndexed { y, line ->
            line.chunked(1).mapIndexed { x, char ->
                if (char == "#")
                    Point(x, y)
                else
                    null
            }.filterNotNull()
        }
        .flatten()
        .toSet()

    val elapsed1 = measureTimeMillis { part1(elves) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(elves) }
    println("Part2: $elapsed2 ms")
}

enum class Direction {
    N, S, E, W
}

private fun Point.adjacent(direction: Direction): List<Point> =
    when (direction) {
        Direction.N -> listOf(Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1))
        Direction.S -> listOf(Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1))
        Direction.E -> listOf(Point(x + 1, y - 1), Point(x + 1, y), Point(x + 1, y + 1))
        Direction.W -> listOf(Point(x - 1, y - 1), Point(x - 1, y), Point(x - 1, y + 1))
    }

fun part1(elves: Set<Point>) {
    val directions = ArrayDeque(listOf(Direction.N, Direction.S, Direction.W, Direction.E))
    var current = elves
    repeat(10) {
        current = moveRound(current, directions)
        directions.addLast(directions.removeFirst()) // rotate directions
    }
    val width = current.maxOf { it.x } - current.minOf { it.x } + 1
    val length = current.maxOf { it.y } - current.minOf { it.y } + 1
    println((width * length) - current.size)
}

fun part2(elves: Set<Point>) {
    val directions = ArrayDeque(listOf(Direction.N, Direction.S, Direction.W, Direction.E))
    var current = elves
    var iterations = 1
    while (true) {
        val newCurrent = moveRound(current, directions)
        if (newCurrent == current) {
            break
        }

        directions.addLast(directions.removeFirst()) // rotate directions
        current = newCurrent
        iterations++
    }
    println(iterations)
}

fun moveRound(elves: Set<Point>, directions: ArrayDeque<Direction>): Set<Point> {
    val proposedMoves = elves.map { elf ->
        var newMove: Point = elf
        if ((elf.neighbors8() - elves).size == 8) {
            // no neighbors, doesn't move
            newMove = elf
        } else {
            for (dir in directions) {
                val adjacents = elf.adjacent(dir)
                if ((adjacents.toSet() - elves).size == 3) {
                    newMove = adjacents[1]
                    break
                }
            }
        }
        elf to newMove
    }

    val collisions = proposedMoves.groupingBy { it.second }.eachCount().filter { it.value > 1 }.keys.toSet()

    return proposedMoves.map { (current, new) ->
        if (new in collisions)
            current
        else
            new
    }.toSet()
}
