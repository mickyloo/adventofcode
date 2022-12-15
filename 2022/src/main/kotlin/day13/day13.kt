package day13


import java.io.File
import kotlin.system.measureTimeMillis

fun main() {

    val blocks = File("src/main/kotlin/day13/input.txt").readText()
        .trimEnd()
        .split("\n\n", "\r\n\r\n")

    val elapsed1 = measureTimeMillis { part1(blocks) }
    val elapsed2 = measureTimeMillis { part2(blocks) }

    println("Part1: $elapsed1 ms")
    println("Part2: $elapsed2 ms")
}

fun part1(blocks: List<String>) {
    val result = blocks
        .map { block ->
            val (left, right) = block.lines().map { parse(it) }
            left.compareTo(right)
        }
        .withIndex()
        .sumOf {
            if (it.value < 0) it.index + 1 else 0
        }

    println(result)
}

fun part2(blocks: List<String>) {
    val divider2 = parse("[[2]]")
    val divider6 = parse("[[6]]")

    val signals = blocks
        .flatMap { block ->
            block.lines().map { parse(it) }
        }.plus(
            listOf(divider2, divider6)
        ).sorted()

    println((signals.indexOf(divider2) + 1) * (signals.indexOf(divider6) + 1))
}


fun parse(s: String): NumOrList.Nested {
    val parsed = NumOrList.Nested(mutableListOf())
    val stack = mutableListOf(parsed)

    var i = 1
    while (i < s.length) {
        when (s[i]) {
            '[' -> {
                val subList = NumOrList.Nested(mutableListOf())
                stack.last().list.add(subList)
                stack.add(subList)
            }

            ']' -> {
                stack.removeLast()
            }

            ',' -> {}
            '0', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                val number = NumOrList.Single(s[i].toString().toInt())
                stack.last().list.add(number)
            }

            '1' -> {
                if (s[i + 1] == '0') {
                    stack.last().list.add(NumOrList.Single(10))
                    i++ // extra increment to consume the 0 to make 10
                } else {
                    stack.last().list.add(NumOrList.Single(1))
                }
            }
        }
        i++
    }

    return parsed
}

sealed class NumOrList : Comparable<NumOrList> {
    data class Single(val num: Int) : NumOrList() {
        fun toNested(): Nested = Nested(mutableListOf(this))
        override fun compareTo(other: NumOrList): Int {
            return when (other) {
                is Single -> num.compareTo(other.num)
                is Nested -> this.toNested().compareTo(other)
            }
        }

        override fun toString(): String = num.toString()
    }

    data class Nested(val list: MutableList<NumOrList>) : NumOrList() {
        override fun compareTo(other: NumOrList): Int {
            when (other) {
                is Single -> return this.compareTo(other.toNested())
                is Nested -> {
                    if (list.isEmpty() && other.list.isEmpty()) return 0
                    val zipped = list zip other.list
                    for ((left, right) in zipped) {
                        val result = left.compareTo(right)
                        if (result < 0) return -1
                        if (result > 0) return 1
                    }
                    if (zipped.size == list.size) return -1
                    if (zipped.size == other.list.size) return 1
                    return 0
                }
            }
        }

        override fun toString(): String = "${list}"
    }
}