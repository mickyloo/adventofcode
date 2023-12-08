package day08

import common.lcm
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day08/input.txt")

    val directions = lines[0].toCharArray()
    val network = lines.drop(2).associate {
        val (name, left, right) = it.replace("= (", "").replace(")", "").replace(",", "").split(" ")
        name to Node(left, right)
    }

    val elapsed1 = measureTimeMillis { part1(directions, network) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(directions, network) }
    println("Part2: $elapsed2 ms")
}

private fun steps(start: String, directions: CharArray, network: Map<String, Node>, exit: (String) -> Boolean): Int {
    var cur = start
    var steps = 0
    while (true) {
        val dir = directions[steps.mod(directions.size)]
        cur = network[cur]!! walk dir
        steps++
        if (exit(cur)) break
    }
    return steps
}

private fun part1(directions: CharArray, network: Map<String, Node>) {
    println(steps("AAA", directions, network) { it == "ZZZ" })
}

fun part2(directions: CharArray, network: Map<String, Node>) {
    // starting to ending point is independent
    // find step count for each, then find the lowest common multiple of them all
    val steps = network.keys
        .filter { it.endsWith('A') }
        .map {
            steps(it, directions, network) { cur -> cur.endsWith('Z') }.toLong()
        }.fold(1L) { acc: Long, i: Long -> lcm(acc, i) }

    println(steps)
}

data class Node(val left: String, val right: String) {
    infix fun walk(dir: Char): String =
        when (dir) {
            'L' -> left
            'R' -> right
            else -> throw Error("invalid dir")
        }
}