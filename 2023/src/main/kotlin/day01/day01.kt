package day01

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day01/input.txt")

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

fun part1(lines: List<String>) {
    val regex = Regex("""\d""")
    val result = lines.sumOf {
        val digits = regex.findAll(it)
        val num = digits.first().value + digits.last().value
        num.toInt()
    }
    println(result)
}

fun part2(lines: List<String>) {
    val regex = Regex("""\d|one|two|three|four|five|six|seven|eight|nine""")
    val result = lines.sumOf {
        val s = it.replace("oneight", "oneeight")
            .replace("twone", "twoone")
            .replace("threeight", "threeeight")
            .replace("sevenine", "sevennine")
            .replace("eightwo", "eighttwo")
            .replace("eighthree", "eightthree")
            .replace("nineight", "nineeight")

        val digits = regex.findAll(s)
        val num = digits.first().value.toDigit() + digits.last().value.toDigit()
        num.toInt()
    }
    println(result)
}

private fun String.toDigit(): String =
    when (this) {
        "1", "one" -> "1"
        "2", "two" -> "2"
        "3", "three" -> "3"
        "4", "four" -> "4"
        "5", "five" -> "5"
        "6", "six" -> "6"
        "7", "seven" -> "7"
        "8", "eight" -> "8"
        "9", "nine" -> "9"
        else -> throw Exception()
    }