package day11

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {

    val blocks = File("src/main/kotlin/day11/input.example.txt")
        .readText()
        .trimEnd()
        .split("\n\n", "\r\n\r\n")

    //val elapsed1 = measureTimeMillis { part1(setUpMonkeys(blocks)) }
    val elapsed2 = measureTimeMillis { part2(setUpMonkeys(blocks)) }

    //println("Part1: $elapsed1 ms")
    //println("Part2: $elapsed2 ms")
}

data class Monkey(
    val order: Int,
    val items: MutableList<Int> = mutableListOf(),
    val transform: (Int) -> Int,
    val modulo: Int,
    val targets: Pair<Int, Int>
) {
    override fun toString(): String {
        return "$order: $items"
    }
}

fun setUpMonkeys(blocks: List<String>): List<Monkey> {
    val monkeys = blocks.map {block ->
        var lines = block.lines()
        val number = lines[0].split(" ").last().replace(":","").toInt()
        val items = lines[1].split(": ").last().split(", ").map { it.toInt() }

        val transformParts = lines[2].split(" ")

        val function = if (transformParts.last() == "old") {
            { worry: Int -> (worry * worry) }
        } else {
            val op = transformParts[transformParts.size - 2]
            val opValue = transformParts.last().toInt()
            when(op) {
                "*" -> { worry: Int -> (worry * opValue) }
                "+" -> { worry: Int -> (worry + opValue) }
                else -> throw Exception("parsing error")
            }
        }

        val modulo = lines[3].split(" ").last().toInt()
        val trueCase = lines[4].split(" ").last().toInt()
        val falseCase = lines[5].split(" ").last().toInt()

        Monkey(number, items.toMutableList(), function, modulo, trueCase to falseCase)
    }.toList()

    return monkeys
}

fun part1(monkeys: List<Monkey>) {
    val counter = monkeys.associate { it.order to 0L }.toMutableMap()

    repeat(20) {
        for(monkey in monkeys) {
            for(worry in monkey.items) {
                val newWorry = monkey.transform(worry) / 3
                val newMonkey = if (newWorry % monkey.modulo == 0) monkey.targets.first else monkey.targets.second
                monkeys[newMonkey].items.add(newWorry)
            }
            counter[monkey.order] = counter[monkey.order]!! + monkey.items.size
            monkey.items.clear()
        }
    }


    val result = counter.values.sortedDescending().take(2).reduce { acc, i -> acc * i }
    println(result)

}

fun part2(monkeys: List<Monkey>) {
    val counter = monkeys.associate { it.order to 0L }.toMutableMap()
    val modulo = monkeys.map { it.modulo }.reduce { acc, i -> acc * i }
    println(modulo)

    repeat(1000) {
        println("== iteration $it ==")
        for(monkey in monkeys) {
            for(worry in monkey.items) {
                val newWorry = monkey.transform(worry.mod(modulo))
                val newMonkey = if (newWorry % monkey.modulo == 0) monkey.targets.first else monkey.targets.second

                println("m ${monkey.order}, test ${monkey.modulo}, $worry -> $newWorry, throw to $newMonkey")
                monkeys[newMonkey].items.add(newWorry)
            }
            counter[monkey.order] = counter[monkey.order]!! + monkey.items.size
            monkey.items.clear()
        }
        println()
    }
    monkeys.forEach { println(it) }
    println(counter)

    val result = counter.values.sortedDescending().take(2).reduce { acc, i -> acc * i }
    println(result)
}
