package day06

import common.println
import common.readLines
import common.toNumbers
import kotlin.system.measureTimeMillis

fun main() {
    val digits = """(\d+)""".toRegex()
    val lines = readLines("day06/input.txt")
    val races = lines[0].toNumbers().zip(lines[1].toNumbers()) { a, b ->
        Race(a.toLong(),b.toLong())
    }

    val elapsed1 = measureTimeMillis { part1(races) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(races) }
    println("Part2: $elapsed2 ms")
}

private fun part1(races: List<Race>) {
    races
        .map { it.numWins() }
        .fold(1L) { acc, value -> acc * value }
        .println()
}

private fun part2(races: List<Race>) {
    val time = races.joinToString(separator = "") { it.time.toString() }.toLong()
    val distance = races.joinToString(separator = "") { it.distance.toString() }.toLong()
    Race(time, distance).numWins().println()
}

private data class Race(val time: Long, val distance: Long) {
    fun numWins(): Long {
        val firstIndex = (0..this.time).first { it * (this.time - it) > this.distance }
        val lastIndex = (this.time downTo 0).first { it * (this.time - it) > this.distance }
        return lastIndex - firstIndex + 1
    }
}