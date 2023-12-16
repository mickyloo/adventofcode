package day15

import common.println
import common.readText
import kotlin.system.measureTimeMillis

fun main() {
    //val line = readText("day15/input.txt")
    val line = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"

    measureTimeMillis {
        part1(line)
    }.also { println("Part 1 $it ms") }

    measureTimeMillis {
        part2(line)
    }.also { println("Part 2 $it ms") }
}

fun part1(line: String) {
    line.split(",")
        .sumOf { it.hash() }
        .println()
}

fun part2(line: String) {
    val boxes = (0..255).map { mutableMapOf<String, Int>() }
    val re = """([a-z]+)(.)(\d+)?""".toRegex()

    line.split(",")
        .forEach {
            val (label, operation, focal) = re.matchEntire(it)!!.destructured
            boxes[label.hash()].also {
                when(operation) {
                    "=" -> it.put(label, focal.toInt())
                    else -> it.remove(label)
                }
            }
        }

    println(boxes.filter { it.isNotEmpty() })
}

private data class Lens(val label: String, val focal: Int)
private fun String.hash() : Int =
    this.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
