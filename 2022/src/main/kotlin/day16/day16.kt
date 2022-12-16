package day16

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day16/input.txt")
        .readLines()

    val elapsed1 = measureTimeMillis { part1(lines) }
    val elapsed2 = measureTimeMillis { part2(lines) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

fun part1(lines: List<String>) {
}

fun part2(lines: List<String>) {
}
