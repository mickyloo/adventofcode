package day01

import java.io.File


fun main() {

    // part 1
    println("part1 ")
    val count1 = File("src/main/kotlin/day01/input.txt")
        .readLines()
        .windowed(2, 1)
        .map { it[1].toInt() > it[0].toInt() }
        .count { it }
    println(count1)

    println("part2: ")
    val count2 = File("src/main/kotlin/day01/input.txt")
        .readLines()
        .asSequence()
        .windowed(3, 1)
        .map { it -> it.sumOf { it.toInt() } }
        .windowed(2, 1)
        .map { it[1] > it[0] }
        .count { it }

    println(count2)
}