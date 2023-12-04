package day03

import common.NEIGHBORS8
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day03/input.txt")
    val grid: Array<CharArray> = Array(lines.size) { i ->
        (lines[i] + ".") // add a . at the end to handle special case where last digit is a number
            .toCharArray()
    }

    val elapsed1 = measureTimeMillis { part1(grid) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(grid) }
    println("Part2: $elapsed2 ms")
}

private typealias Point = Pair<Int, Int>

private fun part1(grid: Array<CharArray>) {
    var validParts = mutableListOf<Int>()
    for (y in grid.indices) {
        var runningNum = ""
        var numberIsValid = false
        for (x in grid[0].indices) {
            when (grid[y][x]) {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    runningNum += grid[y][x]
                    val symbols = grid.adjacentToSymbol(y, x)
                    if (symbols.isNotEmpty()) {
                        numberIsValid = true
                    }
                }

                else -> {
                    if (numberIsValid) {
                        validParts.add(runningNum.toInt())
                    }
                    runningNum = ""
                    numberIsValid = false
                }
            }
        }
    }
    println(validParts.sum());
}

private fun part2(grid: Array<CharArray>) {
    val symbolToNumbers = mutableMapOf<Point, MutableList<Int>>()
    for (y in grid.indices) {
        var runningNum = ""
        var numberIsValid = false
        val adjacentSymbols = mutableSetOf<Point>()
        for (x in grid[0].indices) {
            when (grid[y][x]) {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    runningNum += grid[y][x]
                    val symbols = grid.adjacentToSymbol(y, x)
                    if (symbols.isNotEmpty()) {
                        adjacentSymbols.addAll(symbols)
                        numberIsValid = true
                    }
                }

                else -> {
                    if (numberIsValid) {
                        adjacentSymbols.forEach {
                            val list = symbolToNumbers.getOrDefault(it, mutableListOf())
                            list.add(runningNum.toInt())
                            symbolToNumbers[it] = list
                        }
                    }
                    runningNum = ""
                    numberIsValid = false
                    adjacentSymbols.clear()
                }
            }
        }
    }

    val result = symbolToNumbers
        .filter { (key, value) ->
            grid[key.first][key.second] == '*' && value.size == 2
        }
        .values
        .sumOf {
            it[0] * it[1]
        }
    println(result)
}

private fun Array<CharArray>.adjacentToSymbol(y: Int, x: Int): List<Point> =
    NEIGHBORS8.mapNotNull {
        val newY = y + it.first
        val newX = x + it.second
        if (newX < 0 || newY < 0 || newY >= this.size || newX >= this[0].size) {
            null
        } else {
            when (this[newY][newX]) {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> null
                else -> newY to newX
            }
        }
    }