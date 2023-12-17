package day17

import common.Cardinal
import common.println
import common.readLines
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val grid = readLines("day17/input.test.txt")
        .map {line ->
            line.toCharArray().map { it.digitToInt() }
        }

    measureTimeMillis {
        part1(grid)
    }.also { println("Part 1 $it ms") }

//    measureTimeMillis {
//        part2(grid)
//    }.also { println("Part 2 $it ms") }
}

fun part1(grid: List<List<Int>>) {
    val seen = mutableListOf(0)
    val work = mutableListOf(Path(0, 0, 0, listOf()))
    var minScore = Integer.MAX_VALUE
    val goalCoordinate = coordinate(grid.size - 1, grid[0].size - 1)

    while(work.isNotEmpty()) {
        val current = work.removeFirst()
        println(current)
        if (current.score > minScore) {
            continue
        }
        if (current.coordinate() == goalCoordinate) {
            minScore = min(minScore, current.score)
            continue
        }

        val candidates = grid.next(current)
        candidates
            .filter { !seen.contains(it.coordinate()) }
            .forEach {
                seen.add(it.coordinate())
                work.add(it)
            }
    }

    minScore.println()
}

private fun List<List<Int>>.isValid(row: Int, col: Int) =
    (row >= 0) && (row < this.size) && (col >= 0) && (col < this[0].size)

private fun List<List<Int>>.next(path: Path): List<Path> {
    val last3 = path.history.takeLast(3)
    return Cardinal
        .values()
        .mapNotNull { dir ->
            val y = path.row + dir.dy
            val x = path.col + dir.dx
            if (!this.isValid(y, x) || last3.count { it==dir } == 3) {
                null
            }
            else {
                Path(
                    row = y, col = x,
                    score = path.score + this[y][x],
                    history = path.history + dir
                )
            }
        }
}

internal fun coordinate(row: Int, col: Int) = row * 10_000 + col

private data class Path(val row: Int, val col: Int, val score: Int, val history: List<Cardinal>) {
    fun coordinate() = coordinate(row, col)
}