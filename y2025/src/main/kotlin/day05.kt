package dev.mickyloo

import common.anyOverlap
import common.merge
import common.readParagraphs
import kotlin.system.measureTimeMillis

fun main() {
    val paragraphs = readParagraphs("inputs/day05.txt")

    val ranges = paragraphs[0]
        .split("\n", "\r\n")
        .map {
            val (left, right) = it.split("-")
            left.toLong()..right.toLong()
        }

    val ids = paragraphs[1]
        .split("\n", "\r\n")
        .map {
            it.toLong()
        }

    val elapsed1 = measureTimeMillis { part1(ranges, ids) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(ranges) }
    println("Part2: $elapsed2 ms")
}

private fun part1(ranges: List<LongRange>, ids: List<Long>) {
    var total = 0
    for (id in ids) {
        val range = ranges.find { id in it }
        if (range != null) {
            total++
        }
    }
    println(total)
}

private fun part2(ranges: List<LongRange>) {
    val uniqRanges = mutableListOf<LongRange>()

    val ranges2 = ranges.sortedBy { it.first }.toMutableList()
    for ((i, range) in ranges2.withIndex()) {
        if (i >= ranges2.size - 1) {
            uniqRanges.add(range)
            break
        }

        if (range anyOverlap ranges2[i + 1]) {
            ranges2[i + 1] = range merge ranges2[i + 1]
        } else {
            uniqRanges.add(range)
        }
    }

    val total = uniqRanges.sumOf { it.last - it.first + 1 }
    println(total)
}