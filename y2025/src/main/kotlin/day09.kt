package dev.mickyloo

import common.readLines
import common.toNumbers
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.system.measureTimeMillis


fun main() {
    val points = readLines("inputs/day09.txt")
        .map { line ->
            val nums = line.toNumbers()
            nums[0].toLong() to nums[1].toLong()
        }

    val elapsed1 = measureTimeMillis { part1(points) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(points) }
    println("Part2: $elapsed2 ms")
}

private fun part1(points: List<Pair<Long,Long>>) {
    var rectMax = 0L
    for (i in points.indices) {
        for (j in (i + 1) until points.size) {
            val left = points[i]
            val right = points[j]
            val area = (abs(left.first - right.first)+1) * (abs(left.second - right.second)+1)
            if (area > rectMax) {
                rectMax = area
            }
        }
    }
    println(rectMax)
}

private fun part2(points: List<Pair<Long,Long>>) {

}