package dev.mickyloo

import common.readLines
import common.transpose
import kotlin.system.measureTimeMillis

fun main() {
    val temp = readLines("inputs/day06.txt")
    val maxLen = temp.maxOf { it.length }
    val lines = temp.map { it.padEnd(maxLen + 1).toCharArray() }
    val indexes = (
            lines.last()
                .withIndex()
                .filter { it.value != ' ' }
                .map { it.index } + listOf(maxLen)
            )
        .windowed(2)

    val elapsed1 = measureTimeMillis { part1(lines, indexes) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines, indexes) }
    println("Part2: $elapsed2 ms")
}

private fun part1(lines: List<CharArray>, indexes: List<List<Int>>) {
    val numbers = indexes.map { (left, right) ->
        lines.dropLast(1).map {
            it.slice(left until right - 1).joinToString("")
        } + lines.last()[left].toString()
    }

    println(calculate(numbers))
}

private fun part2(lines: List<CharArray>, indexes: List<List<Int>>) {
    val numbers = indexes.map { (left, right) ->
        lines.dropLast(1).map {
            it.slice(left until right - 1).joinToString("")
        }.transpose() + lines.last()[left].toString()
    }

    println(calculate(numbers))
}

private fun calculate(lines: List<List<String>>): Long =
    lines.sumOf { line ->
        val nums = line.dropLast(1).map { it.trim().toLong() }
        val answer = if (line.last() == "*") {
            nums.fold(1L) { acc, next -> acc * next }
        } else {
            nums.fold(0L) { acc, next -> acc + next }
        }
        answer
    }