package day05

import common.readParagraphs
import kotlin.system.measureTimeMillis

data class Move(val amount: Int, val fromIndex: Int, val toIndex: Int)

fun main() {
    val regex = Regex("""^move (\d+) from (\d) to (\d)$""")

    val texts = readParagraphs("day05/input.txt")
    val moves = texts[1]
        .lines()
        .map {
            val (amount, from, to) = regex.matchEntire(it)!!.destructured
            Move(amount.toInt(), from.toInt() - 1, to.toInt() - 1)
        }

    val containerText = texts[0].lines()
    val numCols = containerText.last().replace(" ", "").length

    val elapsed1 = measureTimeMillis { part1(containerText.toContainerStacks(numCols), moves) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(containerText.toContainerStacks(numCols), moves) }
    println("Part2: $elapsed2 ms")
}

fun part1(containers: List<ArrayDeque<String>>, moves: List<Move>) {
    for (move in moves) {
        repeat(move.amount) {
            containers[move.toIndex].addLast(containers[move.fromIndex].removeLast())
        }
    }
    val result = containers.joinToString("") { it.last() }
    println(result)
}

fun part2(containers: List<ArrayDeque<String>>, moves: List<Move>) {
    val temp = ArrayDeque<String>()
    for (move in moves) {
        repeat(move.amount) {
            temp.addFirst(containers[move.fromIndex].removeLast())
        }
        containers[move.toIndex].addAll(temp)
        temp.clear()
    }
    val result = containers.joinToString("") { it.last() }
    println(result)
}

private fun List<String>.toContainerStacks(size: Int): List<ArrayDeque<String>> {
    val containers: List<ArrayDeque<String>> = (0 until size).map { ArrayDeque() }

    val cleanRegex = Regex("""[\s\[\]]""")
    for (line in this.dropLast(1)) {
        line.chunked(4)
            .forEachIndexed { index, s ->
                val content = s.replace(cleanRegex, "")
                if (content.isNotBlank()) {
                    containers[index].addFirst(content)
                }
            }
    }
    return containers
}

