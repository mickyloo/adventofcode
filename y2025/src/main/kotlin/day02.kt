package dev.mickyloo

import common.readText
import kotlin.system.measureTimeMillis

fun main() {
    val ranges = readText("inputs/day02.txt")
        .split(',')
        .map {
            val (left, right) = it.split('-')
            left.toLong()..right.toLong()
        }

    val elapsed1 = measureTimeMillis { solution(ranges) { """^(\d+?)(\1)$""".toRegex().matches(it) } }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { solution(ranges) { """^(\d+?)(\1)+$""".toRegex().matches(it) } }
    println("Part2: $elapsed2 ms")
}

private fun solution(ranges: List<LongRange>, check: (numString: String) -> Boolean) {
    var invalids = 0L
    for (range in ranges) {
        for (i in range) {
            val numString = i.toString()
            if (check(numString)) {
                invalids += i
            }
        }
    }
    println(invalids)
}