package day20

import java.io.File
import kotlin.system.measureTimeMillis

val lines = File("src/main/kotlin/day20/input.txt")
    .readLines()

val algo = lines[0]
val imageString = lines.subList(2, lines.size)

const val ON = '#'
const val OFF = '.'
val PIXELS = listOf(
    -1 to -1,
    0 to -1,
    1 to -1,
    -1 to 0,
    0 to 0,
    1 to 0,
    -1 to 1,
    0 to 1,
    1 to 1
)

fun main() {
    val elapsed1 = measureTimeMillis { run(2) }
    val elapsed2 = measureTimeMillis { run(50) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

data class Pixel(val x: Int, val y: Int)

class Image(
    private val image: MutableMap<Pixel, Char>,
    private val xRange: IntRange,
    private val yRange: IntRange,
    private val default: Char = OFF
) {

    val lit: Int
        get() = image.count { it.value == ON }

    fun enhanceImage(): Image {
        val newGrid = mutableMapOf<Pixel, Char>()
        yRange.forEach { row ->
            xRange.map { col ->
                val index = PIXELS.map {
                    when (image.getOrDefault(Pixel(col + it.first, row + it.second), default)) {
                        ON -> '1'
                        else -> '0'
                    }
                }.joinToString("").toInt(2)
                newGrid[Pixel(col, row)] = algo[index]
            }
        }

        val newDefault = algo[if (default == ON) 511 else 0]

        return Image(
            image = newGrid,
            xRange = (xRange.first - 1)..(xRange.last + 1),
            yRange = (yRange.first - 1)..(yRange.last + 1),
            newDefault
        )
    }


    companion object {
        fun parse(strings: List<String>): Image {
            val grid = mutableMapOf<Pixel, Char>()
            for ((row, line) in strings.withIndex()) {
                for ((col, char) in line.withIndex()) {
                    if (char != '.') {
                        grid[Pixel(col, row)] = char
                    }
                }
            }
            return Image(grid, -1 until strings[0].length + 1, -1 until strings.size + 1)
        }
    }

    override fun toString(): String = yRange
        .joinToString("\n") { row ->
            xRange.map { col ->
                image.getOrDefault(Pixel(col, row), default)
            }.joinToString("")
        }
}

fun run(iteration: Int) {
    var image: Image = Image.parse(imageString)
    repeat(iteration) {
        image = image.enhanceImage()
    }
    println(image.lit)
}
