package day20

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val numbers = readLines("day20/input.txt")
        .map { it.toInt() }

    val elapsed1 = measureTimeMillis { solution(numbers) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { solution(numbers, key = 811589153L, iterations = 10) }
    println("Part2: $elapsed2 ms")
}

fun solution(numbers: List<Int>, key: Long = 1, iterations: Int = 1) {
    // keeps track of the index of the number after moving
    val numbers = numbers.map { it * key }
    val indexes = numbers.indices.toMutableList()

    repeat(iterations) {
        numbers.forEachIndexed { i, value ->
            val position = indexes.indexOf(i)
            val newPosition = (position + value).mod(numbers.size - 1)
            indexes.add(newPosition, indexes.removeAt(position))
        }
    }

    val mixed = indexes.map { numbers[it] }
    println(
        listOf(
            mixed[(mixed.indexOf(0) + 1000) % mixed.size],
            mixed[(mixed.indexOf(0) + 2000) % mixed.size],
            mixed[(mixed.indexOf(0) + 3000) % mixed.size]
        ).sum()
    )
}
