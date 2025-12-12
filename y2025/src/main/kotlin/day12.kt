package dev.mickyloo

import common.readParagraphs
import common.toNumbers

fun main() {
    val paragraphs = readParagraphs("inputs/day12.txt")

    val containers = paragraphs.last().trim()
        .split("\r\n", "\n")

    val giftSizes = paragraphs.map { it.count { c -> c == '#' } }

    val total = containers.sumOf { container ->
        val parts = container.toNumbers()
        val giftCounts = parts.drop(2)

        val area = parts[0] * parts[1]
        val requiredArea = giftCounts.mapIndexed { index, i -> giftSizes[index] * i }.sum()

        if (requiredArea > area) 0 else 1
    }
    println(total)
}
