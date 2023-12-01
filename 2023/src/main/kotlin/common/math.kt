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

fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)

