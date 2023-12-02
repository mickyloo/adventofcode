package day02

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val re = Regex("""Game (\d+): (.*)""")
    val lines = readLines("day02/input.txt")
        .map {
            val (gameId, rolls) = re.matchEntire(it)!!.destructured
            val listCubes = rolls.split("; ")
                .map { roll ->
                    roll.trim().toCubes()
                }
            gameId.toInt() to listCubes
        }

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

data class Cubes(val red: Int = 0, val blue: Int = 0, val green: Int = 0) {
    fun isValid(): Boolean = red <= 12 && green <= 13 && blue <= 14
}

private fun String.toCubes(): Cubes {
    val cubes: Map<String, Int> = this.split(", ")
        .map {
            val (count, color) = it.split(" ")
            color.trim() to count.trim().toInt()
        }.associateBy({ entry -> entry.first }, { entry -> entry.second })

    return Cubes(
        red = cubes.getOrDefault("red", 0),
        blue = cubes.getOrDefault("blue", 0),
        green = cubes.getOrDefault("green", 0)
    )
}

private fun List<Cubes>.power(): Cubes =
    Cubes(
        red = this.maxOf { it.red },
        blue = this.maxOf { it.blue },
        green = this.maxOf { it.green }
    )

fun part1(lines: List<Pair<Int, List<Cubes>>>) {
    val result = lines.sumOf {
        val valid = it.second.all { cube -> cube.isValid() }
        if (valid) it.first else 0
    }
    println(result)
}

fun part2(lines: List<Pair<Int, List<Cubes>>>) {
    val result = lines.sumOf {
        val power = it.second.power()
        power.red * power.green * power.blue
    }
    println(result)
}
