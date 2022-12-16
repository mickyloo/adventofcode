package day16

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val re = Regex("""Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? (.*)""")

    val volcano = File("src/main/kotlin/day16/input.txt")
        .readLines().associate {
            val matches = re.matchEntire(it)!!
            val (name, valve, tunnels) = matches.destructured
            name to Room(name, valve.toInt(), tunnels.split(", "))
        }

    val elapsed1 = measureTimeMillis { part1(volcano) }
//    val elapsed2 = measureTimeMillis { part2(lines) }
//
    println("Part1: $elapsed1 ms")
//    println("Part2: $elapsed2 ms")
}

data class Room(val name: String, val valve: Int, val tunnels: List<String>) {

}

fun part1(volcano: Map<String, Room>) {

    val stack = mutableListOf(Triple(volcano["AA"]!!, 30, 0))

}

fun part2(lines: List<String>) {
}
