package day19

import java.io.File
import kotlin.system.measureTimeMillis



fun main() {
    val re = Regex("""Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")

    val blueprints = File("src/main/kotlin/day19/input.txt")
        .readLines()
        .map {
            val matches = re.matchEntire(it)!!.destructured.toList().map { it.toInt() }
            Blueprint(matches[0], matches[1], matches[2], matches[3], matches[4], matches[5], matches[6])
        }

    blueprints.forEach { println(it) }

//    val elapsed1 = measureTimeMillis { part1(cubes) }
//    println("Part1: $elapsed1 ms")
//
//    val elapsed2 = measureTimeMillis { part2(cubes) }
//    println("Part2: $elapsed1 ms")
}

data class Resource(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int)
data class Workforce(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int)
data class State(val resource: Resource, val workforce: Workforce)

private val INITIAL = State(Resource(0, 0, 0, 0), Workforce(1, 0, 0, 0))


data class Blueprint(val num: Int, val oreOre: Int, val clayOre: Int, val obsidianOre: Int, val obsidianClay: Int, val geodeOre: Int, val geodeObsidian: Int) {
    fun run(minutes: Int = 24) : Int {


        return 0
    }
}