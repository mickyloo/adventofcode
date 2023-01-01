package day03

import common.readLines
import kotlin.system.measureTimeMillis


private val VALUES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    .toCharArray()
    .mapIndexed { index, c -> c to index + 1 }
    .toMap()

fun main() {
    val lines = readLines("day03/input.txt")
        .map { it.toCharArray() }

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

fun part1(lines: List<CharArray>) {
    val intersects = lines
        .map {
            val half = it.size / 2
            val first = it.sliceArray(0 until half)
            val second = it.sliceArray(half until it.size)

            first.intersect(second.toSet()).first()
        }
        .sumOf { VALUES.getOrDefault(it, 0) }
    println(intersects)
}

fun part2(lines: List<CharArray>) {
    val groupIntersects = lines
        .chunked(3) {
            it[0].intersect(it[1].toSet()).intersect(it[2].toSet()).first()
        }
        .sumOf { VALUES.getOrDefault(it, 0) }

    println(groupIntersects)
}
