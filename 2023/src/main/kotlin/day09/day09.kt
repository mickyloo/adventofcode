package day09

import common.println
import common.readLines
import common.toNumbers
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day09/input.txt").map { it.toNumbers() }

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

fun part1(lines: List<List<Int>>) {
    lines.sumOf { line ->
        line.sequences().sumOf { it.last() }
    }.println()
}

fun part2(lines: List<List<Int>>) {
    part1(lines.map { it.reversed() })
}

fun List<Int>.sequences(): List<List<Int>> {
    val sequences = mutableListOf(this)
    do {
        val seq = sequences.last().windowed(2).map { it[1] - it[0] }
        sequences.add(seq)
    } while (!seq.all { it == seq.first() })

    return sequences
}