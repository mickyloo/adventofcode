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
    val first = parse(blocks[0].lines()[0])
    val second = parse(blocks[0].lines()[1])
}

fun part2(blocks: List<String>) {


}


fun parse(s: String): NumOrList.Nested {

    val parsed = NumOrList.Nested(mutableListOf())
    val stack = mutableListOf(parsed)

    var i = 1
    while(i < s.length) {
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

sealed class NumOrList {
    data class Single(val num: Int): NumOrList() {
        fun toNested(): Nested = Nested(mutableListOf(this))
        override fun toString(): String = num.toString()
    }

    data class Nested(val list: MutableList<NumOrList>): NumOrList() {
        override fun toString(): String = "${list}"
    }
}