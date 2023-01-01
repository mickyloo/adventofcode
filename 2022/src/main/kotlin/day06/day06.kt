package day06

import common.readText
import kotlin.system.measureTimeMillis

fun main() {
    val signal = readText("day06/input.txt")

    val elapsed1 = measureTimeMillis { solution(signal, distinct = 4) }
    val elapsed2 = measureTimeMillis { solution(signal, distinct = 14) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

fun solution(signal: String, distinct: Int) {
    for (group in signal.windowed(distinct).withIndex()) {
        if (group.value.toSet().size == distinct) {
            println(group.index + distinct)
            break
        }
    }
}
