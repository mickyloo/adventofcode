package day07

import java.io.File
import kotlin.math.abs

fun main() {
    val crabs = File("src/main/kotlin/day07/input.txt")
        .readText()
        .trim()
        .split(",")
        .map { it.toInt() }

    val min = crabs.minOf { it }
    val max = crabs.maxOf { it }

    // part 1
    val minFuelUsed = (min..max).minOf { position ->
        crabs.sumOf { abs(it - position) }
    }
    println(minFuelUsed)

    // part2
    val minFuelUsedComplex = (min..max).minOf { position ->
        crabs.sumOf {
            val distance = abs(it - position)
            (distance * (distance + 1) / 2)
        }
    }
    println(minFuelUsedComplex)
}

