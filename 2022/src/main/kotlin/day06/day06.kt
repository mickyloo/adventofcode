package day06

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val signal = File("src/main/kotlin/day06/input.txt").readText().trim()

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
