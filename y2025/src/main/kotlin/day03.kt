package dev.mickyloo

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("inputs/day03.txt")

    val elapsed1 = measureTimeMillis { solution(lines, digits = 2) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { solution(lines, digits = 12) }
    println("Part2: $elapsed2 ms")
}

private fun solution(lines: List<String>, digits: Int) {
    var total = 0L
    for (line in lines) {
        var string = line
        val output = (digits - 1 downTo 0)
            .joinToString("") {
                val (max, newString) = string.getMaxDigit(it)
                string = newString
                max.toString()
            }.toLong()

        total += output
    }
    println(total)
}

private fun String.getMaxDigit(reserve: Int = 0): Pair<Char, String> {
    val maxDigit = this.dropLast(reserve).withIndex().maxBy { (_, c) -> c }
    return maxDigit.value to this.drop(maxDigit.index + 1)
}