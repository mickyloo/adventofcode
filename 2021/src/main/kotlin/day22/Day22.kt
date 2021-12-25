package day22

import java.io.File
import kotlin.system.measureTimeMillis

val steps = File("src/main/kotlin/day22/input.txt")
    .readLines()
    .map {
        val (power, xyz) = it.split(" ")
        val(x, y, z) = xyz.split(",")
        Step(power == "on", x.toRange(), y.toRange(), z.toRange())
    }
fun main() {
    val elapsed1 = measureTimeMillis { part1(steps) }
    val elapsed2 = measureTimeMillis { part2() }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

private fun String.toRange(): IntRange {
    val parts = this.split("=")
    val (start, end) = parts[1].split("..").map { it.toInt() }
    return start..end
}

data class Step(val power: Boolean, val x: IntRange, val y: IntRange, val z: IntRange) {
    fun expand(): Set<Triple<Int, Int, Int>> {
        val result: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()
        x.forEach { x_ ->
            y.forEach { y_ ->
                z.forEach { z_ ->
                    result.add(Triple(x_,y_,z_))
                }
            }
        }
        return result
    }
}

fun part2() {

}

fun part1(steps: List<Step>) {
    // brute force
    val cubes = mutableSetOf<Triple<Int, Int, Int>>()
    steps
        .filter { (it.x.first >= -50) && (it.x.last <= 50)}
        .forEach {
        when(it.power) {
            true -> cubes.addAll(it.expand())
            false -> cubes.removeAll(it.expand())
        }
    }
    println(cubes.size)
}
