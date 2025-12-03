package dev.mickyloo

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("inputs/day01.txt")

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

private fun part1(lines: List<String>) {
    var position = 50
    var password = 0
    lines.forEach {
        val num = it.drop(1).toInt()
        val sign = if (it[0] == 'L') -1 else 1
        position += sign * num

        position %= 100
        if (position == 0) {
            password++
        }
    }
    println(password)
}

private fun part2(lines: List<String>) {
    var position = 50
    var password = 0
    lines.forEach {
        val num = it.drop(1).toInt()
        val sign = if (it[0] == 'L') -1 else 1

        // full rotations
        password += num / 100

        (0 until (num % 100)).forEach { _ ->
            position = (position + sign).mod(100)
            if (position == 0) {
                password++
            }
        }
    }
    println(password)
}