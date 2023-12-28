package day13

import common.println
import common.readParagraphs
import common.transpose
import kotlin.system.measureTimeMillis

fun main() {
    val patterns = readParagraphs("day13/input.txt")
        .map {
            Pattern(it.split("\n", "\r\n"))
        }

    measureTimeMillis {
        part1(patterns)
    }.also { println("Part 1 $it ms") }
}

private fun part1(patterns: List<Pattern>) {
    patterns.sumOf {
        val rowReflection = it.reflectionIndex()
        if (rowReflection == 0) {
            val colReflection = it.transpose().reflectionIndex()
            colReflection + 1
        } else {
            100 * (rowReflection + 1)
        }
    }.println()
}

private data class Pattern(val pattern: List<String>) {
    fun transpose(): Pattern = Pattern(pattern.transpose())

    fun reflectionIndex(): Int {
        for (index in 0 until (pattern.size - 1)) {
            val first = pattern.subList(0, index + 1)
            val second = pattern.subList(index + 1, pattern.lastIndex + 1)

            if (first.size < second.size) {
                val mirror = second.subList(0, first.size).reversed()
                if (first == mirror) {
                    return index
                }
            } else if (first.size > second.size) {
                val mirror = first.reversed().subList(0, second.size)
                if (mirror == second) {
                    return index
                }
            } else {
                if (first == second.reversed()) {
                    return index
                }
            }
        }
        return 0
    }
}
