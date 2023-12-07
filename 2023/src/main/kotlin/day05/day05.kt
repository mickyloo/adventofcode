package day05

import common.anyOverlap
import common.println
import common.readParagraphs
import kotlin.system.measureTimeMillis

fun main() {
    val paragraphs = readParagraphs("day05/input.txt")
    val seeds = paragraphs[0].substringAfter(": ").split(" ").map { it.trim().toLong() }
    val mappers = paragraphs.drop(1).map { it.toMapper() }

    val elapsed1 = measureTimeMillis { part1(seeds, mappers) }
    println("Part1: $elapsed1 ms")

    val seedRanges = seeds
        .chunked(2)
        .map {
            it[0] until (it[0] + it[1])
        }

    val elapsed2 = measureTimeMillis { part2(seedRanges, mappers) }
    println("Part2: $elapsed2 ms")
}

private fun part1(seeds: List<Long>, lookups: List<Mapper>) {
    seeds.minOf {
        var result = it
        lookups.forEach { lookup ->
            result = lookup.mapOne(result)
        }
        result
    }.println()
}

private fun part2(seedRanges: List<LongRange>, lookups: List<Mapper>) {
    var mapped = seedRanges
    lookups.forEach { lookup ->
        mapped = mapped
            .map { range -> lookup.mapOne(range) }
            .flatten()
    }
    mapped.minOf { it.first }.println()
}


private fun String.toMapper(): Mapper {
    val lines = this.split("\n", "\r\n")
    val name = lines[0]
    val ranges = lines
        .drop(1)
        .map {
            val (dest, source, range) = it.split(" ").map { part -> part.toLong() }
            MapperRange(source until (source + range), (dest until (dest + range)))
        }.sortedBy {
            it.src.first
        }

    return Mapper(name, ranges)
}

private data class MapperRange(val src: LongRange, val dest: LongRange) {
    fun map(value: Long): Long {
        val delta = value - src.first
        return dest.first + delta
    }
}

private data class Mapper(val name: String, val ranges: List<MapperRange>) {
    fun mapOne(source: Long): Long {
        val dest = ranges.firstOrNull {
            source in it.src
        }?.let {
            it.map(source)
        }
        return dest ?: source
    }

    // splits the input interval on the boundaries of the mappers
    // after split, the list of output intervals do not cross mapper boundaries
    // meaning that just mapping the start/end of the interval is enough to get new mapping
    fun split(input: LongRange): List<LongRange> {
        val boundaries = mutableSetOf(input.first, input.last + 1)
        for (range in ranges) {
            if (!(input anyOverlap range.src)) continue

            if (range.src.first > input.first) {
                boundaries.add(range.src.first)
            }
            if (range.src.last < input.last) {
                boundaries.add(range.src.last + 1)
            }
        }
        return boundaries
            .sorted()
            .windowed(2)
            .map { it.first() until it.last() }
    }

    fun mapOne(source: LongRange): List<LongRange> =
        split(source)
            .map {
                mapOne(it.first)..mapOne(it.last)
            }
}