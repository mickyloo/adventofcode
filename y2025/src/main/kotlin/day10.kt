package dev.mickyloo

import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import common.powerset
import common.readLines
import common.toNumbers
import kotlin.system.measureTimeMillis


fun main() {
    val lines = readLines("inputs/day10.txt")

    val elapsed1 = measureTimeMillis { part1(lines) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { part2(lines) }
    println("Part2: $elapsed2 ms")
}

private fun part1(lines: List<String>) {
    val manuals = lines.map {
        val parts = it.split(" ")
        val goal = parts.first()
            .dropLast(1).drop(1)
            .replace(".", "0")
            .replace("#", "1")
            .reversed()
            .toInt(2)

        val switches = parts
            .drop(1).dropLast(1)
            .map { switchString ->
                switchString.toNumbers().sumOf { bit -> 1.shl(bit) }
            }
        goal to switches
    }.toList()

    val total = manuals.sumOf { (goal, switches) ->
        var count = Int.MAX_VALUE
        for (set in switches.powerset()) {
            if (set.size >= count) {
                continue
            }
            val res = set.fold(0) { acc, next -> acc xor next }
            if (res == goal) {
                count = set.size
            }
        }
        count
    }
    println(total)
}

private fun part2(lines: List<String>) {

    fun solve(goal: List<Int>, switches: List<List<Int>>): Int {
        val ctx = Context()
        ctx.use { ctx ->
            val opt = ctx.mkOptimize()

            val xVars = switches.indices.map { i ->
                val x = ctx.mkIntConst("x$i")
                opt.Assert(ctx.mkGe(x, ctx.mkInt(0)))
                x
            }

            goal.forEachIndexed { index, targetVal ->
                val terms = switches.withIndex()
                    .filter { it.value.contains(index) }
                    .map { xVars[it.index] }
                    .toTypedArray()

                val sum = if (terms.isNotEmpty()) ctx.mkAdd(*terms) else ctx.mkInt(0)
                opt.Assert(ctx.mkEq(sum, ctx.mkInt(targetVal)))
            }

            val total = ctx.mkIntConst("total")
            val totalSum = if (xVars.isNotEmpty()) ctx.mkAdd(*xVars.toTypedArray()) else ctx.mkInt(0)
            opt.Assert(ctx.mkEq(total, totalSum))

            opt.MkMinimize(total)

            if (opt.Check() == com.microsoft.z3.Status.SATISFIABLE) {
                return (opt.model.getConstInterp(total) as IntNum).int
            }
            throw RuntimeException("Unsatisfiable")
        }
    }

    val manuals: List<Pair<List<Int>, List<List<Int>>>> = lines.map {
        val parts = it.split(" ")
        val goal = parts.last()
            .toNumbers()

        val switches = parts
            .drop(1).dropLast(1)
            .map { switch -> switch.toNumbers() }

        goal to switches
    }.toList()

    val answer = manuals.sumOf { (goal, switches) ->
        solve(goal, switches)
    }
    println(answer)
}