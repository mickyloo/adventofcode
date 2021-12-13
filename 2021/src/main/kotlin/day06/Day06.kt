package day06

import java.io.File
import kotlin.system.measureTimeMillis

const val NEW_SPAWN_DAYS = 8
const val RESPAWN_DAYS = 6

fun main() {
    val fishes = File("src/main/kotlin/day06/input.txt")
        .readText()
        .trim()
        .split(",")
        .map { it.toInt() }
        .groupBy { it }
        .mapValues { it.value.count().toLong() }

    val elapsed1 = measureTimeMillis { println(FishPool(fishes).spawn(days = 80)) }
    val elapsed2 = measureTimeMillis { println(FishPool(fishes).spawn(days = 256)) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

class FishPool(private val initial: Map<Int, Long>) {
    private val pool: MutableMap<Int, Long> = initial.toMutableMap()
    fun spawn(days: Int): Long {
        (0 until days).forEach { _ ->
            for (i in 0..NEW_SPAWN_DAYS) {
                pool[i - 1] = pool.getOrDefault(i, 0)
            }
            pool[RESPAWN_DAYS] = pool.getOrDefault(RESPAWN_DAYS, 0) + pool.getOrDefault(-1, 0)
            pool[NEW_SPAWN_DAYS] = pool.getOrDefault(-1, 0)
            pool[-1] = 0
        }

        return descendants()
    }

    private fun descendants() = pool.values.sum()

    override fun toString(): String {
        return pool.toString()
    }
}