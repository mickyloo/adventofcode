package day02

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val commands = File("src/main/kotlin/day02/input.txt")
        .readLines()
        .map {
            val (command, value) = it.split(" ")
            Pair(command, value.toInt())
        }

    val elapsed1 = measureTimeMillis { part1(commands) }
    val elapsed2 = measureTimeMillis { part2(commands) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part1(commands: List<Pair<String, Int>>) {
    var dist = 0
    var depth = 0

    commands.forEach { command ->
        when (command.first) {
            "forward" -> dist += command.second
            "down" -> depth += command.second
            "up" -> depth -= command.second
            else -> println("parse error")
        }
    }
    println(dist * depth)
}


fun part2(commands: List<Pair<String, Int>>) {
    var dist = 0
    var depth = 0
    var aim = 0

    commands.forEach { command ->
        when (command.first) {
            "forward" -> {
                dist += command.second
                depth += aim * command.second
            }
            "down" -> aim += command.second
            "up" -> aim -= command.second
            else -> println("parse error")
        }
    }
    println(dist * depth)
}