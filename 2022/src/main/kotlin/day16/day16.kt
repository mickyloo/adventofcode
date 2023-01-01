package day16

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val re = Regex("""Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? (.*)""")

    val volcano = readLines("day16/input.txt")
        .associate {
            val matches = re.matchEntire(it)!!
            val (name, valve, tunnels) = matches.destructured
            name to Room(name, valve.toInt(), tunnels.split(", "))
        }

    val elapsed1 = measureTimeMillis { part1(volcano) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(volcano) }
    println("Part2: $elapsed2 ms")
}


fun calcRoutes(volcano: Map<String, Room>): Map<Pair<Room, Room>, Int> {
    val valves = volcano.values.filter { it.valve > 0 }.map { it.name }
    val routes = mutableMapOf<Pair<Room, Room>, Int>()

    val beginning = volcano["AA"]!!
    for (first in valves) {
        val firstRoom = volcano[first]!!
        for (second in valves) {
            val secondRoom = volcano[second]!!
            val pair = firstRoom to secondRoom
            if (!routes.containsKey(pair)) {
                routes[pair] = volcano.shortestRoute(first, second)
                routes[secondRoom to firstRoom] = routes[pair]!!
            }
        }
        routes[beginning to firstRoom] = volcano.shortestRoute("AA", first)
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

data class Room(val name: String, val valve: Int, val tunnels: List<String>) {
    override fun toString(): String = name
}

data class Path(
    val room: Room,
    val time: Int,
    val pressure: Int,
    val remaining: Set<Room>,
)

fun part1(volcano: Map<String, Room>) {
    val routes = calcRoutes(volcano)
    val valves = volcano.values.filter { it.valve > 0 }.toSet()

    var maxPressure = 0
    val stack = mutableListOf(Path(volcano["AA"]!!, 30, 0, valves))
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        if (current.pressure > maxPressure) {
            maxPressure = current.pressure
        }

        current.remaining.forEach {
            val newRemaining = current.remaining - it
            val newTime = current.time - (routes[current.room to it]!! + 1)
            if (newTime >= 0) {
                val newPressure = current.pressure + (newTime * it.valve)
                stack.add(Path(it, newTime, newPressure, newRemaining))
            }
        }
    }
    println(maxPressure)
}

fun part2(volcano: Map<String, Room>) {
    val routes = calcRoutes(volcano)
    val valves = volcano.values.filter { it.valve > 0 }.toSet()

    var maxPressures = mutableMapOf<Set<Room>, Int>()
    val stack = mutableListOf(Path(volcano["AA"]!!, 26, 0, valves))

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()

        current.remaining.forEach {
            val newRemaining = current.remaining - it
            val newTime = current.time - (routes[current.room to it]!! + 1)
            if (newTime >= 0) {
                val newPressure = current.pressure + (newTime * it.valve)
                stack.add(Path(it, newTime, newPressure, newRemaining))
            } else {
                // no more moves remaining, record max pressure
                val visited = valves - newRemaining
                val maxPressure = maxPressures.getOrDefault(visited, 0)
                if (current.pressure > maxPressure) {
                    maxPressures[visited] = current.pressure
                }
            }
        }
    }

    var maxMeAndElephant = 0
    for ((meVisited, mePressure) in maxPressures) {
        for ((elephantVisited, elephantPressure) in maxPressures) {
            val disjointVisits = (meVisited intersect elephantVisited).isEmpty()
            val totalPressure = mePressure + elephantPressure
            if (disjointVisits && totalPressure > maxMeAndElephant) {
                maxMeAndElephant = totalPressure
            }
        }
    }

    println(maxMeAndElephant)
}
