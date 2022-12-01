package day01

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val elves = File("src/main/kotlin/day01/input.txt")
        .readText()
        .trim()
        .split("\n\n", "\r\n\r\n")
        .map { elf -> elf.split("\n", "\r\n").map { it.toInt() } }

    val elapsed1 = measureTimeMillis { part1(elves) }
    val elapsed2 = measureTimeMillis { part2(elves) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

fun part1(elves: List<List<Int>>) {
    println(elves.maxOfOrNull { elf -> elf.sum() })
}

fun part2(elves: List<List<Int>>) {
    val top3 = elves
        .map { it.sum() }
        .sortedDescending()
        .take(3)
        .sum()
    println(top3)
}
