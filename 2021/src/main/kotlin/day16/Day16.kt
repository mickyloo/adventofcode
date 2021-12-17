package day16

import java.io.File
import kotlin.system.measureTimeMillis

private val HEX2BIN = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111"
)


fun main() {
    val stream = File("src/main/kotlin/day16/input.txt")
        .readText().trim()
        .map { HEX2BIN.getOrDefault(it, "") }
        .joinToString("")

    val elapsed1 = measureTimeMillis { part1(stream) }
    val elapsed2 = measureTimeMillis { part2(stream) }

    println("Part1: Took $elapsed1 ms")
    println("Part2: Took $elapsed2 ms")
}

class Reader(private val stream: String) {

    private var pointer = 0

    private fun read(length: Int): String {
        val out = stream.substring(pointer, pointer + length)
        pointer += length
        return out
    }

    private fun readLiteralValue(): Long {
        var binary = ""
        do {
            val byte = read(5)
            val leadingBit = byte[0]
            binary += byte.substring(1)
        } while (leadingBit == '1')
        return binary.toLong(2)
    }

    private fun readSubPacketByBitLength(bits: Int): List<Packet> {
        val start = pointer
        val subPackets = mutableListOf<Packet>()
        while (pointer < start + bits) {
            readPacket()?.let { packet -> subPackets.add(packet) }
        }
        return subPackets
    }

    private fun readSubPacketbyCount(count: Int): List<Packet> {
        val subPackets = mutableListOf<Packet>()
        repeat(count) {
            readPacket()?.let { packet -> subPackets.add(packet) }
        }
        return subPackets
    }

    fun readPacket(): Packet? {
        val version = read(3).toInt(2)
        val type = read(3).toInt(2)

        return if (type == 4) {
            Literal(version, readLiteralValue())
        } else {
            val typeLength = read(1).toInt()
            val subpackets: List<Packet> = if (typeLength == 0) {
                readSubPacketByBitLength(read(15).toInt(2))
            } else {
                readSubPacketbyCount(read(11).toInt(2))
            }

            Operator(version, type, subpackets)
        }
    }
}

abstract class Packet(val version: Int, val type: Int) {
    open fun totalVersion(): Int = version
    abstract fun eval(): Long
}

class Literal(version: Int, private val value: Long) : Packet(version, 4) {
    override fun eval(): Long = value
}

class Operator(version: Int, type: Int, private val subPackets: List<Packet>) : Packet(version, type) {
    override fun totalVersion(): Int {
        return version + subPackets.sumOf { it.totalVersion() }
    }

    override fun eval(): Long =
        when (type) {
            0 -> subPackets.sumOf { it.eval() }
            1 -> subPackets.fold(1L) { acc, packet -> acc * packet.eval() }
            2 -> subPackets.minOf { it.eval() }
            3 -> subPackets.maxOf { it.eval() }
            5 -> if (subPackets[0].eval() > subPackets[1].eval()) 1 else 0
            6 -> if (subPackets[0].eval() < subPackets[1].eval()) 1 else 0
            7 -> if (subPackets[0].eval() == subPackets[1].eval()) 1 else 0
            else -> 0
        }
}

fun part2(stream: String) {
    val reader = Reader(stream)
    val packet = reader.readPacket()
    println(packet?.eval())
}

fun part1(stream: String) {
    val reader = Reader(stream)
    val packet = reader.readPacket()
    println(packet?.totalVersion())
}
