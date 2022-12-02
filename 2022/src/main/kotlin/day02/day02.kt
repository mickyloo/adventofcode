package day02

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val rounds = File("src/main/kotlin/day02/input.txt").readLines()

    val elapsed1 = measureTimeMillis { part1(rounds) }
    val elapsed2 = measureTimeMillis { part2(rounds) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

const val WIN = 6
const val DRAW = 3
const val LOSE = 0
const val ROCK = 1
const val PAPER = 2
const val SCISSORS = 3

fun part1(rounds: List<String>) {
    val results = mapOf(
        "A X" to DRAW + ROCK, // Rock Rock
        "A Y" to WIN + PAPER, // Rock Paper
        "A Z" to LOSE + SCISSORS, // Rock Scissor

        "B X" to LOSE + ROCK, // Paper Rock
        "B Y" to DRAW + PAPER, // Paper Paper
        "B Z" to WIN + SCISSORS, // Paper Scissor

        "C X" to WIN + ROCK, // Scissor Rock
        "C Y" to LOSE + PAPER, // Scissor Paper
        "C Z" to DRAW + SCISSORS, // Scissor Scissor
    )
    val score = rounds.sumOf { results.getOrDefault(it, 0) }
    println(score)
}


fun part2(rounds: List<String>) {
    val results = mapOf(
        // A is rock
        "A X" to LOSE + SCISSORS, // lose use scissor
        "A Y" to DRAW + ROCK, // draw use rock
        "A Z" to WIN + PAPER, // win use paper
        //B is paper
        "B X" to LOSE + ROCK, // lose use rock
        "B Y" to DRAW + PAPER, // draw use paper
        "B Z" to WIN + SCISSORS, // win use scissors
        //C is scissors
        "C X" to LOSE + PAPER, // lose use paper
        "C Y" to DRAW + SCISSORS, // draw use scissors
        "C Z" to WIN + ROCK, // win use rock
    )
    val score = rounds.sumOf { results.getOrDefault(it, 0) }
    println(score)
}