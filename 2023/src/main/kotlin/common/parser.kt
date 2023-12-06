package common

import java.io.File

fun inputFile(name: String) = File("src/main/kotlin/$name")

fun readLines(name: String) = inputFile(name).readLines()

fun readText(name: String) = inputFile(name).readText().trimEnd()

fun readParagraphs(name: String) = inputFile(name).readText().trimEnd().split("\n\n", "\r\n\r\n")

fun Any?.println() { println(this) }

val digitsRegex = """(\d)+""".toRegex()
fun String.toNumbers() = digitsRegex.findAll(this).map { it.value.toInt() }.toList()