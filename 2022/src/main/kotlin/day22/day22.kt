package day22

import common.Point
import common.readParagraphs
import java.io.File
import kotlin.system.measureTimeMillis

private const val WALL = "#"
private const val EMPTY = "."

enum class Direction(val dx: Int, val dy: Int, val value: Int) {
    U(0, -1, 3),
    D(0, 1, 1),
    L(-1, 0, 2),
    R(1, 0, 0);

    fun cw(): Direction =
        when (this) {
            U -> R
            D -> L
            L -> U
            R -> D
        }

    fun ccw(): Direction =
        when (this) {
            U -> L
            D -> R
            L -> D
            R -> U
        }
}

fun main() {
    val (boardMap, movement) = File("src/main/kotlin/day22/input.txt").readParagraphs()

    val map = boardMap.lines()
        .mapIndexed { y, line ->
            line
            line.chunked(1).mapIndexed { x, char ->
                Point(x, y) to char
            }
        }
        .flatten()
        .filterNot { it.second == " " }
        .toMap()

    val elapsed1 = measureTimeMillis { solution(WrappedMap(map), movement) }
    println("Part1: $elapsed1 ms")

    val elapsed2 = measureTimeMillis { solution(CubeMap(map), movement) }
    println("Part2: $elapsed1 ms")

}

fun solution(map: ElfMap, movement: String) {
    val re = Regex("""(\d+|R|L)""")
    var direction = Direction.R
    var current = map.start

    for (move in re.findAll(movement)) {
        when (move.value) {
            "R" -> direction = direction.cw()
            "L" -> direction = direction.ccw()
            else -> {
                repeat(move.value.toInt()) {
                    val (next, newDirection) = map.next(current, direction)
                    if (map.get(next) == EMPTY) {
                        current = next
                        direction = newDirection
                    }
                }
            }
        }
    }

    println(1000 * (current.y + 1) + 4 * (current.x + 1) + direction.value)
}

interface ElfMap {
    val start: Point
    fun next(p: Point, direction: Direction): Vector
    fun get(p: Point): String
}

data class WrappedMap(val map: Map<Point, String>) : ElfMap {
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

    override fun next(p: Point, direction: Direction): Vector {
        val next = Point(p.x + direction.dx, p.y + direction.dy)
        if (next in map)
            return next to direction

        val wrapped = when (direction) {
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

private typealias Vector = Pair<Point, Direction>

/*
    For cubes that look like
    [   1 2 ]
    [   3   ]
    [ 4 5   ]
    [ 6     ]
 */
data class CubeMap(val map: Map<Point, String>) : ElfMap {
    private val translations = cubeTranslations()
    private fun cubeTranslations(): Map<Vector, Vector> {
        var edges = listOf<Pair<Vector, Vector>>()

        // surface 1 up -> surface 6 right
        edges += (50 until 100).map { x -> (Point(x, -1) to Direction.U) to (Point(0, x + 100) to Direction.R) }
        // surface 1 left -> surface 4 right
        edges += (0 until 50).map { y -> (Point(49, y) to Direction.L) to (Point(0, 149 - y) to Direction.R) }
        // surface 2 up -> surface 6 up
        edges += (100 until 150).map { x -> (Point(x, -1) to Direction.U) to (Point(x - 100, 199) to Direction.U) }
        // surface 2 right -> surface 5 left
        edges += (0 until 50).map { y -> (Point(150, y) to Direction.R) to (Point(99, 149 - y) to Direction.L) }
        // surface 2 down -> surface 3 left
        edges += (100 until 150).map { x -> (Point(x, 50) to Direction.D) to (Point(99, x - 50) to Direction.L) }
        // surface 3 left -> surface 4 down
        edges += (50 until 100).map { y -> (Point(49, y) to Direction.L) to (Point(y - 50, 100) to Direction.D) }
        // surface 3 right -> surface 2 up
        edges += (50 until 100).map { y -> (Point(100, y) to Direction.R) to (Point(y + 50, 49) to Direction.U) }
        // surface 4 up -> surface 3 right
        edges += (0 until 50).map { x -> (Point(x, 99) to Direction.U) to (Point(50, x + 50) to Direction.R) }
        // surface 4 left -> surface 1 right
        edges += (100 until 150).map { y -> (Point(-1, y) to Direction.L) to (Point(50, 149 - y) to Direction.R) }
        // surface 5 right -> surface 2 left
        edges += (100 until 150).map { y -> (Point(100, y) to Direction.R) to (Point(149, 149 - y) to Direction.L) }
        // surface 5 down -> surface 6 left
        edges += (50 until 100).map { x -> (Point(x, 150) to Direction.D) to (Point(49, x + 100) to Direction.L) }
        // surface 6 left -> surface 1 down
        edges += (150 until 200).map { y -> (Point(-1, y) to Direction.L) to (Point(y - 100, 0) to Direction.D) }
        // surface 6 right -> surface 5 up
        edges += (150 until 200).map { y -> (Point(50, y) to Direction.R) to (Point(y - 100, 149) to Direction.U) }
        // surface 6 down -> surface 2 down
        edges += (0 until 50).map { x -> (Point(x, 200) to Direction.D) to (Point(x + 100, 0) to Direction.D) }

        return edges.toMap()
    }

    override val start: Point
        get() = Point(50, 0)

    override fun next(p: Point, direction: Direction): Vector {
        val next = Point(p.x + direction.dx, p.y + direction.dy)
        if (next in map)
            return next to direction

        return translations[next to direction]!!
    }

    override fun get(p: Point): String = map[p]!!
}