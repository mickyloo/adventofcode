package day22

import java.io.File
import kotlin.system.measureTimeMillis

private const val WALL = "#"
private const val EMPTY = "."

enum class Direction(val dx: Int, val dy: Int, val value: Int) {
    U(0, -1, 3),
    D(0,1, 1),
    L(-1,0, 2),
    R(1,0, 0);

    fun cw() : Direction =
       when(this) {
           U -> R
           D -> L
           L -> U
           R -> D
       }
    fun ccw() : Direction =
        when(this) {
            U -> L
            D -> R
            L -> D
            R -> U
        }
}

fun main() {
    val (boardMap, movement) = File("src/main/kotlin/day22/input.txt")
        .readText()
        .trimEnd()
        .split("\n\n", "\r\n\r\n")

    val map = boardMap.lines()
        .mapIndexed { y, line -> line
            line.chunked(1).mapIndexed { x, char ->
                Point(x,y) to char
            }
        }
        .flatten()
        .filterNot { it.second == " " }
        .toMap()

    val elapsed1 = measureTimeMillis { part1(WrappedMap(map), movement) }
    println("Part1: $elapsed1 ms")

//    val elapsed2 = measureTimeMillis { part2(lines) }
//    println("Part2: $elapsed1 ms")

}

fun part1(map: ElfMap, movement: String) {
    val re = Regex("""(\d+|R|L)""")
    var direction = Direction.R
    var current = map.start

    for (move in re.findAll(movement)) {
        when(move.value) {
            "R" -> direction = direction.cw()
            "L" -> direction = direction.ccw()
            else -> {
                repeat(move.value.toInt()) {
                    val (next, direction) = map.next(current, direction)
                    if (map.get(next)!! == EMPTY) {
                        current = next
                    }
                }
            }
        }
    }

    println(1000 * (current.y + 1) + 4 * (current.x + 1) + direction.value)
}

data class Point(val x: Int, val y:Int)
interface ElfMap {
    val start: Point
    fun next(p: Point, direction: Direction): Pair<Point, Direction>
    fun get(p: Point): String
}

data class WrappedMap(val map: Map<Point, String>): ElfMap {
    private val rowRanges = (0..map.keys.maxOf { it.y })
        .associateWith { row ->
            val rows = map.filter { it.key.y == row }
            (rows.minOf { it.key.x }..rows.maxOf { it.key.x })
        }

    private val colRanges = (0..map.keys.maxOf { it.x })
        .associateWith { col ->
            val cols = map.filter { it.key.x == col }
            (cols.minOf { it.key.y }..cols.maxOf { it.key.y })
        }

    override val start: Point
        get() = Point(rowRanges[0]!!.first, colRanges[rowRanges[0]!!.first]!!.first)

    override fun next(p: Point, direction: Direction): Pair<Point, Direction> {
        val next = Point(p.x + direction.dx, p.y + direction.dy)
        if (next in map)
            return next to direction

        val wrapped = when(direction) {
            Direction.U -> Point(next.x, colRanges[next.x]!!.last)
            Direction.D -> Point(next.x, colRanges[next.x]!!.first)
            Direction.L -> Point(rowRanges[next.y]!!.last, next.y)
            Direction.R -> Point(rowRanges[next.y]!!.first, next.y)
        }
        if (wrapped !in map) throw Exception("warp went wrong $p, $direction -> $wrapped")

        return wrapped to direction
    }

    override fun get(p: Point): String = map[p]!!
}

data class CubeMap(val map: Map<Point, String>): ElfMap {
    override val start: Point
        get() = TODO("Not yet implemented")

    override fun next(p: Point, direction: Direction): Pair<Point, Direction> {
        TODO("Not yet implemented")
    }

    override fun get(p: Point): String = map[p]!!
}