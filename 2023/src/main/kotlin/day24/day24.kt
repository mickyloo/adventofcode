package day24

import common.combinations
import common.println
import common.readLines
import common.toLongs
import java.math.BigDecimal
import kotlin.math.sign
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day24/input.txt")
        .map {
            val nums = it.toLongs()
            PointSlope(nums[0], nums[1], nums[2], nums[3].toInt(), nums[4].toInt(), nums[5].toInt())
        }

    measureTimeMillis {
        part1(lines)
    }.also { println("Part 1 $it ms") }

//    measureTimeMillis {
//        part2(grid)
//    }.also { println("Part 2 $it ms") }
}

fun part1(lines: List<PointSlope>) {
    lines
        .combinations()
        .count {
            val (p1, p2) = it
            collide(p1, p2)
        }.println()
}

data class PointSlope(val x: Long, val y: Long, val z: Long, val dx: Int, val dy: Int, val dz: Int) {
    // general form of line: ax + by + c = 0
    val a: Long = -1L * dy
    val b: Long = dx.toLong()
    val c: Long = (dy * x) - (dx * y)
}

fun collide(p1: PointSlope, p2: PointSlope, bounds: LongRange = (200000000000000..400000000000000)): Boolean {
    val (x1, y1) = p1.intersection(p2)
    if (x1.isInfinite() || y1.isInfinite())
        return false

    if (p1.dx.sign != (x1 - p1.x).sign.toInt()) {
        return false
    }
    if (p1.dy.sign != (y1 - p1.y).sign.toInt()) {
        return false
    }
    if (p2.dx.sign != (x1 - p2.x).sign.toInt()) {
        return false
    }
    if (p2.dy.sign != (y1 - p2.y).sign.toInt()) {
        return false
    }

    if ((x1 < bounds.first) || (x1 > bounds.last) || (y1 < bounds.first) || (y1 > bounds.last)) {
        return false
    }

    return true
}

fun PointSlope.intersection(other: PointSlope): Pair<Double, Double> {
    val denominator = (BigDecimal(this.a) * BigDecimal(other.b)) - (BigDecimal(other.a) * BigDecimal(this.b))
    if (denominator == BigDecimal.ZERO) {
        return Double.POSITIVE_INFINITY to Double.POSITIVE_INFINITY
    }
    val x = ((BigDecimal(this.b) * BigDecimal(other.c)) - (BigDecimal(other.b) * BigDecimal(this.c))) / denominator
    val y = ((BigDecimal(other.a) * BigDecimal(this.c)) - (BigDecimal(this.a) * BigDecimal(other.c))) / denominator
    return x.toDouble() to y.toDouble()
}