package day17

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val parts = File("src/main/kotlin/day17/input.txt")
        .readText().trim()
        .replace("target area: x=", "")
        .replace(" y=", "")
        .split(",")
        .map { range ->
            range.split("..").map { it.toInt() }
        }
    val target = Target((parts[0][0])..(parts[0][1]), (parts[1][0])..(parts[1][1]))

    val elapsed1 = measureTimeMillis { part1(target) }
    val elapsed2 = measureTimeMillis { part2(target) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

data class Target(val xRange: IntRange, val yRange: IntRange) {
    private fun inRange(x: Int, y: Int) = (x in xRange) and (y in yRange)
    private fun overShoot(x: Int, y: Int) = ((x > xRange.last) or (y < yRange.first))

    fun launch(vx: Int, vy: Int): Boolean {
        var xVelocity = vx
        var yVelocity = vy

        var xPos = 0
        var yPos = 0
        while (true) {
            xPos += xVelocity
            yPos += yVelocity
            if (overShoot(xPos, yPos)) {
                return false
            }
            if (inRange(xPos, yPos)) {
                return true
            }

            xVelocity += when {
                xVelocity > 0 -> -1
                xVelocity < 0 -> 1
                else -> 0
            }
            yVelocity -= 1
        }
    }
}

fun part2(target: Target) {
    var hits = 0
    for (x in 0..abs(target.xRange.last)) {
        for (y in -abs(target.yRange.first)..abs(target.yRange.first)) {
            if (target.launch(x, y)) {
                hits++
            }
        }
    }
    println(hits)
}

fun part1(target: Target) {
    val maxYVelocity = abs(target.yRange.first) - 1
    var yMax = 0
    for (i in 1..maxYVelocity) {
        var yPos = 0
        var yVel = i
        while (yPos >= 0) {
            yPos += yVel
            yVel--
            if (yPos > yMax) {
                yMax = yPos
            }

        }
    }
    println(yMax)
}
