package common

data class Point(val x: Int, val y: Int) {
    fun neighbors4(): Set<Point> =
        setOf(
            Point(x+1, y),
            Point(x-1, y),
            Point(x, y-1),
            Point(x, y+1),
        )

    fun neighbors8(): Set<Point> =
        neighbors4() + setOf(
            Point(x+1, y+1),
            Point(x-1, y+1),
            Point(x+1, y-1),
            Point(x-1, y-1),
        )
}
