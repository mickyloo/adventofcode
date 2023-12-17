package common

data class Point(val x: Int, val y: Int) {
    fun neighbors4(): Set<Point> =
        setOf(
            Point(x + 1, y),
            Point(x - 1, y),
            Point(x, y - 1),
            Point(x, y + 1),
        )

    fun neighbors8(): Set<Point> =
        neighbors4() + setOf(
            Point(x + 1, y + 1),
            Point(x - 1, y + 1),
            Point(x + 1, y - 1),
            Point(x - 1, y - 1),
        )
}

val NEIGHBORS4 = setOf(
    1 to 0, -1 to 0, 0 to -1, 0 to 1,
)

val NEIGHBORS8 = NEIGHBORS4 + setOf(
    1 to 1, -1 to 1, 1 to -1, -1 to -1
)

enum class Cardinal(val dy: Int, val dx: Int) {
    N(-1, 0),
    W(0, -1),
    S(1, 0),
    E(0, 1)
}