package day14

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day14/input.txt").readLines()
    val template = lines[0]

    // converts CH -> B into CH -> [CB, BH]
    val replacements = lines.subList(2, lines.size)
        .associate {
            val (match, insert) = it.split(" -> ")
            match to (match[0] + insert + match[1]).windowed(2)
        }

    val elapsed1 = measureTimeMillis { run(template, replacements, iterations = 10) }
    val elapsed2 = measureTimeMillis { run(template, replacements, iterations = 40) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun run(template: String, replacements: Map<String, List<String>>, iterations: Int) {
    // use partialWindows=true to capture last character by itself, otherwise the final count will be off-by-one
    var segments: Map<String, Long> = template
        .windowed(2, partialWindows = true)
        .groupBy { it }
        .mapValues { it.value.count().toLong() }

    repeat(iterations) {
        segments = segments.map {
            val newSegments = replacements.getOrDefault(it.key, listOf(it.key)).associateWith { _ -> it.value }
            newSegments
        }.fold(mutableMapOf()) { acc, map ->
            map.entries.forEach { acc[it.key] = (acc.getOrDefault(it.key, 0) + it.value) }
            acc
        }
    }

    // only count first character per segment, otherwise will double count
    val counts = segments.map {
        it.key[0] to it.value
    }.fold(mutableMapOf<Char, Long>()) { acc, pair ->
        acc[pair.first] = acc.getOrDefault(pair.first, 0) + pair.second
        acc
    }

    println(counts.maxOf { it.value } - counts.minOf { it.value })
}