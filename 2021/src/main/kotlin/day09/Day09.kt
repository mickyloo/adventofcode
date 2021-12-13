package day09

import java.io.File
import kotlin.system.measureTimeMillis

val lines = File("src/main/kotlin/day09/input.txt")
    .readLines()
    .map { line -> line.map { it.toString().toByte() } }

val DIRECTIONS: List<Pair<Int, Int>> = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

fun main() {
    val elapsed1 = measureTimeMillis { part1() }
    val elapsed2 = measureTimeMillis { part2() }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part2() {
    val basins = getLowPoints()
        .map(Pair<Int, Int>::basinSize)
        .sortedDescending()
        .take(3)
    println(basins[0] * basins[1] * basins[2])
}

fun part1() {
    val sum = getLowPoints()
        .sumOf { lines[it] + 1 }
    println(sum)
}

fun getLowPoints(): List<Pair<Int, Int>> {
    var lows: List<Pair<Int, Int>> = mutableListOf()
    for (row in lines.indices) {
        for (col in lines[0].indices) {
            val cur = Pair(row, col)
            val lowest = cur.neighbors().all { lines[cur] < lines[it] }
            if (lowest) {
                lows += cur
            }
        }
    }
    return lows
}

private operator fun List<List<Byte>>.get(xy: Pair<Int, Int>): Byte = this[xy.first][xy.second]

private fun Pair<Int, Int>.neighbors(): List<Pair<Int, Int>> = DIRECTIONS
    .mapNotNull {
        val newRow = this.first + it.first
        val newCol = this.second + it.second
        lines.getOrNull(newRow)?.getOrNull(newCol)?.let {
            Pair(newRow, newCol)
        }
    }.toList()

private fun Pair<Int, Int>.basinSize(): Int {
    var found: Set<Pair<Int, Int>> = mutableSetOf(this)
    var queue: ArrayDeque<Pair<Int, Int>> = ArrayDeque()
    queue.addFirst(this)
    while (queue.isNotEmpty()) {
        val next = queue.removeFirst()
        next.neighbors()
            .filter { (lines[it] < 9) && (it !in found) }
            .forEach {
                found += it
                queue.add(it)
            }
    }

    return found.size
}