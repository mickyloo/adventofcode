package common

fun gcd(a: Int, b: Int): Int {
    var i = 1
    var gcd = 1
    while (i <= a && i <= b) {
        if (a % i == 0 && b % i == 0) gcd = i
        ++i
    }
    return gcd
}

fun gcd(a: Long, b: Long): Long {
    var i = 1L
    var gcd = 1L
    while (i <= a && i <= b) {
        if (a % i == 0L && b % i == 0L) gcd = i
        ++i
    }
    return gcd
}

fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)
fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

infix fun LongRange.fullOverlap(other: LongRange): Boolean =
    (this.first >= other.first && this.last <= other.last) || (this.first <= other.first && this.last >= other.last)
infix fun LongRange.anyOverlap(other: LongRange): Boolean =
    !(this.last < other.first || this.first > other.last)