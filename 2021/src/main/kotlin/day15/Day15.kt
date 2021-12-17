package day15

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val graph = parseInputToGraph(File("src/main/kotlin/day15/input.txt"))

    val elapsed1 = measureTimeMillis { part1(graph) }
    val elapsed2 = measureTimeMillis { part2(graph) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

fun part2(graph: Graph) {
    val (width, length) = graph.keys.last()
    val copies: Array<Map<Vertex, Int>> = Array(8) { index ->
        graph.mapValues {
            val new = it.value + index + 1
            if (new > 9) (new - 9) else new
        }
    }

    // copy current map
    val fullGraph: Graph = graph.toMutableMap()

    for (row in 0 until 5) {
        for (col in 0 until 5) {
            if ((row == 0) and (col == 0))
                continue
            val copy = copies[row + col - 1]
            copy.entries.forEach {
                val newKey = Vertex(
                    x = it.key.x + col * (width + 1),
                    y = it.key.y + row * (length + 1)
                )
                fullGraph[newKey] = it.value
            }
        }
    }
    part1(fullGraph)
}

fun part1(graph: Graph) {
    val target = graph.keys.last()
    val shortestPath = dijkstra(graph)
    println(shortestPath[target])
}

typealias Graph = MutableMap<Vertex, Int>

private fun parseInputToGraph(file: File): Graph {
    val graph: MutableMap<Vertex, Int> = mutableMapOf()
    file.readLines()
        .forEachIndexed { row, line ->
            for (col in line.indices) {
                graph[Vertex(col, row)] = line[col].toString().toInt()
            }
        }

    return graph
}

private fun Graph.neighbors(v: Vertex) =
    listOf(
        Vertex(v.x + 1, v.y),
        Vertex(v.x - 1, v.y),
        Vertex(v.x, v.y + 1),
        Vertex(v.x, v.y - 1),
    ).filter { it in this }


data class Vertex(val x: Int, val y: Int)

fun dijkstra(graph: Graph, source: Vertex = Vertex(0, 0)): Map<Vertex, Int> {

    val shortestDistance: MutableMap<Vertex, Int> = mutableMapOf()
    val unvisited: MutableSet<Vertex> = mutableSetOf()
    val visited: MutableSet<Vertex> = mutableSetOf()

    graph.keys.forEach {
        shortestDistance[it] = Int.MAX_VALUE
    }
    unvisited.add(source)
    shortestDistance[source] = 0

    while (unvisited.isNotEmpty()) {
        // find min node
        var minVertex: Vertex = unvisited.first()
        for (v in unvisited) {
            if (shortestDistance[v]!! < shortestDistance[minVertex]!!) {
                minVertex = v
            }
        }

        unvisited.remove(minVertex)
        visited.add(minVertex)
        for (n in graph.neighbors(minVertex)) {
            if (n !in graph) continue
            if ((n !in visited) and (n !in unvisited)) {
                unvisited.add(n)
            }
            val newDist = shortestDistance[minVertex]!! + graph[n]!!
            if (newDist < shortestDistance[n]!!) {
                shortestDistance[n] = newDist
            }
        }
    }

    return shortestDistance.toMap()
}