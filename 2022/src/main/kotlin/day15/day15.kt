package day15

import day04.anyOverlap
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val re = Regex("""Sensor at x=([\-\d]+), y=([\-\d]+): closest beacon is at x=([\-\d]+), y=([\-\d]+)""")

    val sensors = File("src/main/kotlin/day15/input.txt")
        .readLines()
        .map {
            val matches = re.matchEntire(it)!!
            val (sensorX, sensorY, beaconX, beaconY) = matches.destructured
            Sensor(Point(sensorX.toInt(), sensorY.toInt()), Point(beaconX.toInt(), beaconY.toInt()))
        }

    val elapsed1 = measureTimeMillis { part1(sensors) }
    val elapsed2 = measureTimeMillis { part2(sensors) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

fun part1(sensors: List<Sensor>) {
    val y = 2000000
    val covered = sensors.rowCoverage(y)

    sensors.filter {
        it.beacon.y == y
    }.forEach {
        covered.remove(it.beacon.x)
    }

    println(covered.size)
}

fun part2(sensors: List<Sensor>) {
    val max = 4_000_000
    val beacon = (0..max)
        .map {
            it to sensors.coverageRanges(it)
        }.first {
            it.second.size > 1
        }

    val x = beacon.second[0].last + 1
    println(x * max.toLong() + beacon.first)
}

private fun List<Sensor>.rowCoverage(row: Int) =
    this.coverageRanges(row)
        .flatMap {
            it.toSet()
        }.toMutableSet()

private fun List<Sensor>.coverageRanges(row: Int) = this.mapNotNull { it.coverage(row) }.merge()

private fun List<IntRange>.merge(): MutableList<IntRange> {
    val ranges = mutableListOf<IntRange>()
    val sorted = this.sortedBy { it.first }

    var merged = sorted.first()
    for (r in sorted.drop(1)) {
        merged = if (merged anyOverlap r) {
            min(r.first, merged.first)..max(r.last, merged.last)
        } else {
            ranges.add(merged)
            r
        }
    }
    ranges.add(merged)
    return ranges
}

fun Pair<Point, Point>.distance(): Int =
    abs(this.first.x - this.second.y) + abs(this.first.y - this.second.y)

data class Point(val x: Int, val y: Int)

data class Sensor(val sensor: Point, val beacon: Point) {
    private val distance: Int
        get() = abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y)

    fun coverage(row: Int): IntRange? {
        val distToRow = abs(sensor.y - row)
        if (distToRow > distance) {
            return null
        }
        val x = (distance - distToRow)
        return (sensor.x - x)..(sensor.x + x)
    }
}