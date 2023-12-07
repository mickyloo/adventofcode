package day07

import common.println
import common.readLines
import kotlin.system.measureTimeMillis

fun main() {
    val hands = readLines("day07/input.txt")
        .map {
            val (cards, bet) = it.split(" ")
            Hand(cards, bet.toInt())
        }

    val elapsed1 = measureTimeMillis { part1(hands) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(hands) }
    println("Part2: $elapsed2 ms")
}

private fun part1(hands: List<Hand>) {
    hands
        .sortedBy { it.score() }
        .mapIndexed { index, hand -> (index + 1) * hand.bet }
        .sum()
        .println()
}

private fun part2(hands: List<Hand>) {
    part1(hands.map { JokerHand(it.cards, it.bet) })
}


private open class Hand(val cards: String, val bet: Int) {
    open val alphabet = "__23456789TJQKA"
    open fun type(): Type {
        val freq = cards.groupingBy { it }.eachCount()
        return when (freq.size) {
            1 -> Type.FIVE_OF_KIND
            2 -> if (freq.values.contains(4)) Type.FOUR_OF_KIND else Type.FULL_HOUSE
            3 -> if (freq.values.contains(3)) Type.THREE_OF_KIND else Type.TWO_PAIR
            4 -> Type.ONE_PAIR
            5 -> Type.HIGH_CARD
            else -> throw Error()
        }
    }

    // use bits 30 -> 24 for the type
    // use bits 20 -> 0 for the cards, each card uses 4 bits
    fun score() = cards
        .mapIndexed { index, c ->
            alphabet.indexOf(c) shl (20 - 4 * index)
        }
        .sum() + this.type().bit

}

private class JokerHand(cards: String, bet: Int) : Hand(cards, bet) {
    override val alphabet = "_J23456789TQKA"
    override fun type(): Type {
        val type = super.type()
        val jokers = cards.count { it == 'J' }
        return when (jokers) {
            0 -> type
            1 -> when (type) {
                Type.FOUR_OF_KIND -> Type.FIVE_OF_KIND
                Type.THREE_OF_KIND -> Type.FOUR_OF_KIND
                Type.TWO_PAIR -> Type.FULL_HOUSE
                Type.ONE_PAIR -> Type.THREE_OF_KIND
                Type.HIGH_CARD -> Type.ONE_PAIR
                else -> throw Error("not possible")
            }

            2 -> when (type) {
                Type.FULL_HOUSE -> Type.FIVE_OF_KIND
                Type.TWO_PAIR -> Type.FOUR_OF_KIND
                Type.ONE_PAIR -> Type.THREE_OF_KIND
                else -> throw Error("not possible")
            }

            3 -> when (type) {
                Type.FULL_HOUSE -> Type.FIVE_OF_KIND
                Type.THREE_OF_KIND -> Type.FOUR_OF_KIND
                else -> throw Error("not possible")
            }

            4 -> Type.FIVE_OF_KIND
            5 -> Type.FIVE_OF_KIND
            else -> throw Error("not possible")
        }
    }
}


private enum class Type(val bit: Int) {
    FIVE_OF_KIND(1 shl 30),
    FOUR_OF_KIND(1 shl 29),
    FULL_HOUSE(1 shl 28),
    THREE_OF_KIND(1 shl 27),
    TWO_PAIR(1 shl 26),
    ONE_PAIR(1 shl 25),
    HIGH_CARD(1 shl 24)
}