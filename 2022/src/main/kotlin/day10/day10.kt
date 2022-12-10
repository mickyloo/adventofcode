package day10

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val instructions = File("src/main/kotlin/day10/input.txt")
        .readLines()
        .map { it.split(" ") }

    val values = setup(instructions)
    val elapsed1 = measureTimeMillis { part1(values) }
    val elapsed2 = measureTimeMillis { part2(values) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

fun setup(instructions: List<List<String>>): List<Int> {
    val values = mutableListOf(1, 1)
    instructions.forEach {
        val register = values.last()
        values.add(register)
        when (it[0]) {
            "addx" -> values.add(register + it[1].toInt())
            else -> {} //noop
        }
    }
    return values.toList()
}

fun part1(values: List<Int>) {
    val sum = (20..220 step 40).sumOf {
        it * values[it]
    }
    println(sum)
}

fun part2(values: List<Int>) {
    var clock = 1
    for (row in 0 until 6) {
        for (col in 0 until 40) {
            if (abs(values[clock] - col) <= 1)
                print("⬜")
            else
                print("⬛")
            clock++
        }
        println()
    }
}