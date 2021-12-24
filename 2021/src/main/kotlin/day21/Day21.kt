package day21

import kotlin.system.measureTimeMillis

fun main() {
    val elapsed1 = measureTimeMillis { part1(7,4) }
    val elapsed2 = measureTimeMillis { part2(4, 8) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

class DeterministicDie(val maxVal: Int = 100) {
    var rolls = 0
    var result: Int = 0

    fun roll(count: Int = 3): Int {
        rolls += count
        return (0 until count).sumOf {
            result = if ((result++) >= maxVal) 1 else result
            result
        }
    }
}

val quantumDie = mapOf(
    3 to 1,  // 1, 1, 1
    4 to 3,
    5 to 6,
    6 to 7,
    7 to 6,
    8 to 3,
    9 to 1  // 3, 3, 3
)

fun part2(player1: Int, player2: Int) {
    quantumRolls(player1)
    quantumRolls(player2)
}

private fun quantumRolls(start: Int) {
    val scores = mutableMapOf(0 to 1L)
    val positions = mutableMapOf(start to 1L)

    println("Start: $start")
    quantumDie.forEach { (roll, universes) ->
        val newPositions = positions.map {
            val newPos = (it.key + roll -1).mod(10) + 1
            val newUniverses = universes.toBigInteger().pow(it.value.toInt())
            newPos to newUniverses.toLong()
        }
        println(newPositions)
    }

}

fun part1(player1: Int, player2: Int) {
    val die = DeterministicDie()
    var positions = mutableListOf(player1, player2)
    var scores = mutableListOf(0, 0)

    var player = 0
    while (scores.all { it < 1000}) {
        val roll = die.roll()
        val pos = (positions[player] + roll - 1).mod(10) + 1
        positions[player] = pos
        scores[player] += pos

        player = if (player==1) 0 else 1
    }

    println(scores.minOf { it } * die.rolls)
}
