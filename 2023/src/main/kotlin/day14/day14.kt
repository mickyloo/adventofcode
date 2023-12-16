package day14

import common.println
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day14/input.txt")

    measureTimeMillis {
        part1(lines)
    }.also { println("Part 1 $it ms") }

    measureTimeMillis {
        part2(lines)
    }.also { println("Part 2 $it ms") }
}

fun part1(dish: Dish) {
    dish.tilt(Direction.N).totalLoad().println()
}

fun part2(dish: Dish) {
    val patterns = mutableListOf<String>()
    val loads = mutableListOf<Int>()

    var current = dish
    repeat(500) {
        current = current.tilt(Direction.N).tilt(Direction.W).tilt(Direction.S).tilt(Direction.E)
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

private typealias Dish = List<String>
fun Dish.totalLoad() =
    this.mapIndexed { index, row ->
        row.map { if (it == 'O') this.size - index else 0 }.sum()
    }.sum()

fun Dish.transpose() = this.first()
    .mapIndexed { index, _ ->
        this.map { it[index]!! }.joinToString("")
    }

fun Dish.pattern() = this.joinToString("\n")
fun Dish.tilt(direction: Direction): Dish {

    val dish = when(direction) {
        Direction.N, Direction.S -> this.transpose()
        else -> this
    }

    val newDish= dish.map { row ->
        row.split("#")
            .joinToString("#") {
                val rocks = it.count { ch -> ch == 'O' }
                val empty = it.length - rocks
                when(direction) {
                    Direction.E, Direction.S -> "${".".repeat(empty)}${"O".repeat(rocks)}"
                    Direction.W, Direction.N -> "${"O".repeat(rocks)}${".".repeat(empty)}"
                }
            }
    }

    return when(direction) {
        Direction.N, Direction.S -> newDish.transpose()
        else -> newDish
    }
}

enum class Direction {
    N,S,E,W
}