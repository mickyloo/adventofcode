package day13

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day13/input.txt")
        .readText()
        .split("\r\n\r\n", "\n\n")

    val dots = lines[0].split("\r\n", "\n").map { it.toDot() }
    val splits = lines[1].split("\r\n", "\n")

    val elapsed1 = measureTimeMillis { part1(dots, splits[0].toFold()) }
    val elapsed2 = measureTimeMillis { part2(dots, splits.map { it.toFold() }) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part2(dots: List<Dot>, folds: List<Fold>) {
    var result: Collection<Dot> = dots
    for (fold in folds) {
        result = result.fold(fold)
    }

    for (y in 0..5) {
        for (x in 0..40) {
            if (Dot(x, y) in result) {
                print("█ ")
            } else {
                print("  ")
            }
        }
        println()
    }
}

fun part1(dots: List<Dot>, fold: Fold) {
    val result = dots.fold(fold)
    println(result.size)
}

data class Fold(val axis: String, val index: Int)
data class Dot(val x: Int, val y: Int)

private fun String.toDot(): Dot {
    val (x, y) = this.split(",").map { it.toInt() }
    return Dot(x, y)
}

private fun String.toFold(): Fold {
    val parts = this.split(" ")
    val (axis, index) = parts[2].split("=")
    return Fold(axis, index.toInt())
}

private fun Collection<Dot>.fold(fold: Fold): Collection<Dot> =
    this.mapNotNull {
        when {
            (fold.axis == "x") && (it.x > fold.index) -> Dot(2 * fold.index - it.x, it.y)
            (fold.axis == "y") && (it.y > fold.index) -> Dot(it.x, 2 * fold.index - it.y)
            else -> it
        }
    }.toSet()
