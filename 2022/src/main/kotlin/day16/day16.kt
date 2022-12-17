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

fun calcRoutes(volcano: Map<String, Room>): Map<Pair<String, String>, Int> {
    val valves = volcano.values.filter { it.valve > 0 }.map { it.name }
    val routes = mutableMapOf<Pair<String, String>, Int>()

    for (first in valves) {
        for (second in valves) {
            val pair = first to second
            if (!routes.containsKey(pair)) {
                routes[pair] = volcano.shortestRoute(first, second)
                routes[second to first] = routes[pair]!!
            }
        }
        routes["AA" to first] = volcano.shortestRoute("AA", first)
    }

    return routes.toMap()
}

private fun Map<String, Room>.shortestRoute(start: String, end: String): Int {
    val visited = mutableSetOf(start)
    val queue = mutableListOf(start to 0)

    while (queue.isNotEmpty()) {
        val (current, dist) = queue.removeLast()
        if (current == end) {
            return dist
        }
        for (neighbor in this[current]!!.tunnels) {
            if (neighbor !in visited) {
                queue.add(neighbor to dist + 1)
                visited.add(neighbor)
            }
        }
        queue.sortBy { -it.second }
    }

    return Int.MAX_VALUE
}

data class Room(val name: String, val valve: Int, val tunnels: List<String>)

data class Path(
    val room: String,
    val time: Int,
    val pressure: Int,
    val remaining: Set<String>,
)

fun part1(volcano: Map<String, Room>) {
    val routes = calcRoutes(volcano)
    val valves = volcano.values.filter { it.valve > 0 }.map { it.name }.toSet()

    var maxPressure = 0
    val stack = mutableListOf(Path("AA", 30, 0, valves))
    while(stack.isNotEmpty()) {
        val current = stack.removeLast()
        val room = volcano[current.room]!!
        var time = current.time

        val newPressure = if (room.valve > 0) {
            time--
            current.pressure + time * room.valve
        } else {
            current.pressure
        }

        if (newPressure > maxPressure) {
            maxPressure = newPressure
        }

        current.remaining.forEach {
            val newRemaining = current.remaining - it
            val newTime = time - routes[room.name to it]!!
            if (newTime > 0) {
                stack.add(Path(it, newTime, newPressure, newRemaining))
            }
        }
    }
    println(maxPressure)
}

fun part2(lines: List<String>) {
}
