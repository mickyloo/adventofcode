package day18

import java.io.File
import kotlin.math.ceil
import kotlin.system.measureTimeMillis

fun main() {
    val numbers = File("src/main/kotlin/day18/input.txt")
        .readLines()

    val elapsed1 = measureTimeMillis { part1(numbers) }
    val elapsed2 = measureTimeMillis { part2(numbers) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

private fun String.toSNum(): SNum {
    return this.iterator().toSNum() as SNum
}

private fun CharIterator.toSNum(): Number {
    return when (val c = this.next()) {
        '[' -> return SNum(this.toSNum(), this.toSNum())
        ',' -> this.toSNum()
        ']' -> this.toSNum()
        else -> return Scalar(c.toString().toInt())
    }
}


interface Number {
    val magnitude: Int
}

class SNum(var left: Number, var right: Number) : Number {
    data class Node(val number: Number, val depth: Int, val parent: Number?)

    val nodeOrder: MutableList<Node> = mutableListOf()
    override val magnitude: Int
        get() {
            return (3 * left.magnitude) + (2 * right.magnitude)
        }

    private fun buildNodeOrder() {
        fun dfs(node: Number, depth: Int, parent: Number?) {
            nodeOrder.add(Node(node, depth, parent))
            if (node is SNum) {
                dfs(node.left, depth + 1, node)
                dfs(node.right, depth + 1, node)
            }
        }

        nodeOrder.clear()
        dfs(this, 0, null)
    }

    private fun explode(index: Int) {
        var exploded = nodeOrder[index].number as SNum
        val parent = nodeOrder[index].parent as SNum

        for (leftIndex in index downTo 0) {
            if (nodeOrder[leftIndex].number is Scalar) {
                (nodeOrder[leftIndex].number as Scalar).value += (exploded.left as Scalar).value
                break
            }
        }

        for (rightIndex in index + 3 until nodeOrder.size) {
            if (nodeOrder[rightIndex].number is Scalar) {
                (nodeOrder[rightIndex].number as Scalar).value += (exploded.right as Scalar).value
                break
            }
        }

        if (parent.left == exploded) {
            parent.left = 0.s
        } else {
            parent.right = 0.s
        }
    }

    fun reduce(): SNum {
        var modified: Boolean
        do {
            buildNodeOrder()
            modified = false
            nodeOrder
                .indexOfFirst { (it.number is SNum) && (it.depth >= 4) }
                .takeIf { it > 0 }
                ?.let {
                    explode(it)
                    modified = true
                }
            if (modified) continue

            nodeOrder
                .indexOfFirst { (it.number is Scalar) && (it.number.value >= 10) }
                .takeIf { it > 0 }
                ?.let {
                    split(it)
                    modified = true
                }
        } while (modified)

        return this
    }

    private fun split(index: Int) {
        var split = nodeOrder[index].number as Scalar
        val parent = nodeOrder[index].parent as SNum
        val newSNum = SNum(Scalar(split.value / 2), Scalar(ceil(split.value / 2.0).toInt()))
        if (parent.left == split) {
            parent.left = newSNum
        } else {
            parent.right = newSNum
        }
    }

    operator fun plus(b: SNum): SNum {
        return SNum(this, b)
    }

    override fun toString(): String {
        return "[$left,$right]"
    }
}

class Scalar(value: Int) : Number {
    var value = value
    override val magnitude: Int
        get() = value

    override fun toString(): String {
        return "$value"
    }
}

private val Int.s: Scalar
    get() = Scalar(this)

fun part2(numbers: List<String>) {
    var max = 0
    numbers.indices.forEach { i ->
        numbers.indices.forEach { j ->
            if (i != j) {
                val sum = (numbers[i].toSNum() + numbers[j].toSNum()).reduce()
                if (sum.magnitude > max) {
                    max = sum.magnitude
                }
            }
        }
    }

    println(max)
}

fun part1(numbers: List<String>) {
    val sum = numbers
        .map { it.toSNum() }
        .reduce { acc, sNum -> (acc + sNum).reduce() }
    println(sum.magnitude)
}
