package day08

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val lines = File("src/main/kotlin/day08/input.txt").readLines()

    val elapsed1 = measureTimeMillis { part1(lines) }
    val elapsed2 = measureTimeMillis { part2(lines) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

val DIGITS: Map<Set<Char>, Char> = mapOf(
    setOf('a', 'b', 'c', 'e', 'f', 'g') to '0',
    setOf('c', 'f') to '1',
    setOf('a', 'c', 'd', 'e', 'g') to '2',
    setOf('a', 'c', 'd', 'f', 'g') to '3',
    setOf('b', 'c', 'd', 'f') to '4',
    setOf('a', 'b', 'd', 'f', 'g') to '5',
    setOf('a', 'b', 'd', 'e', 'f', 'g') to '6',
    setOf('a', 'c', 'f') to '7',
    setOf('a', 'b', 'c', 'd', 'f', 'e', 'g') to '8',
    setOf('a', 'b', 'c', 'd', 'f', 'g') to '9',
)

val SEGMENTS = setOf('a', 'b', 'c', 'd', 'f', 'e', 'g')

fun part2(lines: List<String>) {
    val outputs = lines
        .map { line ->
            val (signals, outputs) = line.split(" | ").map { it.split(" ") }
            val decoder = decipherSegment(signals)
            outputs.map { decodeOutput(decoder, it) }.joinToString("").toInt()
        }

    println(outputs.sum())
}

fun decipherSegment(signals: List<String>): Map<Char, Char> {
    /*
    Each signal input contains all 10 digits.  By looking at scrambled segment frequency, we can find some clues
     Expected segment frequency
     a = 8
     b = 6
     c = 8
     d = 7
     e = 4
     f = 9
     g = 7
     segment b, e, f are unique
     segment a can be found by look at digit 1 and digit 7
       digit 1 can be found by input with only 2 segments
       digit 7 can be found by input with only 3 segments
     segment g can be found by finding the input with abcef and size 6
     segment d is the final one not in the list
     */

    val signals = signals.map { signal -> signal.toCharArray().toList() }
    val frequencies = signals.flatten().groupingBy { it }.eachCount()

    val digit7 = signals.first { it.size == 3 }.toHashSet()
    val digit1 = signals.first { it.size == 2 }.toHashSet()

    val B = frequencies.filterValues { it == 6 }.keys.first()
    val E = frequencies.filterValues { it == 4 }.keys.first()
    val F = frequencies.filterValues { it == 9 }.keys.first()
    val A = (digit7 - digit1).first()
    val C = frequencies.filterValues { it == 8 }.filterKeys { it != A }.keys.first()

    val digit0 = signals.filter { it.size == 6 }.first { it.containsAll(setOf(A, B, C, E, F)) }
    val G = (digit0 - setOf(A, B, C, E, F)).first()
    val D = (SEGMENTS - setOf(A, B, C, E, F, G)).first()

    return mapOf(
        A to 'a',
        B to 'b',
        C to 'c',
        D to 'd',
        E to 'e',
        F to 'f',
        G to 'g'
    )
}

fun decodeOutput(decoder: Map<Char, Char>, output: String): Char {
    val remapped = output.toCharArray().map {
        decoder[it]!!
    }.toHashSet()
    return DIGITS[remapped]!!
}

fun part1(lines: List<String>) {
    val easyDigits = setOf(2, 4, 3, 7) // digits 1,4,7,8

    val countEasyDigitOutputs = lines
        .map { it.split(" | ")[1].split(" ") }
        .sumOf { line -> line.count { it.length in easyDigits } }

    println(countEasyDigitOutputs)
}
