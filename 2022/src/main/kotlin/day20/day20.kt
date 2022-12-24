package day20

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val file = File("src/main/kotlin/day20/input.txt")
        .readLines()
        .map {it.toInt() }


    val foo = mutableMapOf<String, Lazy<Int>>()

    foo["root"] = lazy {
        println("calling root")
        foo["bar"]!!.value + 1
    }
    foo["bar"] = lazy {
        println("calling bar")
        10
    }

    println(foo["root"]!!.value)
    println(foo["root"]!!.value)

    val elapsed1 = measureTimeMillis { part1(file) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(file) }
    println("Part2: $elapsed1 ms")
}

fun part1(file: List<Int>) {

}

fun part2(file: List<Int>) {

}
