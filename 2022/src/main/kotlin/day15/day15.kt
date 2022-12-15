package day15


import java.io.File
import kotlin.math.abs
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

    //val elapsed1 = measureTimeMillis { part1(sensors) }
    val elapsed2 = measureTimeMillis { part2(sensors) }

    //println("Part1: $elapsed1 ms")
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
    //val max = 20
    val max = 4_000_000
    val col = (0..max).toSet()

    for(row in 0 .. max) {
        val covered = col - sensors.rowCoverage(row)
        if (covered.isNotEmpty()) {
            println("$covered and $row")
            break
        }
    }
}

private fun List<Sensor>.rowCoverage(row: Int) =
    this.mapNotNull {
        it.coverage(row)
    }.flatMap {
        it.toSet()
    }.toMutableSet()

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