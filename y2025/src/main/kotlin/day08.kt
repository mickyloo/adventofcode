package dev.mickyloo

import common.readLines
import common.toNumbers
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

fun main() {
    val points = readLines("inputs/day08.txt")
        .map { line ->
            val nums = line.toNumbers()
            Point3(nums[0].toLong(), nums[1].toLong(), nums[2].toLong())
        }

    var pairs = mutableListOf<Pair<Point3, Point3>>()
    for (i in points.indices) {
        for (j in (i + 1) until points.size) {
            pairs.add(points[i] to points[j])
        }
    }

    pairs.sortBy { it.first.distTo(it.second) }

    val elapsed1 = measureTimeMillis { part1(pairs.toList()) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(pairs.toList(), goal = points.size) }
    println("Part2: $elapsed2 ms")
}

private fun part1(pairs: List<Pair<Point3, Point3>>) {
    val shortestPairs = pairs.take(1000)

    var circuits = mutableListOf<Set<Point3>>()
    for ((p1, p2) in shortestPairs) {
        findAndMerge(circuits, p1, p2)
    }

    circuits.sortByDescending { it.size }
    val result = circuits.take(3).fold(1L) { acc, next -> acc * next.size }
    println(result)
}

private fun part2(pairs: List<Pair<Point3, Point3>>, goal: Int) {
    var circuits = mutableListOf<Set<Point3>>()

    for ((p1, p2) in pairs) {
        findAndMerge(circuits, p1, p2)

        if (circuits.size == 1 && circuits[0].size == goal) {
            println(p1.x * p2.x)
            break
        }
    }
}

private fun findAndMerge(circuits: MutableList<Set<Point3>>, p1: Point3, p2: Point3) {
    val circuit1 = circuits.find { it.contains(p1) }
    val circuit2 = circuits.find { it.contains(p2) }

    if (circuit1 == null && circuit2 == null) {
        circuits.add(setOf(p1, p2))
    } else if (circuit1 != null && circuit2 == null) {
        circuits.remove(circuit1)
        circuits.add(circuit1 + p2)
    } else if (circuit1 == null && circuit2 != null) {
        circuits.remove(circuit2)
        circuits.add(circuit2 + p1)
    } else if (circuit1 != null && circuit2 != null && circuit1 != circuit2) { // merge
        circuits.remove(circuit1)
        circuits.remove(circuit2)
        circuits.add(circuit1 + circuit2 + p1 + p2)
    }
}


data class Point3(val x: Long, val y: Long, val z: Long) {
    fun distTo(other: Point3): Double {
        val dx = (this.x - other.x)
        val dy = (this.y - other.y)
        val dz = (this.z - other.z)
        return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
    }
}
