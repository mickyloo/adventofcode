package day04

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val regex = Regex("""^(\d+)\-(\d+)\,(\d+)\-(\d+)$""")
    val lines = readLines("day04/input.txt")
        .map {
            val matches = regex.matchEntire(it)!!
            val (e1Start, e1End, e2Start, e2End) = matches.destructured
            e1Start.toInt()..e1End.toInt() to e2Start.toInt()..e2End.toInt()
        }

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

fun part1(lines: List<Pair<IntRange, IntRange>>) {
    println(lines.count { it.first fullOverlap it.second })
}

fun part2(lines: List<Pair<IntRange, IntRange>>) {
    println(lines.count { it.first anyOverlap it.second })
}

infix fun IntRange.fullOverlap(other: IntRange): Boolean =
    (this.first >= other.first && this.last <= other.last) || (this.first <= other.first && this.last >= other.last)

infix fun IntRange.anyOverlap(other: IntRange): Boolean =
    !(this.last < other.first || this.first > other.last)

