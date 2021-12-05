package day03

import java.io.File
import kotlin.math.pow

fun main() {
    val lines = File("src/main/kotlin/day03/input.txt")
        .readLines()

    part1(lines)
    part2(lines)
}

fun part2(lines: List<String>) {
    val bits = (lines[0].length - 1 downTo 0).map { (2.0.pow(it)).toInt() }

    fun reduceReport(lines: List<String>, compare: (Int, Double) -> Boolean): Int {
        var report = lines.map { it.toInt(radix = 2) }
        for (bit in bits) {
            val majority = report.size.toDouble() / 2
            val bitCounter = report.count { it and bit > 0 }
            report = if (compare(bitCounter, majority)) {
                report.filter { it and bit > 0 }
            } else {
                report.filter { it and bit == 0 }
            }
            if (report.size == 1) break
        }
        return report[0]
    }

    val o2 = reduceReport(lines) { count, majority -> count >= majority }
    val co2 = reduceReport(lines) { count, majority -> count < majority }
    println(o2 * co2)
}


fun part1(lines: List<String>) {
    val bits = (0 until lines[0].length).map { (2.0.pow(it)).toInt() }
    val maxVal = bits.last() * 2 - 1
    val majority = lines.size / 2

    val report = lines.map { it.toInt(radix = 2) }
    var gamma = 0

    bits.forEach { bit ->
        val bitCounter = report.count { it and bit > 0 }
        if (bitCounter > majority) {
            gamma += bit
        }
    }
    println(gamma * (maxVal - gamma))
}
