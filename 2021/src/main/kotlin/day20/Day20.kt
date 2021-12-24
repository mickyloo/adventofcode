package day20

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day20/input.example.txt")
        .readLines()

    val algo = lines[0]
    val image = lines.subList(2, lines.size)
    println(image.getOrNull(-1)?.getOrNull(-1))
    val elapsed1 = measureTimeMillis { part1() }
    val elapsed2 = measureTimeMillis { part2() }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part2() {

}

fun part1() {

}
