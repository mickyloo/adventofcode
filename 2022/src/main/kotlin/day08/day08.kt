package day08

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val trees = readLines("day08/input.txt")
        .map { row ->
            row.chunked(1).map { it.toInt() }
        }

    val elapsed1 = measureTimeMillis { part1(trees) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(trees) }
    println("Part2: $elapsed2 ms")
}

fun part2(trees: List<List<Int>>) {
    val rows = trees.size
    val cols = trees[0].size

    val maxScore = sequence<Pair<Int, Int>> {
        for (row in 1 until rows - 1) {
            for (col in 1 until cols - 1) {
                yield(row to col)
            }
        }
    }.map {
        val (row, col) = it
        val height = trees[row][col]

        var viewLeft = 0
        for (i in col - 1 downTo 0) {
            viewLeft++
            if (trees[row][i] >= height) {
                break
            }
        }

        var viewUp = 0
        for (i in row - 1 downTo 0) {
            viewUp++
            if (trees[i][col] >= height) {
                break
            }
        }

        var viewDown = 0
        for (i in row + 1 until rows) {
            viewDown++
            if (trees[i][col] >= height) {
                break
            }
        }

        var viewRight = 0
        for (i in col + 1 until cols) {
            viewRight++
            if (trees[row][i] >= height) {
                break
            }
        }

        viewUp * viewDown * viewLeft * viewRight
    }.max()

    println(maxScore)
}

fun part1(trees: List<List<Int>>) {
    val rows = trees.size
    val cols = trees[0].size
    val perimeter = 2 * (rows + cols) - 4

    val visible = sequence<Pair<Int, Int>> {
        for (row in 1 until rows - 1) {
            for (col in 1 until cols - 1) {
                yield(row to col)
            }
        }
    }.count { cell ->
        val (row, col) = cell
        val height = trees[row][col]

        val left = (col - 1 downTo 0).maxOfOrNull { trees[row][it] }!! >= height
        val up = (row - 1 downTo 0).maxOfOrNull { trees[it][col] }!! >= height
        val down = (row + 1 until rows).maxOfOrNull { trees[it][col] }!! >= height
        val right = (col + 1 until cols).maxOfOrNull { trees[row][it] }!! >= height

        !(left && up && down && right)
    }

    println(perimeter + visible)
}
