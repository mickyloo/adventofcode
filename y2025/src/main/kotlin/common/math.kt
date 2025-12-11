package common

import kotlin.math.max
import kotlin.math.min

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

infix fun LongRange.merge(other: LongRange): LongRange {
    if (!(this anyOverlap other)) {
        throw ArithmeticException("Cannot merge $this and $other")
    }
    return min(this.first, other.first)..max(this.last, other.last)
}

fun <T> List<T>.combinations(): Sequence<Pair<T,T>> = sequence {
    for(i in 0 until this@combinations.size-1)
        for(j in i+1 until this@combinations.size)
            yield(this@combinations[i] to this@combinations[j])
}

fun <T> Collection<T>.powerset(): Set<Set<T>> =
    powerset(this, setOf(emptySet()))

private tailrec fun <T> powerset(left: Collection<T>, acc: Set<Set<T>>): Set<Set<T>> =
    if (left.isEmpty()) acc
    else powerset(left.drop(1), acc + acc.map { it + left.first() })