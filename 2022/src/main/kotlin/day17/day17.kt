package day17

import common.Point
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {

    val wind = File("src/main/kotlin/day17/input.txt")
        .readText().trim().toCharArray()

    val elapsed1 = measureTimeMillis { part1(wind) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(wind) }
    println("Part2: $elapsed2 ms")
}

sealed interface Block {
    val cells: Set<Point>
    fun outOfBounds(): Boolean {
        return cells.any { it.x < 0 || it.x > 6 || it.y < 1 }
    }
}

data class Bar(override val cells: Set<Point>) : Block
data class Cross(override val cells: Set<Point>) : Block
data class L(override val cells: Set<Point>) : Block
data class Long(override val cells: Set<Point>) : Block
data class Square(override val cells: Set<Point>) : Block

fun makeBlock(iteration: Int, x: Int, y: Int): Block =
    when ((iteration % 5)) {
        0 -> Bar(setOf(Point(2 + x, y), Point(3 + x, y), Point(4 + x, y), Point(5 + x, y)))
        1 -> Cross(
            setOf(
                Point(3 + x, y),
                Point(2 + x, y + 1),
                Point(3 + x, y + 1),
                Point(4 + x, y + 1),
                Point(3 + x, y + 2)
            )
        )

        2 -> L(setOf(Point(2 + x, y), Point(3 + x, y), Point(4 + x, y), Point(4 + x, y + 1), Point(4 + x, y + 2)))
        3 -> Long(setOf(Point(2 + x, y), Point(2 + x, y + 1), Point(2 + x, y + 2), Point(2 + x, y + 3)))
        4 -> Square(setOf(Point(2 + x, y), Point(3 + x, y), Point(2 + x, y + 1), Point(3 + x, y + 1)))
        else -> throw Exception("error")
    }

class FallingRocks(private val wind: CharArray) {
    val blocks = mutableSetOf<Point>()
    var height = 0
    private var windIndex = 0
    var round = 0

    fun next() {
        var xOffset = 0
        var blockY = height + 4
        var current = makeBlock(round, xOffset, blockY)
        while (true) {
            // move by wind
            val windDir = if (wind[windIndex % wind.size] == '<') -1 else 1
            windIndex++
            val newBlock = makeBlock(round, xOffset + windDir, blockY)
            if (!(newBlock.outOfBounds() || (newBlock.cells intersect blocks).isNotEmpty())) {
                current = newBlock
                xOffset += windDir
            }

            // move down
            blockY -= 1
            val blockDown = makeBlock(round, xOffset, blockY)
            // add to settled blocks
            if ((blockDown.cells intersect blocks).isNotEmpty() || blockDown.outOfBounds()) {
                blocks.addAll(current.cells)
                height = maxOf(height, current.cells.maxOf { it.y })
                break
            } else {
                current = blockDown
            }
        }
        round++
        return
    }

    fun pattern(): String {
        val pattern = StringBuilder()
        for (y in height downTo height - 50) {
            for (x in 0..6) {
                pattern.append(if (Point(x, y) in blocks) "#" else ".")
            }
            pattern.appendLine()
        }
        return pattern.toString()
    }
}

fun part1(wind: CharArray) {
    val game = FallingRocks(wind)
    repeat(2022) {
        game.next()
    }
    println(game.height)
}


fun part2(wind: CharArray) {
    val game = FallingRocks(wind)

    // run the game for a while to get repeats
    val rockOffset = 100_000
    repeat(rockOffset) { game.next() }

    val patternToFind = game.pattern()
    val heightOffset = game.height

    do {
        game.next()
    } while (game.pattern() != patternToFind)

    val rockRepeat = game.round - rockOffset
    val heightPerRepeat = game.height - heightOffset
    val repeats = (1000000000000 - rockOffset) / rockRepeat

    // run remaining rocks
    val remainder = (1000000000000 - rockOffset) % rockRepeat
    val height = game.height
    repeat(remainder.toInt()) { game.next() }
    val remainderHeight = game.height - height

    println(repeats * heightPerRepeat + heightOffset + remainderHeight)
}
