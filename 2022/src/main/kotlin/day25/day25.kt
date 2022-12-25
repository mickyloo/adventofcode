package day25

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val numbers = File("src/main/kotlin/day25/input.example.txt")
        .readLines()
        .map { line ->
            line.reversed().chunked(1).map {
                when(it) {
                    "=" -> -2
                    "-" -> -1
                    else -> it.toInt()
                }
            }
        }

    val elapsed1 = measureTimeMillis { part1(numbers) }
    println("Part1: $elapsed1 ms")

//    val elapsed2 = measureTimeMillis { part2(elves) }
//    println("Part2: $elapsed2 ms")
}

val base5 = mapOf( 0 to 0, 1 to 1, 2 to 2, 3 to -2, 4 to -1)
fun part1(numbers: List<List<Int>>) {
    val maxLen = numbers.maxOf { it.size }
    var sum = (0 until maxLen)
        .map { index ->
            numbers.sumOf { it.getOrElse(index) { 0 } }
        }.toMutableList()
    sum.add(sum.size, 0)
    println(sum)

    var carries = (0..sum.size).map { 0 }.toMutableList()

    for ((i, value) in sum.dropLast(1).withIndex()) {
        sum[i] = base5[(value + carries[i]).mod(5)]!!
        carries[i+1] = value / 5
    }

    println(sum)
//
//    println(10 * 5^0 + 11*5^1 + (-2 * 5^2) + (4* 5^3) + (2* 5^4) + (1* 5^5))
//
//    1  2  4 -2 11 10
//    2  =  -  1  =  0
//
//    2 -2 -1  1  -2 0
}