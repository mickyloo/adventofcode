package day21

import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val lines = readLines("day21/input.txt")
        .associate {
            val (name, expression) = it.split(": ")
            name to expression
        }

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

fun part1(lines: Map<String, String>) {
    val monkeys = makeMonkeyMap(lines)
    println(monkeys["root"]!!.invoke())
}

fun part2(lines: Map<String, String>) {
    val monkeys = makeMonkeyMap(lines)
    val (left, _, right) = lines["root"]!!.split(" ")

    val goal = monkeys[right]!!.invoke()

    var bounds = 1..10_000_000_000_000L
    val monkey = monkeys[left]!!
    var guess = monkeys["humn"]!!.invoke()

    while (true) {
        monkeys["humn"] = { guess }

        val current = monkey.invoke()
        if (goal == current) {
            break
        } else if (current < goal) {
            bounds = bounds.first..guess
        } else {
            bounds = guess..bounds.last
        }
        guess = (bounds.first + bounds.last) / 2
    }

    while (monkey.invoke() == goal) {
        monkeys["humn"] = { --guess }
    }
    println(guess + 1)
}

private fun makeMonkeyMap(lines: Map<String, String>): MutableMap<String, () -> Long> {
    val monkeys = mutableMapOf<String, () -> Long>()
    lines.forEach {
        val parts = it.value.split(" ")

        val expression = when {
            parts.size == 1 -> ({ parts[0].toLong() })
            parts[1] == "*" -> ({ monkeys[parts[0]]!!.invoke() * monkeys[parts[2]]!!.invoke() })
            parts[1] == "+" -> ({ monkeys[parts[0]]!!.invoke() + monkeys[parts[2]]!!.invoke() })
            parts[1] == "-" -> ({ monkeys[parts[0]]!!.invoke() - monkeys[parts[2]]!!.invoke() })
            parts[1] == "/" -> ({ monkeys[parts[0]]!!.invoke() / monkeys[parts[2]]!!.invoke() })
            else -> throw Exception("parse error")
        }
        monkeys[it.key] = expression
    }
    return monkeys
}
