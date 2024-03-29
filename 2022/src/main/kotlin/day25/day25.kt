package day25

import common.readLines
import kotlin.math.abs
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {
    val numbers = readLines("day25/input.txt")
        .map { line ->
            line.reversed().chunked(1).map {
                when (it) {
                    "=" -> -2
                    "-" -> -1
                    else -> it.toInt()
                }
            }
        }

    val elapsed1 = measureTimeMillis { part1(numbers) }
    println("Part1: $elapsed1 ms")
}

val base5 = mapOf(0 to 0, 1 to 1, 2 to 2, 3 to -2, 4 to -1)
val snafu = mapOf(0 to "0", 1 to "1", 2 to "2", -2 to "=", -1 to "-")
fun List<Int>.toSnafu() =
    this.joinToString("") {
        snafu[it]!!
    }.reversed()

fun List<Int>.toBase10() =
    this.mapIndexed { i, value ->
        value * 5.toDouble().pow(i.toDouble())
    }.sum().toLong()

fun part1(numbers: List<List<Int>>) {
    val maxLen = numbers.maxOf { it.size }
    var sum = (0 until maxLen)
        .map { index ->
            numbers.sumOf { it.getOrElse(index) { 0 } }
        }.toMutableList()

    sum.add(sum.size, 0)
    var carries = (0..sum.size).map { 0 }.toMutableList()

    for ((i, value) in sum.withIndex()) {
        val total = value + carries[i]
        if (total > 0) {
            val newDigit = base5[total.mod(5)]!!
            sum[i] = newDigit
            carries[i + 1] = abs(total / 5)
            if (newDigit < 0) {
                carries[i + 1] += 1
            }
        } else {
            sum[i] = total % 5
            carries[i + 1] = total / 5
        }

    }

    println(sum.toSnafu())
}