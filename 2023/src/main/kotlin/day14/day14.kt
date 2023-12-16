package day14

import common.Cardinal
import common.println
import common.readLines
import common.transpose
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day14/input.txt")

    measureTimeMillis {
        part1(Dish(lines))
    }.also { println("Part 1 $it ms") }

    measureTimeMillis {
        part2(Dish(lines))
    }.also { println("Part 2 $it ms") }
}

private fun part1(dish: Dish) {
    dish.tilt(Cardinal.N).totalLoad().println()
}

private fun part2(dish: Dish) {
    val patterns = mutableListOf<String>()
    val loads = mutableListOf<Int>()

    var current = dish
    repeat(500) {
        current = current.tilt(Cardinal.N).tilt(Cardinal.W).tilt(Cardinal.S).tilt(Cardinal.E)
        patterns.add(current.pattern())
        loads.add(current.totalLoad())
    }

    val offset = 200
    val patternToFind = patterns[offset]
    val search = patterns.subList(offset + 1, patterns.size - 1).indexOf(patternToFind)
    val modulo = search + 1
    val loadIndex = (1000000000 - offset - 1) % modulo
    println(loads[offset + loadIndex])

}

private data class Dish(val rows : List<String>) {
    fun totalLoad() =
        rows.mapIndexed { index, row ->
            row.map { if (it == 'O') rows.size - index else 0 }.sum()
        }.sum()

    fun pattern() = rows.joinToString("\n")

    fun tilt(direction: Cardinal): Dish {
        val dish = when(direction) {
            Cardinal.N, Cardinal.S -> this.rows.transpose()
            else -> this.rows
        }

        val newDish= dish.map { row ->
            row.split("#")
                .joinToString("#") {
                    val rocks = it.count { ch -> ch == 'O' }
                    val empty = it.length - rocks
                    when(direction) {
                        Cardinal.E, Cardinal.S -> "${".".repeat(empty)}${"O".repeat(rocks)}"
                        Cardinal.W, Cardinal.N -> "${"O".repeat(rocks)}${".".repeat(empty)}"
                    }
                }
        }

        return when(direction) {
            Cardinal.N, Cardinal.S -> Dish(newDish.transpose())
            else -> Dish(newDish)
        }
    }
}