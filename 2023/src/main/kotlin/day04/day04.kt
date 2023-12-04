package day04

import common.readLines
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {
    val re = """Card\s+(\d+): (.*) \| (.*)""".toRegex()
    val whitespace = """\s+""".toRegex()

    val cards = readLines("day04/input.txt")
        .map { line ->
            val (num, winning, values) = re.matchEntire(line)!!.destructured
            Card(
                num = num.toInt(),
                winning = winning.trim().split(whitespace).map { it.trim().toInt() }.toSet(),
                values = values.trim().split(whitespace).map { it.trim().toInt() }.toSet(),
            )
        }

    val elapsed1 = measureTimeMillis { part1(cards) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(cards) }
    println("Part2: $elapsed2 ms")
}

private fun part1(cards: List<Card>) {
    val results = cards.sumOf { it.score() }
    println(results)
}

private fun part2(cards: List<Card>) {
    val totals = IntArray(cards.size) { 1 }
    for ((index, card) in cards.withIndex()) {
        (1..card.matches()).forEach {
            val newIndex = index + it
            totals[newIndex] += totals[index]
        }
    }
    println(totals.sum())
}

private data class Card(val num: Int, val winning: Set<Int>, val values: Set<Int>) {
    fun score(): Int =
        when (val winning = matches()) {
            0 -> 0
            else -> 2.0.pow(winning - 1).toInt()
        }

    fun matches(): Int = values.intersect(winning).size
}