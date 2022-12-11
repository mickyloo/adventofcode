package day11

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {

    val blocks = File("src/main/kotlin/day11/input.txt").readText()
        .trimEnd()
        .split("\n\n", "\r\n\r\n")

    val elapsed1 = measureTimeMillis { part1(blocks) }
    val elapsed2 = measureTimeMillis { part2(blocks) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

data class Monkey(
    val order: Int,
    val items: MutableList<Long> = mutableListOf(),
    val transform: (Long) -> Long,
    val test: Int,
    val targets: Pair<Int, Int>
)

fun setUpMonkeys(blocks: List<String>): List<Monkey> {
    val monkeys = blocks.map { block ->
        var lines = block.lines()
        val number = lines[0].split(" ").last().replace(":", "").toInt()
        val items = lines[1].split(": ").last().split(", ").map { it.toLong() }

        val transformParts = lines[2].split(" ")

        val function = if (transformParts.last() == "old") {
            { worry: Long -> (worry * worry) }
        } else {
            val op = transformParts[transformParts.size - 2]
            val opValue = transformParts.last().toInt()
            when (op) {
                "*" -> { worry: Long -> (worry * opValue) }
                "+" -> { worry: Long -> (worry + opValue) }
                else -> throw Exception("parsing error")
            }
        }

        val test = lines[3].split(" ").last().toInt()
        val trueCase = lines[4].split(" ").last().toInt()
        val falseCase = lines[5].split(" ").last().toInt()

        Monkey(number, items.toMutableList(), function, test, trueCase to falseCase)
    }.toList()

    return monkeys
}

fun solution(monkeys: List<Monkey>, iterations: Int, limiter: (Long) -> Long) {
    val counter = monkeys.associate { it.order to 0L }.toMutableMap()

    repeat(iterations) {
        for (monkey in monkeys) {
            for (worry in monkey.items) {
                val newWorry = limiter(monkey.transform(worry))
                val newMonkey = if (newWorry % monkey.test == 0L) monkey.targets.first else monkey.targets.second
                monkeys[newMonkey].items.add(newWorry)
            }
            counter[monkey.order] = counter[monkey.order]!! + monkey.items.size
            monkey.items.clear()
        }
    }

    val result = counter.values.sortedDescending().take(2).reduce { acc, i -> acc * i }
    println(result)
}

fun part1(blocks: List<String>) {
    solution(setUpMonkeys(blocks), 20) { x ->
        x / 3
    }
}

fun part2(blocks: List<String>) {
    val monkeys = setUpMonkeys(blocks)
    val lcm = monkeys.map { it.test }.reduce { acc, i -> acc * i }.toLong()
    solution(setUpMonkeys(blocks), 10_000) { x ->
        x.mod(lcm)
    }
}