package day17

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {

    val wind = File("src/main/kotlin/day17/input.example.txt")
        .readText().trim().toCharArray()

    val elapsed1 = measureTimeMillis { part1(wind) }
    val elapsed2 = measureTimeMillis { part2(wind) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

data class Point(val x: Int, val y: Int)

enum class Move(pair: Pair<Int, Int>) {
    LEFT(-1 to 0),
    RIGHT(1 to 0),
    DOWN(0 to -1),
}

sealed interface Block {
    val cells: Set<Point>
    fun outOfBounds(): Boolean {
        return cells.any { it.x < 0 || it.x > 6 || it.y < 1 }
    }
}

data class Bar(override val cells: Set<Point>): Block
data class Cross(override val cells: Set<Point>): Block
data class L(override val cells: Set<Point>): Block
data class Long(override val cells: Set<Point>): Block
data class Square(override val cells: Set<Point>): Block

fun makeBlock(iteration: Int, x: Int, y: Int): Block =
    when((iteration % 5)) {
        0 -> Bar(setOf(Point(2 + x, y), Point(3 + x, y), Point(4 + x, y), Point(5 + x, y)))
        1 -> Cross(setOf(Point(3 + x, y), Point(2 + x, y+1), Point(3 + x, y+1), Point(4 + x, y+1), Point(3 + x, y+2)))
        2 -> L(setOf(Point(2+x, y), Point(3+x, y), Point(4+x, y), Point(4+x, y+1), Point(4+x, y+2)))
        3 -> Long(setOf(Point(2+x, y), Point(2+x, y+1), Point(2+x, y+2), Point(2+x, y+3)))
        4 -> Square(setOf(Point(2+x, y), Point(3+x, y), Point(2+x, y+1), Point(3+x, y+1)))
        else -> throw Exception("error")
    }

fun part1(wind: CharArray) {
    val blocks = mutableSetOf<Point>()
    var height = 0
    var windIndex = 0
    repeat(100) { round ->
        var xOffset = 0
        height += 4
        var current = makeBlock(round, xOffset, height)
        while(true) {
            // move by wind
            val windDir = if (wind[windIndex % wind.size] == '<') -1 else 1
            windIndex++
            val newBlock = makeBlock(round, xOffset + windDir, height)
            if (!(newBlock.outOfBounds() || (newBlock.cells intersect blocks).isNotEmpty())) {
                current = newBlock
                xOffset += windDir
            }
            // move down
            val blockDown = makeBlock(round, xOffset, height - 1)
            // add to settled blocks
            if ((blockDown.cells intersect blocks).isNotEmpty() || blockDown.outOfBounds()) {
                blocks.addAll(current.cells)
                height = blocks.maxOf { it.y }
                break
            } else {
                current = blockDown
                height--
            }
        }
    }
    println(blocks.maxOf { it.y })

    blocks.print()
}

fun part2(wind: CharArray) {
}


fun MutableSet<Point>.print() {
    for(y in 200 downTo 1) {
        print("|")
        for(x in 0 .. 6) {
            if (Point(x,y) in this) {
                print("#")
            } else {
                print(".")
            }
        }
        println("|")
    }
}
