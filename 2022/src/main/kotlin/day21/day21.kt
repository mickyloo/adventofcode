package day21

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day21/input.txt")
        .readLines()
        .map {
            val (name, expression) = it.split(": ")
            name to expression
        }

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed1 ms")
}

fun part1(lines: List<Pair<String, String>>) {
    val monkeys = mutableMapOf<String, Lazy<Long>>()
    lines.forEach {
        val parts = it.second.split(" ")
        val expression = when {
            parts.size == 1 -> lazy { parts[0].toLong() }
            parts[1] == "*" -> lazy { monkeys[parts[0]]!!.value * monkeys[parts[2]]!!.value }
            parts[1] == "+" -> lazy { monkeys[parts[0]]!!.value + monkeys[parts[2]]!!.value }
            parts[1] == "-" -> lazy { monkeys[parts[0]]!!.value - monkeys[parts[2]]!!.value }
            parts[1] == "/" -> lazy { monkeys[parts[0]]!!.value / monkeys[parts[2]]!!.value }
            else -> throw Exception("parse error")
        }
        monkeys[it.first] = expression
    }
    println(monkeys["root"]!!.value)
}

fun part2(lines: List<Pair<String, String>>) {

}
