package day04

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day04/input.txt")
        .readText()
        .trim()
        .split("\n\n", "\r\n\r\n")

    val numbers = lines[0].split(",").map { it.toInt() }
    val boards = lines
        .subList(1, lines.size)
        .map { BingoBoard.parse(it) }


    val elapsed1 = measureTimeMillis { part1(numbers, boards) }
    val elapsed2 = measureTimeMillis { part2(numbers, boards) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part2(numbers: List<Int>, boards: List<BingoBoard>) {
    for (i in 5..numbers.size) {
        val input = numbers.subList(0, i).toHashSet()

        if (boards.all { it.isBingo(input) != null }) {
            val previousInput = numbers.subList(0, i - 1).toHashSet()
            val lastWinner = boards.find { it.isBingo(previousInput) == null }
            println(lastWinner?.isBingo(input)?.times(numbers[i - 1]))
            break
        }
    }
}

fun part1(numbers: List<Int>, boards: List<BingoBoard>) {
    for (i in 5..numbers.size) {
        val input = numbers.subList(0, i).toHashSet()
        val winning = boards.firstNotNullOfOrNull { it.isBingo(input) }
        if (winning != null) {
            println(winning * numbers[i - 1])
            break
        }
    }
}

class BingoBoard(private val board: List<Collection<Int>>) {
    fun isBingo(input: Set<Int>): Int? {
        return if (board.any { input.containsAll(it) }) board
            .flatten()
            .toHashSet()
            .filter { !input.contains(it) }
            .sum()
        else null
    }

    companion object {
        private val RE = """\s+""".toRegex()

        fun parse(string: String): BingoBoard {
            val lines = string
                .trim()
                .split("\r\n", "\n")
                .map { line -> line.trim().split(RE).map { it.toInt() } }

            val rows = lines.map { it.toHashSet() }
            val cols = (0 until 5).map { col -> lines.map { it[col] } }.toHashSet()

            return BingoBoard(rows + cols)
        }
    }
}
