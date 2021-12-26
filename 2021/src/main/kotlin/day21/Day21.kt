package day21

import kotlin.system.measureTimeMillis

fun main() {
    val elapsed1 = measureTimeMillis { part1(7, 4) }
    val elapsed2 = measureTimeMillis { part2(7, 4) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

class DeterministicDie(private val maxVal: Int = 100) {
    var rolls = 0
    private var result: Int = 0

    fun roll(count: Int = 3): Int {
        rolls += count
        return (0 until count).sumOf {
            result = if ((result++) >= maxVal) 1 else result
            result
        }
    }
}

val quantumDie = mapOf(
    9 to 1,  // 3, 3, 3
    8 to 3,
    7 to 6,
    6 to 7,
    5 to 6,
    4 to 3,
    3 to 1,  // 1, 1, 1
)

const val PLAYER1 = 0
const val PLAYER2 = 1

data class Player(val pos: Int, val score: Int)
data class State(val p1: Player, val p2: Player, val nextTurn: Int)

fun part2(player1: Int, player2: Int) {

    val gameStates = mutableMapOf(
        State(Player(player1, 0), Player(player2, 0), PLAYER1) to 1L
    )

    val wins = Array(2) { 0L }

    while (gameStates.isNotEmpty()) {
        val cur = gameStates.firstNotNullOf { it }
        val curPlayer = if (cur.key.nextTurn == PLAYER1) cur.key.p1 else cur.key.p2

        // create new states with rolls
        quantumDie.forEach { (roll, universes) ->
            val newPos = (curPlayer.pos + roll - 1).mod(10) + 1
            val newScore = curPlayer.score + newPos
            val newState = if (cur.key.nextTurn == PLAYER1) {
                State(Player(newPos, newScore), cur.key.p2, nextTurn = PLAYER2)
            } else {
                State(cur.key.p1, Player(newPos, newScore), nextTurn = PLAYER1)
            }

            val newUniverses = universes * cur.value

            if (newState.p1.score >= 21) {
                wins[0] += newUniverses
            } else if (newState.p2.score >= 21) {
                wins[1] += newUniverses
            } else {
                gameStates[newState] = gameStates.getOrDefault(newState, 0L) + newUniverses
            }
            gameStates.remove(cur.key)
        }

    }

    println(maxOf(wins[0], wins[1]))
}

fun part1(player1: Int, player2: Int) {
    val die = DeterministicDie()
    var positions = mutableListOf(player1, player2)
    var scores = mutableListOf(0, 0)

    var player = 0
    while (scores.all { it < 1000 }) {
        val roll = die.roll()
        val pos = (positions[player] + roll - 1).mod(10) + 1
        positions[player] = pos
        scores[player] += pos

        player = if (player == 1) 0 else 1
    }

    println(scores.minOf { it } * die.rolls)
}