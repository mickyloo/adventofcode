package dev.mickyloo

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val map = readLines("inputs/day11.txt")
        .associate {
            val (key, outputs) = it.split(": ")
            outputs.split(" ")
            key to outputs.split(" ")
        }

    val elapsed1 = measureTimeMillis { part1(map) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(map) }
    println("Part2: $elapsed2 ms")
}

private fun part1(map: Map<String, List<String>>) {
    val work = map["you"]!!.map {
        listOf("you", it)
    }.toMutableList()

    var total = 0
    while (work.isNotEmpty()) {
        val currPath = work.removeFirst()
        val last = currPath.last()
        map[last]!!.forEach {
            when (it) {
                "out" -> total++
                else -> work.add(currPath + it)
            }
        }
    }
    println(total)
}

private fun part2(map: Map<String, List<String>>) {

    val cache = mutableMapOf<Key, Long>()

    fun paths(node: String, hasDac: Boolean, hasFFT: Boolean): Long {
        val key = Key(node, hasDac, hasFFT)
        if (cache.contains(key)) {
            return cache[key]!!
        }

        if (node == "out") {
            return if (hasDac && hasFFT) 1 else 0
        }

        val result = map[node]!!.sumOf {
            paths(it, hasDac || node == "dac", hasFFT || node == "fft")
        }

        cache[key] = result
        return result
    }

    val total = paths("svr", hasDac = false, hasFFT = false)
    println(total)

}

data class Key(val node: String, val hasDac: Boolean, val hasFFT: Boolean)