package day16

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val re = Regex("""Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? (.*)""")

    val volcano = File("src/main/kotlin/day16/input.example.txt")
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

data class Room(val name: String, val valve: Int, val tunnels: List<String>)

data class Path(
    val currentRoom: String,
    val time: Int,
    val pressure: Int,
    val opened: Set<String>
)

fun part1(volcano: Map<String, Room>) {
    val stack = mutableListOf(Path("AA", 30, 0, setOf()))
    var maxPressure = 0

    while(stack.isNotEmpty()) {
        val current = stack.removeLast()
        if (current.time < 1 && current.pressure > maxPressure) {
            maxPressure = current.pressure
        }
        // decide if should open pressure on room
        val room = volcano[current.currentRoom]!!
        if (room.valve > 0 && current.currentRoom !in current.opened) {
            //spend a turn opening valve and move to others

            room.tunnels.forEach {
                val remaining = current.time - 2
                stack.add(Path(it, remaining, current.pressure + remaining * room.valve, current.opened + room.name))
            }
        }

        // move to others without opening
        room.tunnels.forEach {
            stack.add(Path(it, current.time - 1, current.pressure, current.opened))
        }
    }

    println(maxPressure)
}

fun part2(lines: List<String>) {
}
