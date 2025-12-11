package dev.mickyloo

import common.powerset
import common.readLines
import common.toNumbers
import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("inputs/day10.test.txt")

    val manuals1 = lines.map {
        val parts = it.split(" ")
        val goalString = parts.first()
        val switchesStrings = parts.drop(1).dropLast(1)

        val goal = goalString
            .substring(1, goalString.length - 1)
            .replace(".", "0")
            .replace("#", "1")
            .reversed()
            .toInt(2)

        val switches = switchesStrings.map { switchString ->
            switchString
                .toNumbers()
                .sumOf { bit -> 1.shl(bit) }
        }
        Manual1(goal, switches)
    }

    val elapsed1 = measureTimeMillis { part1(manuals1) }
    println("Part1: $elapsed1 ms")

    val manuals2 = lines.map { line ->
        val parts = line.split(" ")

        val switches = parts
            .drop(1)
            .dropLast(1)
            .map {
                it.toNumbers().fold(0.toBigInteger()) { acc, i -> acc + 1.toBigInteger().shl(i * 16) }
            }

        switches.sortedBy { it.toByteArray().toHexString().count { c -> c == '1' } }

        val goal =  parts.last().toNumbers()
            .foldIndexed(0.toBigInteger()) { index, acc, value ->
                acc + value.toBigInteger() * 1.toBigInteger().shl(index * 16)
            }

        Manual2(goal, switches)
    }

    val elapsed2 = measureTimeMillis { part2(manuals2) }
    println("Part2: $elapsed2 ms")
}

private fun part1(manuals: List<Manual1>) {
    val total = manuals.sumOf { manual ->
        var count = Int.MAX_VALUE
        for (set in manual.switches.powerset()) {
            if (set.size >= count) {
                continue
            }
            val res = set.fold(0) { acc, next -> acc xor next }
            if (res == manual.goal) {
                count = set.size
            }
        }
        count
    }
    println(total)
}

private fun part2(manuals: List<Manual2>) {

}

data class Manual1(val goal: Int, val switches: List<Int>)

data class Manual2(val goal: BigInteger, val switches: List<BigInteger>)