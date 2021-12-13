package day10

import java.io.File
import kotlin.system.measureTimeMillis

val OPEN2CLOSE = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
val CLOSE2OPEN = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
val OPENINGS = OPEN2CLOSE.keys
val ILLEGAL_CHAR_SCORE = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val COMPLETE_CHAR_SCORE = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

fun main() {
    val lines = File("src/main/kotlin/day10/input.txt")
        .readLines()
        .map { it.trim() }

    val elapsed1 = measureTimeMillis { part1(lines) }
    val elapsed2 = measureTimeMillis { part2(lines) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part2(lines: List<String>) {
    val incompleteLines = lines
        .filter { it.illegalChar() == null }
    val scores = incompleteLines
        .map { it.autocompleteScore() }
        .sortedDescending()

    println(scores[incompleteLines.size / 2])
}

fun part1(lines: List<String>) {
    val illegalChars = lines
        .mapNotNull { ILLEGAL_CHAR_SCORE[it.illegalChar()] }
        .sum()
    println(illegalChars)
}

private fun String.illegalChar(): Char? {
    var deque: ArrayDeque<Char> = ArrayDeque()
    for (char in this) {
        if (char in OPENINGS) {
            deque.addLast(char) // push
        } else {
            val opener = deque.removeLast()
            if (opener != CLOSE2OPEN[char]) {
                return char
            }
        }
    }
    return null
}

private fun String.autocompleteScore(): Long {
    var deque: ArrayDeque<Char> = ArrayDeque()
    for (char in this) {
        if (char in OPENINGS) {
            deque.addLast(char) // push
        } else {
            deque.removeLast()
        }
    }
    return deque
        .map { OPEN2CLOSE[it] }
        .reversed()
        .fold(0) { total, char -> total * 5 + COMPLETE_CHAR_SCORE.getOrDefault(char, 0) }
}
