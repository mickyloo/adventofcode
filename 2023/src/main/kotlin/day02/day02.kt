package day02

import common.readLines
import kotlin.system.measureTimeMillis

private val redRegex = """(\d+) red""".toRegex()
private val greenRegex = """(\d+) green""".toRegex()
private val blueRegex = """(\d+) blue""".toRegex()

fun main() {
    val re = Regex("""Game (\d+): (.*)""")
    val lines = readLines("day02/input.txt")
        .map {
            val (gameId, rolls) = re.matchEntire(it)!!.destructured
            gameId.toInt() to rolls.toCubes()
        }

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

private data class Cubes(val red: Int = 0, val blue: Int = 0, val green: Int = 0) {
    fun isValid(): Boolean = red <= 12 && green <= 13 && blue <= 14
    fun power(): Int = red * green * blue
}

private fun String.toCubes(): Cubes =
    Cubes(
        red = redRegex.findAll(this).maxOf { it.groupValues[1].toInt() },
        blue = blueRegex.findAll(this).maxOf { it.groupValues[1].toInt() },
        green = greenRegex.findAll(this).maxOf { it.groupValues[1].toInt() },
    )

private fun part1(lines: List<Pair<Int, Cubes>>) {
    println(lines
        .filter { it.second.isValid() }
        .sumOf { it.first }
    )
}

private fun part2(lines: List<Pair<Int, Cubes>>) {
    println(lines
        .sumOf { it.second.power() }
    )
}
